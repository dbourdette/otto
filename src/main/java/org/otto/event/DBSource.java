package org.otto.event;

import java.util.Iterator;

import org.joda.time.Interval;
import org.otto.web.util.Frequency;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 */
public class DBSource {
	private DBCollection events;
	
	private DBCollection config;
	
	public static DBSource fromCollection(DBCollection events, DBCollection config) {
		DBSource source = new DBSource();
		
		source.events = events;
		source.config = config;
		
		return source;
	}
	
	public long count() {
		return events.count();
	}
	
	public boolean isCapped() {
		return events.isCapped();
	}
	
	public Frequency frequency(Interval interval) {
		BasicDBObject query = intervalQuery(interval);

		int count = events.find(query).count();

		return new Frequency(count, interval.toDuration());
	}

    public Iterator<DBObject> findEvents(Interval interval) {
        BasicDBObject query = intervalQuery(interval);

        return events.find(query).sort(new BasicDBObject("date", -1)).iterator();
    }

    public Iterator<DBObject> findEvents(int count) {
    	return events.find().sort(new BasicDBObject("date", -1)).limit(count).iterator();
    }
	
	public void post(Event event) {
		TimeFrame timeFrame = getTimeFrame();
		
		if (timeFrame == null || timeFrame == TimeFrame.MILLISECOND) {
			events.insert(event.toDBObject());	
		} else {
			event.setDate(timeFrame.roundDate(event.getDate()));
			
			BasicDBObject inc = new BasicDBObject();
			inc.put("$inc", new BasicDBObject("count", 1));
			
			events.update(event.toDBObject(), inc, true, false);
		}
	}
	
	public void clearEvents() {
		events.remove(new BasicDBObject());
	}

	public void saveTimeFrame(TimeFrame timeFrame) {
		BasicDBObject filter = new BasicDBObject();
		filter.put("name", "aggregation");

		BasicDBObject values = new BasicDBObject();
		values.put("name", "aggregation");

		if (timeFrame == null) {
			values.put("value", null);
		} else {
			values.put("value", timeFrame.name());
		}

		config.update(filter, values, true, false);
	}

	public TimeFrame getTimeFrame() {
		DBCursor cursor = config.find(new BasicDBObject("name", "aggregation"));
		
		if (!cursor.hasNext()) {
			return TimeFrame.MILLISECOND;
		}
		
		DBObject property = cursor.next();
		
		if (property == null) {
			return TimeFrame.MILLISECOND;
		}
		
		String value = (String) property.get("value");
		
		if (value == null) {
			return TimeFrame.MILLISECOND;
		}
		
		return TimeFrame.valueOf(value);
	}

	public void drop() {
		events.drop();
		config.drop();
	}
	
	private BasicDBObject intervalQuery(Interval interval) {
		BasicDBObject criteria = new BasicDBObject();

		criteria.append("$gt", interval.getStart().toDate());
		criteria.append("$lte", interval.getEnd().toDate());

		return new BasicDBObject("date", criteria);
	}
}
