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

package com.github.dbourdette.otto.web.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.joda.time.Interval;

import com.github.dbourdette.otto.graph.Graph;
import com.github.dbourdette.otto.graph.ReportPeriod;
import com.github.dbourdette.otto.graph.filler.FillerChain;
import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.config.DefaultGraphParameters;
import com.github.dbourdette.otto.source.config.ReportConfig;
import com.github.dbourdette.otto.web.util.Pair;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ReportForm {
    @NotNull
    private ReportPeriod period;

    private String reportId;

    private List<ReportConfig> reportConfigs;

    public ReportForm() {
        period = ReportPeriod.RECENT;
    }

    public Interval getInterval() {
        return period.getInterval();
    }

    public void fillWithDefault(DefaultGraphParameters defaultParameters, HttpServletRequest request) {
        Map<String, String> map = request.getParameterMap();

        if (!map.containsKey("period") && defaultParameters.getPeriod() != null) {
            setPeriod(defaultParameters.getPeriod());
        }
    }

    public void setReportConfigs(List<ReportConfig> reportConfigs) {
        this.reportConfigs = reportConfigs;
    }

    public List<Pair> getValues(DBSource source) {
        Graph graph = buildGraph(source);

        List<Pair> values = new ArrayList<Pair>();

        for (String column : graph.getColumnTitles()) {
            values.add(new Pair(column, graph.getSum(column)));
        }

        return values;
    }

    public Graph buildGraph(DBSource source) {
        Graph graph = new Graph();

        period.createRows(graph);

        ReportConfig reportConfig = getReportConfig();

        FillerChain chain = reportConfig.buildChain(graph);

        Iterator<DBObject> events = source.findEvents(getInterval());

        while (events.hasNext()) {
            DBObject event = events.next();

            chain.write(event);
        }

        if (graph.getColumnCount() == 0) {
            graph.ensureColumnExists("no data");
        }

        if (reportConfig.getSort() == Sort.ALPHABETICALLY) {
            graph.sortAlphabetically();
        } else if (reportConfig.getSort() == Sort.BY_SUM) {
            graph.sortBySum();
        }

        return graph;
    }

    public ReportPeriod[] getPeriods() {
        return ReportPeriod.values();
    }

    public ReportPeriod getPeriod() {
        return period;
    }

    public void setPeriod(ReportPeriod period) {
        this.period = period;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public List<ReportConfig> getReportConfigs() {
        return reportConfigs;
    }

    public ReportConfig getReportConfig() {
        for (ReportConfig reportConfig : reportConfigs) {
            if (reportConfig.getId().equals(reportId)) {
                return reportConfig;
            }
        }

        if (reportConfigs.size() > 0) {
            ReportConfig reportConfig = reportConfigs.get(0);
            reportId = reportConfig.getId();

            return reportConfig;
        }

        return new ReportConfig();
    }
}
