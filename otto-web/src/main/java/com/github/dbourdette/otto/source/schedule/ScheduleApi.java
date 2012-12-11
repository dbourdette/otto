package com.github.dbourdette.otto.source.schedule;

import java.util.List;

import com.github.dbourdette.otto.data.DataTable;
import com.github.dbourdette.otto.data.DataTablePeriod;
import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.source.reports.ReportConfig;
import com.github.dbourdette.otto.source.reports.ReportConfigs;

public class ScheduleApi {
    public List<Source> getSources() {
        return Source.findAll();
    }

    public List<ReportConfig> getReports(String sourceName) {
        Source source = Source.findByName(sourceName);

        return ReportConfigs.forSource(source).getReportConfigs();
    }

    public DataTable getData(String sourceName, String reportTitle, String period) {
        Source source = Source.findByName(sourceName);
        ReportConfig reportConfig = ReportConfigs.forSource(source).getReportConfigByTitle(reportTitle);

        return source.buildTable(reportConfig, DataTablePeriod.valueOf(period));
    }
}
