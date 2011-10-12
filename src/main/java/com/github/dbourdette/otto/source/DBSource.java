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

package com.github.dbourdette.otto.source;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.joda.time.Interval;

import com.github.dbourdette.otto.util.Page;
import com.github.dbourdette.otto.web.exception.SourceNotFound;
import com.github.dbourdette.otto.web.form.CappingForm;
import com.github.dbourdette.otto.web.form.GraphPeriod;
import com.github.dbourdette.otto.web.util.Constants;
import com.github.dbourdette.otto.web.util.Frequency;
import com.github.dbourdette.otto.web.util.IntervalUtils;
import com.github.dbourdette.otto.web.util.SizeInBytes;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class DBSource {
    private static final int PAGE_SIZE = 100;

    private DB mongoDb;

    private String name;

    private DBCollection events;

    private DBCollection config;

    private DBCollection mailReports;

    public static DBSource fromDb(DB mongoDb, String name) {
        if (!mongoDb.collectionExists(qualifiedName(name))) {
            throw new SourceNotFound();
        }

        DBSource source = new DBSource();

        source.mongoDb = mongoDb;
        source.name = name;
        source.events = mongoDb.getCollection(qualifiedName(name));
        source.config = mongoDb.getCollection(qualifiedConfigName(name));
        source.mailReports = mongoDb.getCollection(qualifiedMailReportsName(name));

        return source;
    }

    public static String qualifiedName(String name) {
        return Constants.SOURCES + name + Constants.EVENTS;
    }

    public static String qualifiedConfigName(String name) {
        return Constants.SOURCES + name + Constants.CONFIG;
    }

    public static String qualifiedMailReportsName(String name) {
        return Constants.SOURCES + name + "." + Constants.MAIL_CONFIG;
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

    public String getMailReportsCollectionName() {
        return mailReports.getName();
    }

    public CommandResult getStats() {
        return events.getStats();
    }

    public Iterator<DBObject> findEvents(Interval interval) {
        BasicDBObject query = IntervalUtils.query(interval);

        return events.find(query).sort(new BasicDBObject("date", -1)).iterator();
    }

    public Page<DBObject> findEvents(Integer page) {
        return Page.fromCursor(events.find().sort(new BasicDBObject("date", -1)), page, PAGE_SIZE);
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
        BasicDBObject filter = new BasicDBObject("name", "aggregation");

        BasicDBObject values = new BasicDBObject("name", "aggregation");

        if (form.getTimeFrame() == null) {
            values.put("timeFrame", null);
        } else {
            values.put("timeFrame", form.getTimeFrame().name());
        }

        values.put("attributeName", form.getAttributeName());

        config.update(filter, values, true, false);
    }

    public AggregationConfig getAggregation() {
        DBObject dbObject = findConfigItem("aggregation");

        AggregationConfig aggregationConfig = new AggregationConfig();

        if (dbObject != null) {
            String timeFrame = (String) dbObject.get("timeFrame");

            aggregationConfig.setTimeFrame(TimeFrame.safeValueOf(timeFrame, TimeFrame.MILLISECOND));
            aggregationConfig.setAttributeName((String) dbObject.get("attributeName"));
        }

        return aggregationConfig;
    }

    public DefaultGraphParameters getDefaultGraphParameters() {
        DBObject dbObject = findConfigItem("defaultGraphParameters");

        DefaultGraphParameters parameters = new DefaultGraphParameters();

        if (dbObject != null) {
            parameters.setSplitColumn((String) dbObject.get("splitColumn"));
            parameters.setSumColumn((String) dbObject.get("sumColumn"));
        }

        return parameters;
    }

    public void setDefaultGraphParameters(DefaultGraphParameters params) {
        BasicDBObject filter = new BasicDBObject("name", "defaultGraphParameters");

        BasicDBObject values = new BasicDBObject("name", "defaultGraphParameters");

        values.put("splitColumn", params.getSplitColumn());
        values.put("sumColumn", params.getSumColumn());

        config.update(filter, values, true, false);
    }

    public void cap(CappingForm form) {
        SizeInBytes sizeInBytes = form.getSizeInBytes();

        if (sizeInBytes == null) {
            return;
        }

        BasicDBObject capping = new BasicDBObject();

        capping.put("convertToCapped", qualifiedName(name));
        capping.put("capped", true);
        capping.put("size", sizeInBytes.getValue());

        mongoDb.command(capping);
    }

    public void drop() {
        events.drop();
        config.drop();
    }

    public String getName() {
        return name;
    }

    public List<MailReportConfig> getMailReports() {
        List<DBObject> objects = mailReports.find().toArray();

        List<MailReportConfig> mailReports = new ArrayList<MailReportConfig>();

        for (DBObject object : objects) {
            mailReports.add(toMailReportConfig(object));
        }

        return mailReports;
    }

    public void saveMailReport(MailReportConfig mailReport) {
        BasicDBObject object = new BasicDBObject();

        if (StringUtils.isNotEmpty(mailReport.getId())) {
            object.put("_id", new ObjectId(mailReport.getId()));
        } else {
            ObjectId objectId = new ObjectId();

            object.put("_id", objectId);

            mailReport.setId(objectId.toString());
        }

        mailReport.setSourceName(name);

        object.put("sourceName", name);
        object.put("cronExpression", mailReport.getCronExpression());
        object.put("to", mailReport.getTo());
        object.put("title", mailReport.getTitle());
        object.put("period", mailReport.getPeriod().name());
        object.put("splitColumn", mailReport.getSplitColumn());
        object.put("sumColumn", mailReport.getSumColumn());

        mailReports.save(object);
    }

    public MailReportConfig getMailReport(String id) {
        DBObject object = mailReports.findOne(new BasicDBObject("_id", new ObjectId(id)));

        if (object == null) {
            return null;
        }

        return toMailReportConfig(object);
    }

    public MailReportConfig deleteMailReport(String id) {
        MailReportConfig config = getMailReport(id);

        if (config == null) {
            return null;
        }

        mailReports.remove((new BasicDBObject("_id", new ObjectId(id))));

        return config;
    }

    private DBObject findConfigItem(String name) {
        DBCursor cursor = config.find(new BasicDBObject("name", name));

        if (!cursor.hasNext()) {
            return null;
        }

        return cursor.next();
    }

    private MailReportConfig toMailReportConfig(DBObject object) {
        MailReportConfig mailReport = new MailReportConfig();

        mailReport.setId(((ObjectId) object.get("_id")).toString());
        mailReport.setSourceName((String) object.get("sourceName"));
        mailReport.setCronExpression((String) object.get("cronExpression"));
        mailReport.setTo((String) object.get("to"));
        mailReport.setTitle((String) object.get("title"));
        mailReport.setPeriod(GraphPeriod.valueOf((String) object.get("period")));
        mailReport.setSplitColumn((String) object.get("splitColumn"));
        mailReport.setSumColumn((String) object.get("sumColumn"));

        return mailReport;
    }
}


