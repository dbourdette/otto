package com.github.dbourdette.otto.source.schedule;

import java.util.ArrayList;
import java.util.List;

import com.github.dbourdette.otto.data.DataTable;
import com.github.dbourdette.otto.data.DataTablePeriod;
import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.source.reports.ReportConfig;
import com.github.dbourdette.otto.source.reports.ReportConfigs;

public class ScheduleApi {
    public List<String> getSources() {
        List<String> result = new ArrayList<String>();

        for (Source source : Source.findAll()) {
            result.add(source.getName());
        }

        return result;
    }

    public List<String> getReports(String source) {
        Source dbSource = Source.findByName(source);

        List<String> result = new ArrayList<String>();

        for (ReportConfig reportConfig : ReportConfigs.forSource(dbSource).getReportConfigs()) {
            result.add(reportConfig.getTitle());
        }

        return result;
    }

    public DataTable getData(String source, String report, String period) {
        Source dbSource = Source.findByName(source);
        ReportConfig dbReport = ReportConfigs.forSource(dbSource).getReportConfigByTitle(report);

        return dbSource.buildTable(dbReport, DataTablePeriod.valueOf(period));
    }
}
