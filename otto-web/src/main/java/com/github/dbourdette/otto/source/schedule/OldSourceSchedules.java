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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.source.OldSource;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * All schedules for a {@link com.github.dbourdette.otto.source.OldSource}
 *
 * @author damien bourdette
 */
public class OldSourceSchedules {
    private DBCollection schedules;

    private OldSource source;

    public static OldSourceSchedules forSource(OldSource source) {
        OldSourceSchedules schedules = new OldSourceSchedules();

        schedules.source = source;
        schedules.schedules = Registry.mongoDb.getCollection(OldSource.qualifiedSchedulesName(source.getName()));

        return schedules;
    }

    public void schedule(OldMailSchedule schedule) {
        if (StringUtils.isEmpty(schedule.getId())) {
            BasicDBObject object = schedule.toDBObject();

            schedules.save(object);

            schedule.setId(((ObjectId) object.get("_id")).toString());
        } else {
            schedules.update(new BasicDBObject("_id", new ObjectId(schedule.getId())), schedule.toDBObject());
        }
    }

    public OldMailSchedule getSchedule(String id) {
        return OldMailSchedule.read((BasicDBObject) schedules.findOne(new BasicDBObject("_id", new ObjectId(id))));
    }

    public List<OldMailSchedule> getSchedules() {
        return OldMailSchedule.readAll(schedules.find().sort(new BasicDBObject("title", 1)));
    }

    public void deleteSchedule(String id) {
        schedules.remove(new BasicDBObject("_id", new ObjectId(id)));
    }

    /**
     * Drop underlying mongodb collection
     */
    public void dropCollection() {
        schedules.drop();
    }
}
