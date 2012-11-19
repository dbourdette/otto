package com.github.dbourdette.otto.source.schedule;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.data.DataTablePeriod;
import com.github.dbourdette.otto.data.SimpleDataTable;
import com.github.dbourdette.otto.service.mail.Mail;
import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.source.reports.ReportConfig;
import com.github.dbourdette.otto.source.reports.SourceReports;
import com.github.dbourdette.otto.web.util.Constants;
import com.github.dbourdette.otto.web.util.Pair;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Entity(value = Constants.SOURCES_ROOT + "schedules", noClassnameStored = true)
public class MailSchedule {
    @Id
    private ObjectId id;

    @Property
    private String sourceName;

    @NotNull
    @Property
    private String report;

    @NotNull
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

    public MailSchedule() {
    }

    public MailSchedule(String sourceName) {
        this.sourceName = sourceName;
    }

    public void save() {
        Registry.datastore.save(this);
    }

    public void delete() {
        Registry.datastore.delete(this);
    }

    public Mail buildMail(Source source) {
        Mail mail = new Mail();

        mail.setTo(to);
        mail.setSubject(title + " sent at " + new Date() + " for period " + period);
        mail.setHtml(buildHtml(source));

        return mail;
    }

    public String buildHtml(Source source) {
        String html = "";

        ReportConfig config = SourceReports.forSource(source).getReportConfigByTitle(report);

        if (config == null) {
            config = new ReportConfig();
        }

        SimpleDataTable report = source.buildTable(config, period);

        html += "<div style=\"font-family: Helvetica Neue, Helvetica, Arial, sans-serif;\">";

        html += "<h1 style=\"font-size: 24px;line-height: 36px;font-weight: bold;display: block;\">" + config.getTitle() + "</h1>";

        html += "<table style=\"font-size: 13px;line-height:18px;border: 1px solid #DDD;border-collapse: separate;border-radius: 4px;width: 100%;margin-bottom: 18px;border-spacing: 0;\">";

        int count = 0;

        for (Pair pair : report.getSums()) {
            count += pair.getValue();
        }

        html += "<thead style=\"font-weight:bold\"><tr>";
        html += "<td style=\"border-top: 0;border-left: 1px solid #DDD;padding: 4px;line-height: 18px;text-align: left;vertical-align: top;\">total count</td>";
        html += "<td style=\"border-top: 0;border-left: 1px solid #DDD;padding: 4px;line-height: 18px;text-align: left;vertical-align: top;\">";
        html += count;
        html += "</td>";
        html += "</tr></thead>";

        for (Pair pair : report.getSums()) {
            html += "<tbody><tr>";
            html += "<td style=\"border-radius: 0 0 0 4px;border-left: 1px solid #DDD;padding: 4px;line-height: 18px;text-align: left;vertical-align: top;border-top: 1px solid #DDD;\">";
            html += pair.getName();
            html += "</td>";
            html += "<td style=\"border-radius: 0 0 4px 0;border-left: 1px solid #DDD;padding: 4px;line-height: 18px;text-align: left;vertical-align: top;border-top: 1px solid #DDD;\">";
            html += pair.getValue();
            html += "</td>";
            html += "</tr></tbody>";
        }

        html += "</table>";

        html += "</div>";

        return html;
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
}
