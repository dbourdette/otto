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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.dbourdette.otto.data.DataTablePeriod;
import com.github.dbourdette.otto.service.mail.Mail;
import com.github.dbourdette.otto.source.OldSource;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

/**
 * Send mail schedule operation for a {@link com.github.dbourdette.otto.source.OldSource}
 *
 * @author damien bourdette
 */
public class OldMailSchedule {
    private String id;

    @NotNull
    private String report;

    @NotNull
    private DataTablePeriod period;

    private String cronExpression;

    @NotEmpty
    private String to;

    @NotEmpty
    private String title;

    public static List<OldMailSchedule> readAll(DBCursor cursor) {
        List<OldMailSchedule> result = new ArrayList<OldMailSchedule>();

        while (cursor.hasNext()) {
            result.add(read((BasicDBObject) cursor.next()));
        }

        return result;
    }

    public static OldMailSchedule read(BasicDBObject object) {
        OldMailSchedule schedule = new OldMailSchedule();

        schedule.setId(((ObjectId) object.get("_id")).toString());
        schedule.setReport(object.getString("report"));
        schedule.setPeriod(DataTablePeriod.valueOf(object.getString("period")));
        schedule.setTitle(object.getString("title"));
        schedule.setTo(object.getString("to"));
        schedule.setCronExpression(object.getString("cronExpression"));

        return schedule;
    }

    public BasicDBObject toDBObject() {
        BasicDBObject object = new BasicDBObject();

        if (StringUtils.isNotEmpty(id)) {
            object.put("_id", new ObjectId(id));
        }

        object.put("report", report);
        object.put("period", period.name());
        object.put("title", title);
        object.put("to", to);
        object.put("cronExpression", cronExpression);

        return object;
    }

    public Mail buildMail(OldSource source) {
        Mail mail = new Mail();

        mail.setTo(to);
        mail.setSubject(title + " sent at " + new Date() + " for period " + period);
        mail.setHtml("empty");

        return mail;
    }

    public DataTablePeriod[] getPeriods() {
        return DataTablePeriod.values();
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DataTablePeriod getPeriod() {
        return period;
    }

    public void setPeriod(DataTablePeriod period) {
        this.period = period;
    }


}
