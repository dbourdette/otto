package com.github.dbourdette.otto.source.schedule;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.dbourdette.otto.data.DataTablePeriod;
import com.github.dbourdette.otto.data.SimpleDataTable;
import com.github.dbourdette.otto.service.mail.Mail;
import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.source.reports.ReportConfig;
import com.github.dbourdette.otto.source.reports.ReportConfigs;
import com.github.dbourdette.otto.web.util.MongoCollections;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Entity(value = MongoCollections.SOURCES_SCHEDULES, noClassnameStored = true)
public class MailSchedule {
    @Id
    private ObjectId id;

    @Property
    private String sourceName;

    @Property
    private String report;

    @Property
    private DataTablePeriod period;

    @Property
    private String cronExpression;

    @NotEmpty
    @Property
    private String to;

    @NotEmpty
    @Property
    private String title;

    @Property
    private String groovyTemplate;

    public MailSchedule() {
    }

    public MailSchedule(String sourceName) {
        this.sourceName = sourceName;
    }

    public Mail buildMail() {
        Mail mail = new Mail();

        mail.setTo(to);
        mail.setSubject(buildMailSubject());
        mail.setHtml(buildHtml());

        return mail;
    }

    public boolean isGlobal() {
        return Source.ALL_SOURCES.equals(sourceName);
    }

    public String buildHtml() {
        if (isGlobal()) {
            return buildHtmlForGlobalSchedule();
        } else {
            return buildHtmlForSpecificSource();
        }
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public DataTablePeriod[] getPeriods() {
        return DataTablePeriod.values();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public DataTablePeriod getPeriod() {
        return period;
    }

    public void setPeriod(DataTablePeriod period) {
        this.period = period;
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

    public String getGroovyTemplate() {
        return groovyTemplate;
    }

    public void setGroovyTemplate(String groovyTemplate) {
        this.groovyTemplate = groovyTemplate;
    }

    private String buildMailSubject() {
        if (isGlobal()) {
            return title + " sent at " + new Date();
        } else {
            return title + " sent at " + new Date() + " for period " + period;
        }
    }

    public String buildHtmlForGlobalSchedule() {
        Map<String, Object> bindings = new HashMap<String, Object>();

        bindings.put("title", title);
        bindings.put("api", new ScheduleApi());

        return render(bindings, "global.template");
    }

    public String buildHtmlForSpecificSource() {
        Source source = Source.findByName(sourceName);

        ReportConfig config = ReportConfigs.forSource(source).getReportConfigByTitle(report);

        if (config == null) {
            config = new ReportConfig();
        }

        SimpleDataTable data = source.buildTable(config, period);

        Map<String, Object> bindings = new HashMap<String, Object>();

        bindings.put("title", title);
        bindings.put("data", data);

        return render(bindings, "default.template");
    }

    public String render(Map bindings, String defaultTemplate) {
        try {
            return buildTemplate(defaultTemplate).make(bindings).toString();
        } catch (Exception e) {
            return ExceptionUtils.getFullStackTrace(e);
        }
    }

    public Template buildTemplate(String defaultTemplate) throws IOException, ClassNotFoundException {
        SimpleTemplateEngine engine = new SimpleTemplateEngine();

        if (StringUtils.isBlank(groovyTemplate)) {
            return engine.createTemplate(getClass().getResource(defaultTemplate));
        } else {
            return engine.createTemplate(groovyTemplate);
        }
    }
}
