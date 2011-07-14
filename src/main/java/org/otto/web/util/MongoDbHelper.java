package org.otto.web.util;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

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

    public DBCollection getCollection(String name) {
        return mongoDb.getCollection(EVENTS_PREFIX + name);
    }
}
