package org.otto.event;

import com.mongodb.*;
import org.joda.time.Interval;
import org.otto.web.util.Frequency;
import org.otto.web.util.IntervalUtils;
import org.otto.web.util.SizeInBytes;

import java.util.Iterator;

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
	
	public long getCount() {
		return events.count();
	}
	
	public boolean isCapped() {
		return events.isCapped();
	}

	public SizeInBytes getSize() {
		return new SizeInBytes(events.getStats().getLong("storageSize"));
	}

	public Long getMax() {
		return events.getStats().getLong("max");
	}

	public String getCollectionName() {
		return events.getName();
	}

	public String getConfigCollectionName() {
		return config.getName();
	}

	public CommandResult getStats() {
		return events.getStats();
	}

    public Iterator<DBObject> findEvents(Interval interval) {
        BasicDBObject query = IntervalUtils.query(interval);

        return events.find(query).sort(new BasicDBObject("date", -1)).iterator();
    }

    public Iterator<DBObject> findEvents(int count) {
    	return events.find().sort(new BasicDBObject("date", -1)).limit(count).iterator();
    }

	public Frequency findEventsFrequency(Interval interval) {
		BasicDBObject query = IntervalUtils.query(interval);

		int count = events.find(query).count();

		return new Frequency(count, interval.toDuration());
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
}
