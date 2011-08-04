package org.otto.event;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.otto.web.exception.SourceNotFound;
import org.otto.web.form.SourceForm;
import org.otto.web.util.Constants;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * User: damien bourdette Date: 14/07/11 Time: 13:34
 */
@Component
public class Sources {

	@Inject
	private DB mongoDb;

	public DBSource getSource(String name) {
		if (!mongoDb.collectionExists(qualifiedName(name))) {
			throw new SourceNotFound();
		}

        DBCollection events = mongoDb.getCollection(qualifiedName(name));
        DBCollection config = mongoDb.getCollection(qualifiedConfigName(name));

		return DBSource.fromCollection(events, config);
	}

	public DBSource createSource(SourceForm form) {
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

		DBCollection events = mongoDb.createCollection(qualifiedName(form.getName()), capping);
        DBCollection config = mongoDb.getCollection(qualifiedConfigName(form.getName()));

        return DBSource.fromCollection(events, config);
	}

	public List<String> getNames() {
		List<String> sources = new ArrayList<String>();

		for (String name : mongoDb.getCollectionNames()) {
			if (name.startsWith(Constants.SOURCES) && name.endsWith(Constants.EVENTS)) {
				sources.add(StringUtils.substringBetween(name, Constants.SOURCES, Constants.EVENTS));
			}
		}

		return sources;
	}

	private String qualifiedName(String name) {
		return Constants.SOURCES + name + Constants.EVENTS;
	}

	private String qualifiedConfigName(String name) {
		return Constants.SOURCES + name + Constants.CONFIG;
	}
}
