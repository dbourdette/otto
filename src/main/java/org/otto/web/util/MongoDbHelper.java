package org.otto.web.util;

import javax.inject.Inject;

import org.joda.time.Interval;
import org.otto.web.exception.TypeNotFound;
import org.otto.web.form.TypeForm;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * User: damien bourdette
 * Date: 14/07/11
 * Time: 13:34
 */
@Component
public class MongoDbHelper {

    public static final String EVENTS_PREFIX = "events.";

    @Inject
    private DB mongoDb;

    public boolean notExists(String name) {
        return !mongoDb.collectionExists(EVENTS_PREFIX + name);
    }

    public Frequency frequency(String name, Interval interval) {
    	DBCollection collection = getCollection(name);

        BasicDBObject query = intervalQuery(interval);

        int count = collection.find(query).count();
    	
        return new Frequency(count, interval.toDuration());
    }
    
    public BasicDBObject intervalQuery(Interval interval) {
    	BasicDBObject criteria = new BasicDBObject();
    	
    	criteria.append("$gt", interval.getStart().toDate());
    	criteria.append("$lte", interval.getEnd().toDate());
        
        return new BasicDBObject("date", criteria);
    }

    public DBCollection getCollection(String name) {
    	if (notExists(name)) {
    		throw new TypeNotFound();
    	} 
    	
        return mongoDb.getCollection(EVENTS_PREFIX + name);
    }

    public DBCollection createCollection(TypeForm form) {
    	BasicDBObject capping = new BasicDBObject();
    	
    	if (form.getSizeInBytes() == null) {
    		capping.put("capped", false);
    	} else {
    		capping.put("capped", true);
    		capping.put("size", form.getSizeInBytes());
    		
    		if (form.getMaxEvents() != null) {
    			capping.put("max", form.getMaxEvents());
    		}
    	}

        return mongoDb.createCollection(EVENTS_PREFIX + form.getName(), capping);
    }
}
