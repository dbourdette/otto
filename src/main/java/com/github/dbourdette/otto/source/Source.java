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

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.report.Report;
import com.github.dbourdette.otto.report.ReportPeriod;
import com.github.dbourdette.otto.report.filler.OperationChain;
import com.github.dbourdette.otto.source.config.*;
import com.github.dbourdette.otto.util.Page;
import com.github.dbourdette.otto.web.exception.SourceAlreadyExists;
import com.github.dbourdette.otto.web.exception.SourceNotFound;
import com.github.dbourdette.otto.web.form.CappingForm;
import com.github.dbourdette.otto.web.form.IndexForm;
import com.github.dbourdette.otto.web.form.Sort;
import com.github.dbourdette.otto.web.form.SourceForm;
import com.github.dbourdette.otto.web.util.Constants;
import com.github.dbourdette.otto.web.util.Frequency;
import com.github.dbourdette.otto.web.util.IntervalUtils;
import com.github.dbourdette.otto.web.util.SizeInBytes;
import com.mongodb.*;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.joda.time.Interval;
import org.quartz.SchedulerException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class Source {
    private static final int PAGE_SIZE = 100;

    private String name;

    private String displayName;

    private String displayGroup;

    private DBCollection events;

    private DBCollection config;

    private DBCollection mailReports;

    private volatile AggregationConfig aggregationConfig;

    private volatile TransformConfig transformConfig;

    public static String qualifiedName(String name) {
        return Constants.SOURCES + name + Constants.EVENTS;
    }

    public static String qualifiedConfigName(String name) {
        return Constants.SOURCES + name + Constants.CONFIG;
    }

    public static String qualifiedMailReportsName(String name) {
        return Constants.SOURCES + name + "." + Constants.MAIL_CONFIG;
    }

    public static String qualifiedReportsName(String name) {
        return Constants.SOURCES + name + "." + Constants.REPORTS;
    }

    public static String qualifiedSchedulesName(String name) {
        return Constants.SOURCES + name + "." + Constants.SCHEDULES;
    }

    public static Source create(String name) {
        if (exists(name)) {
            throw new SourceAlreadyExists();
        }

        String collectionName = qualifiedName(name);

        Registry.mongoDb.createCollection(collectionName, new BasicDBObject());

        return findByName(name);
    }

    public static Source create(SourceForm form) {
        if (exists(form.getName())) {
            throw new SourceAlreadyExists();
        }

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

        String collectionName = qualifiedName(form.getName());

        Registry.mongoDb.createCollection(collectionName, capping);

        Source source = findByName(form.getName());

        source.updateDisplayGroupAndName(form.getDisplayGroup(), form.getDisplayName());

        return source;
    }

    public static boolean exists(String name) {
        String collectionName = qualifiedName(name);

        return Registry.mongoDb.collectionExists(collectionName);
    }

    public static Source findByName(String name) {
        if (!exists(name)) {
            throw new SourceNotFound();
        }

        Element element = Registry.sourceCache.get(name);
        
        if (element != null) {
           return (Source) element.getObjectValue(); 
        }
        
        Source source = new Source();

        source.name = name;
        source.events = Registry.mongoDb.getCollection(qualifiedName(name));
        source.config = Registry.mongoDb.getCollection(qualifiedConfigName(name));
        source.mailReports = Registry.mongoDb.getCollection(qualifiedMailReportsName(name));

        source.loadAggregation();
        source.loadDisplay();
        source.loadTransformConfig();

        Registry.sourceCache.put(new Element(name, source));
        
        return source;
    }

    public static Collection<String> findAllNames() {
        List<String> sources = new ArrayList<String>();

        for (String name : Registry.mongoDb.getCollectionNames()) {
            if (name.startsWith(Constants.SOURCES) && name.endsWith(Constants.EVENTS)) {
                sources.add(StringUtils.substringBetween(name, Constants.SOURCES, Constants.EVENTS));
            }
        }

        return sources;
    }

    public static Collection<Source> findAll() {
        List<Source> sources = new ArrayList<Source>();

        for (String name : findAllNames()) {
            sources.add(findByName(name));
        }

        return sources;
    }

    public void drop() throws SchedulerException {
        for (MailReportConfig report : getMailReports()) {
            Registry.mailReports.onReportDeleted(report);
        }

        events.drop();
        config.drop();
        Registry.mongoDb.getCollection(qualifiedReportsName(name)).drop();
        mailReports.drop();

        Registry.sourceCache.remove(name);
    }

    public void updateDisplayGroupAndName(String group, String name) {
        BasicDBObject filter = new BasicDBObject("name", "display");

        BasicDBObject values = new BasicDBObject("name", "display");

        values.put("displayGroup", group.trim());
        values.put("displayName", name.trim());

        config.update(filter, values, true, false);

        loadDisplay();
    }

    public Report buildReport(ReportConfig config, ReportPeriod period) {
        Report report = new Report();

        report.createRows(period);

        OperationChain chain = config.buildChain(report);

        Iterator<DBObject> events = findEvents(period.getInterval());

        while (events.hasNext()) {
            DBObject event = events.next();

            chain.write(event);
        }

        if (report.getColumnCount() == 0) {
            report.ensureColumnExists("no data");
        }

        if (config.getSort() == Sort.ALPHABETICALLY) {
            report.sortAlphabetically();
        } else if (config.getSort() == Sort.BY_SUM) {
            report.sortBySum();
        }

        return report;
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

        int count = 0;

        if (aggregationConfig.isAggregating()) {
            String attName = aggregationConfig.getAttributeName();

            BasicDBObject fields = new BasicDBObject(attName, "1");

            Iterator<DBObject> iterator = events.find(query, fields).iterator();

            while (iterator.hasNext()) {
                Object value = iterator.next().get(attName);

                if (value instanceof Integer) {
                    count += (Integer) value;
                } else {
                    count += 1;
                }
            }
        } else {
            count = (int) events.count(query);
        }

        return new Frequency(count, interval.toDuration());
    }

    public void post(Event event) {
        transformConfig.applyOn(event);

        AggregationConfig config = aggregationConfig;

        if (config.isAggregating()) {
            event.setDate(config.getTimeFrame().roundDate(event.getDate()));

            BasicDBObject inc = new BasicDBObject();
            inc.put("$inc", new BasicDBObject(config.getAttributeName(), 1));

            events.update(event.toDBObject(), inc, true, false);
        } else {
            events.insert(event.toDBObject());
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

        loadAggregation();
    }

    public void saveTransformConfig(TransformConfig transformConfig) {
        BasicDBObject filter = new BasicDBObject("name", "transform");

        config.update(filter, transformConfig.toDBObject(), true, false);

        loadTransformConfig();
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

        Registry.mongoDb.command(capping);
    }

    public DefaultGraphParameters getDefaultGraphParameters() {
        DBObject dbObject = findConfigItem("defaultGraphParameters");

        DefaultGraphParameters parameters = new DefaultGraphParameters();

        if (dbObject != null) {
            try {
                parameters.setPeriod(ReportPeriod.valueOf((String) dbObject.get("period")));
            } catch (Exception e) {
                // well, value was not correct in db
            }
        }

        return parameters;
    }

    public void setDefaultGraphParameters(DefaultGraphParameters params) {
        BasicDBObject filter = new BasicDBObject("name", "defaultGraphParameters");

        BasicDBObject values = new BasicDBObject("name", "defaultGraphParameters");

        values.put("period", params.getPeriod().name());

        config.update(filter, values, true, false);
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
        object.put("reportTitle", mailReport.getReportTitle());

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

    public List<DBObject> getIndexes() {
        return events.getIndexInfo();
    }

    public void createIndex(IndexForm form) {
        BasicDBObject keys = new BasicDBObject(form.getKey(), form.isAscending() ? 1 : -1);

        String name = form.getKey() + (form.isAscending() ? "_1" : "_-1");

        BasicDBObject options = new BasicDBObject("name", name);

        if (form.isBackground()) {
            options.put("background", "1");
        }

        events.ensureIndex(keys, options);
    }

    public void dropIndex(String index) {
        events.dropIndex(index);
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
        mailReport.setPeriod(ReportPeriod.valueOf((String) object.get("period")));
        mailReport.setReportTitle((String) object.get("reportTitle"));

        return mailReport;
    }

    public void loadAggregation() {
        DBObject dbObject = findConfigItem("aggregation");

        AggregationConfig temp = new AggregationConfig();

        if (dbObject != null) {
            String timeFrame = (String) dbObject.get("timeFrame");

            temp.setTimeFrame(TimeFrame.safeValueOf(timeFrame, TimeFrame.MILLISECOND));
            temp.setAttributeName((String) dbObject.get("attributeName"));
        }

        aggregationConfig = temp;
    }

    public void loadDisplay() {
        DBObject dbObject = findConfigItem("display");

        if (dbObject != null) {
            displayGroup = (String) dbObject.get("displayGroup");
            displayName = (String) dbObject.get("displayName");
        }
    }

    public void loadTransformConfig() {
        DBObject dbObject = findConfigItem("transform");

        transformConfig = TransformConfig.fromDBObject(dbObject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Source source = (Source) o;

        if (name != null ? !name.equals(source.name) : source.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Source{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", displayGroup='" + displayGroup + '\'' +
                ", aggregationConfig=" + aggregationConfig +
                ", transformConfig=" + transformConfig +
                '}';
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

    public String getReportsCollectionName() {
        return qualifiedReportsName(name);
    }

    public String getMailReportsCollectionName() {
        return mailReports.getName();
    }

    public CommandResult getStats() {
        return events.getStats();
    }

    public AggregationConfig getAggregationConfig() {
        return aggregationConfig;
    }

    public TransformConfig getTransformConfig() {
        return transformConfig;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayGroup() {
        return displayGroup;
    }
}


