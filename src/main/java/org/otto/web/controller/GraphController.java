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

package org.otto.web.controller;

import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.otto.event.DBSource;
import org.otto.event.Sources;
import org.otto.graph.Graph;
import org.otto.web.form.GraphForm;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
@Controller
public class GraphController {

    @Inject
    private Sources sources;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, "start", new CustomDateEditor(dateFormat, false));
        binder.registerCustomEditor(Date.class, "end", new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping({"/sources/{name}/graph"})
    public String graph(@PathVariable String name, GraphForm form, BindingResult result, Model model) {
        model.addAttribute("navItem", "graph");
        model.addAttribute("form", form);
        model.addAttribute("graph", buildGraph(name, form).toGoogleHtml(1080, 750));

        return "sources/graph";
    }

    @RequestMapping({"/sources/{name}/graph.csv"})
    public void csv(@PathVariable String name, GraphForm form, HttpServletResponse response) throws IOException {
        response.setContentType("application/csv");

        response.getWriter().write(buildGraph(name, form).toCsv());
    }

    @RequestMapping({"/sources/{name}/graph/table"})
    public String table(@PathVariable String name, GraphForm form, Model model) throws IOException {
        model.addAttribute("table", buildGraph(name, form).toHtmlTable());

        return "sources/graph_table";
    }

    private Graph buildGraph(String name, GraphForm form) {
        DateMidnight start = new DateMidnight(form.getStart());
        DateMidnight end = new DateMidnight(form.getEnd());

        Interval interval = new Interval(start, end);

        Graph graph = new Graph();

        graph.setRows(interval, Duration.standardMinutes(form.getStepInMinutes()));

        DBSource source = sources.getSource(name);

        Iterator<DBObject> events = source.findEvents(interval);

        while (events.hasNext()) {
            DBObject event = events.next();

            String columnName = name;

            if (StringUtils.isNotEmpty(form.getSplitColumn())) {
                columnName = event.get(form.getSplitColumn()).toString();
            }

            graph.ensureColumnsExists(columnName);

            Date date = (Date) event.get("date");

            if (StringUtils.isEmpty(form.getSumColumn())) {
                graph.increaseValue(columnName, new DateTime(date));
            } else {
                Integer value = (Integer) event.get(form.getSumColumn());

                graph.increaseValue(columnName, new DateTime(date), value);
            }
        }

        return graph;
    }
}
