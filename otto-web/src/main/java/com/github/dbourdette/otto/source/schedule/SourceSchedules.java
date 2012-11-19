package com.github.dbourdette.otto.source.schedule;

import java.util.List;

import org.bson.types.ObjectId;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.source.Source;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SourceSchedules {
    private String sourceName;

    public static SourceSchedules forSource(Source source) {
        SourceSchedules schedules = new SourceSchedules();

        schedules.sourceName = source.getName() ;

        return schedules;
    }

    public MailSchedule getSchedule(String id) {
        return Registry.datastore.get(MailSchedule.class, new ObjectId(id));
    }

    public List<MailSchedule> getSchedules() {
        return Registry.datastore.find(MailSchedule.class).filter("sourceName", sourceName).order("title").asList();
    }
}
