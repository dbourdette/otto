/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dbourdette.otto.source.schedule;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.source.Source;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * All schedules for a {@link com.github.dbourdette.otto.source.Source}
 *
 * @author damien bourdette
 */
public class SourceSchedules {
    private DBCollection schedules;

    private Source source;

    public static SourceSchedules forSource(Source source) {
        SourceSchedules schedules = new SourceSchedules();

        schedules.source = source;
        schedules.schedules = Registry.mongoDb.getCollection(Source.qualifiedSchedulesName(source.getName()));

        return schedules;
    }

    public void schedule(MailSchedule schedule) {
        if (StringUtils.isEmpty(schedule.getId())) {
            BasicDBObject object = schedule.toDBObject();

            schedules.save(object);

            schedule.setId(((ObjectId) object.get("_id")).toString());
        } else {
            schedules.update(new BasicDBObject("_id", new ObjectId(schedule.getId())), schedule.toDBObject());
        }
    }

    public MailSchedule getSchedule(String id) {
        return MailSchedule.read((BasicDBObject) schedules.findOne(new BasicDBObject("_id", new ObjectId(id))));
    }

    public List<MailSchedule> getSchedules() {
        return MailSchedule.readAll(schedules.find().sort(new BasicDBObject("title", 1)));
    }

    public void deleteSchedule(String id) {
        schedules.remove(new BasicDBObject("_id", new ObjectId(id)));
    }
}
