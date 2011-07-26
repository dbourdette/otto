package org.otto.event;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Interval;
import org.otto.web.exception.SourceNotFound;
import org.otto.web.form.SourceForm;
import org.otto.web.util.Constants;
import org.otto.web.util.Frequency;
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
public class Sources {

	
	
	@Inject
    private DB mongoDb;

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
    		throw new SourceNotFound();
    	} 
    	
        return mongoDb.getCollection(qualifiedName(name));
    }

    public DBCollection createCollection(SourceForm form) {
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

        return mongoDb.createCollection(qualifiedName(form.getName()), capping);
    }
    
    public List<String> getNames() {
    	List<String> sources = new ArrayList<String>();

        for (String name : mongoDb.getCollectionNames()) {
            if (name.startsWith(Constants.OTTO) && name.endsWith(Constants.EVENTS)) {
            	sources.add(StringUtils.substringBetween(name, Constants.OTTO, Constants.EVENTS));
            }
        }
        
        return sources;
    }
    
    public String qualifiedName(String name) {
    	return Constants.OTTO + name + Constants.EVENTS;
    }

    private boolean notExists(String name) {
        return !mongoDb.collectionExists(qualifiedName(name));
    }
}
