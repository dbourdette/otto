package com.github.dbourdette.otto.source.reports;

import java.util.List;

import org.bson.types.ObjectId;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.source.Source;
import com.google.code.morphia.query.Query;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ReportConfigs {
    private String sourceName;

    private ReportConfigs() {}

    public static ReportConfigs forSource(Source source) {
        ReportConfigs reports = new ReportConfigs();

        reports.sourceName = source.getName();

        return reports;
    }

    public List<ReportConfig> getReportConfigs() {
        return query().order("name").asList();
    }

    public ReportConfig getReportConfig(String id) {
        return getReportConfig(new ObjectId(id));
    }

    public ReportConfig getReportConfig(ObjectId id) {
        return query().filter("_id", id).get();
    }

    public ReportConfig getReportConfigByTitle(String title) {
        return query().filter("title", title).get();
    }

    private Query<ReportConfig> query() {
        return Registry.datastore.find(ReportConfig.class).filter("sourceName", sourceName);
    }
}
