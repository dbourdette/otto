package com.github.dbourdette.otto.source.schedule;

import java.util.List;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.github.dbourdette.otto.source.Source;
import com.google.code.morphia.Datastore;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Service
public class SourceSchedules {
    @Inject
    private Datastore datastore;
    
    public MailSchedule findById(String id) {
        return datastore.get(MailSchedule.class, new ObjectId(id));
    }

    public List<MailSchedule> findForSource(Source source) {
        return datastore.find(MailSchedule.class).filter("sourceName", source.getName()).order("title").asList();
    }

    public List<MailSchedule> findForAllSources() {
        return datastore.find(MailSchedule.class).filter("sourceName", Source.ALL_SOURCES).order("title").asList();
    }

    public void save(MailSchedule mailSchedule) {
        datastore.save(mailSchedule);
    }

    public void delete(MailSchedule mailSchedule) {
        datastore.delete(mailSchedule);
    }
}
