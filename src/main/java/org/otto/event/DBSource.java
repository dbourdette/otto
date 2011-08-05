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

package org.otto.event;

import com.mongodb.*;
import org.joda.time.Interval;
import org.otto.web.util.Frequency;
import org.otto.web.util.IntervalUtils;
import org.otto.web.util.SizeInBytes;

import java.util.Iterator;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class DBSource {
    private DBCollection events;

    private DBCollection config;

    public static DBSource fromCollection(DBCollection events, DBCollection config) {
        DBSource source = new DBSource();

        source.events = events;
        source.config = config;

        return source;
    }

    public long getCount() {
        return events.count();
    }

    public boolean isCapped() {
        return events.isCapped();
    }

    public SizeInBytes getSize() {
        return new SizeInBytes(events.getStats().getLong("storageSize"));
    }

    public Long getMax() {
        return events.getStats().getLong("max");
    }

    public String getCollectionName() {
        return events.getName();
    }

    public String getConfigCollectionName() {
        return config.getName();
    }

    public CommandResult getStats() {
        return events.getStats();
    }

    public Iterator<DBObject> findEvents(Interval interval) {
        BasicDBObject query = IntervalUtils.query(interval);

        return events.find(query).sort(new BasicDBObject("date", -1)).iterator();
    }

    public Iterator<DBObject> findEvents(int count) {
        return events.find().sort(new BasicDBObject("date", -1)).limit(count).iterator();
    }

    public Frequency findEventsFrequency(Interval interval) {
        BasicDBObject query = IntervalUtils.query(interval);

        int count = events.find(query).count();

        return new Frequency(count, interval.toDuration());
    }

    public void post(Event event) {
        AggregationConfig config = getAggregation();

        if (config.getTimeFrame() == null || config.getTimeFrame() == TimeFrame.MILLISECOND) {
            events.insert(event.toDBObject());
        } else {
            event.setDate(config.getTimeFrame().roundDate(event.getDate()));

            BasicDBObject inc = new BasicDBObject();
            inc.put("$inc", new BasicDBObject(config.getAttributeName(), 1));

            events.update(event.toDBObject(), inc, true, false);
        }
    }

    public void clearEvents() {
        events.remove(new BasicDBObject());
    }

    public void saveAggregation(AggregationConfig form) {
        BasicDBObject filter = new BasicDBObject();
        filter.put("name", "aggregation");

        BasicDBObject values = new BasicDBObject();
        values.put("name", "aggregation");

        if (form.getTimeFrame() == null) {
            values.put("timeFrame", null);
        } else {
            values.put("timeFrame", form.getTimeFrame().name());
        }

        values.put("attributeName", form.getAttributeName());

        config.update(filter, values, true, false);
    }

    public AggregationConfig getAggregation() {
        DBCursor cursor = config.find(new BasicDBObject("name", "aggregation"));

        if (!cursor.hasNext()) {
            return new AggregationConfig();
        }

        DBObject property = cursor.next();

        if (property == null) {
            return new AggregationConfig();
        }

        AggregationConfig config = new AggregationConfig();

        String timeFrame = (String) property.get("timeFrame");

        if (timeFrame != null) {
            config.setTimeFrame(TimeFrame.valueOf(timeFrame));
        }

        config.setAttributeName((String) property.get("attributeName"));

        return config;
    }

    public void drop() {
        events.drop();
        config.drop();
    }
}
