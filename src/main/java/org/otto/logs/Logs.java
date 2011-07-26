package org.otto.logs;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.otto.web.util.Constants;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 */
@Component
public class Logs {

	@Inject
	private DB mongoDb;

	@PostConstruct
	public void createCollection() {
		if (mongoDb.collectionExists(Constants.LOGS)) {
			return;
		}

		BasicDBObject capping = new BasicDBObject();

		capping.put("capped", true);
		capping.put("size", 100000);

		mongoDb.createCollection(Constants.LOGS, capping);
	}

	public void trace(String message) {
		BasicDBObject log = new BasicDBObject();

		log.put("date", new Date());
		log.put("message", message);

		logs().insert(log);
	}

	public List<DBObject> top() {
		return logs().find().limit(100).toArray();
	}

	private DBCollection logs() {
		return mongoDb.getCollection(Constants.LOGS);
	}
}
