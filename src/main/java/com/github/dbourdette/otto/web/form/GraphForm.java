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

import org.apache.commons.lang.StringUtils;
import org.joda.time.Interval;

import com.github.dbourdette.otto.graph.Graph;
import com.github.dbourdette.otto.graph.GraphPeriod;
import com.github.dbourdette.otto.graph.filler.FillerChain;
import com.github.dbourdette.otto.graph.filler.SplitFiller;
import com.github.dbourdette.otto.graph.filler.SumFiller;
import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.config.DefaultGraphParameters;
import com.github.dbourdette.otto.web.util.Pair;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class GraphForm {
    @NotNull
    private GraphPeriod period;

    public String sumColumn;

    public String splitColumn;

    public Sort sort;

    public GraphForm() {
        period = GraphPeriod.RECENT;
    }

    public Interval getInterval() {
        return period.getInterval();
    }

    public void fillWithDefault(DefaultGraphParameters defaultParameters, HttpServletRequest request) {
        Map<String, String> map = request.getParameterMap();

        if (!map.containsKey("period") && defaultParameters.getPeriod() != null) {
            setPeriod(defaultParameters.getPeriod());
        }

        if (!map.containsKey("splitColumn")) {
            setSplitColumn(defaultParameters.getSplitColumn());
        }

        if (!map.containsKey("sumColumn")) {
            setSumColumn(defaultParameters.getSumColumn());
        }
    }

    public List<Pair> getValues(DBSource source) {
        Graph graph = buildGraph(source);

        List<Pair> values = new ArrayList<Pair>();

        for (String column : graph.getColumnTitles()) {
            values.add(new Pair(column, graph.getSum(column)));
        }

        return values;
    }

    public List<Pair> getCounts(DBSource source) {
        String temp = sumColumn;

        sumColumn = null;

        List<Pair> pairs = getValues(source);

        sumColumn = temp;

        return pairs;
    }

    public Graph buildGraph(DBSource source) {
        Graph graph = new Graph();

        period.createRows(graph);

        FillerChain chain = buildChain(graph);

        Iterator<DBObject> events = source.findEvents(getInterval());

        while (events.hasNext()) {
            DBObject event = events.next();

            chain.write(event);
        }

        if (graph.getColumnCount() == 0) {
            graph.ensureColumnExists("no data");
        }

        if (sort == Sort.ALPHABETICALLY) {
            graph.sortAlphabetically();
        } else if (sort == Sort.BY_SUM) {
            graph.sortBySum();
        }

        return graph;
    }

    public String getSumColumn() {
        return sumColumn;
    }

    public void setSumColumn(String sumColumn) {
        this.sumColumn = sumColumn;
    }

    public String getSplitColumn() {
        return splitColumn;
    }

    public void setSplitColumn(String splitColumn) {
        this.splitColumn = splitColumn;
    }

    public GraphPeriod[] getPeriods() {
        return GraphPeriod.values();
    }

    public GraphPeriod getPeriod() {
        return period;
    }

    public void setPeriod(GraphPeriod period) {
        this.period = period;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Sort[] getSorts() {
        return Sort.values();
    }

    private FillerChain buildChain(Graph graph) {
        FillerChain chain = FillerChain.forGraph(graph);

        if (StringUtils.isNotEmpty(splitColumn)) {
            SplitFiller split = new SplitFiller();
            split.setColumns(splitColumn);
            chain.add(split);
        }

        if (StringUtils.isNotEmpty(splitColumn)) {
            SumFiller sum = new SumFiller();
            sum.setColumn(sumColumn);
            chain.add(sum);
        }

        return chain;
    }

    @Override
    public String toString() {
        return "GraphForm{" +
                "period=" + period +
                ", sumColumn='" + sumColumn + '\'' +
                ", splitColumn='" + splitColumn + '\'' +
                ", sort=" + sort +
                '}';
    }
}
