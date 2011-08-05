package org.otto.event;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.apache.commons.lang.StringUtils;
import org.otto.web.exception.SourceNotFound;
import org.otto.web.form.SourceForm;
import org.otto.web.util.Constants;
import org.otto.web.util.SizeInBytes;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
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

        SizeInBytes sizeInBytes = form.getSizeInBytes();

        if (sizeInBytes == null) {
            capping.put("capped", false);
        } else {
            capping.put("capped", true);
            capping.put("size", sizeInBytes.getValue());

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
