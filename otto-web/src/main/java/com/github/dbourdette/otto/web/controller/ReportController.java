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

package com.github.dbourdette.otto.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.dbourdette.otto.data.DataTableCsvRenderer;
import com.github.dbourdette.otto.data.DataTableGoogleChartRenderer;
import com.github.dbourdette.otto.data.DataTableGooglePieRenderer;
import com.github.dbourdette.otto.data.SimpleDataTable;
import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.source.reports.SourceReports;
import com.github.dbourdette.otto.web.editor.DatePropertyEditor;
import com.github.dbourdette.otto.web.form.ReportForm;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class ReportController {

    private static final int TOP_COUNT = 30;

    @InitBinder
    public void binder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DatePropertyEditor());
    }

    @RequestMapping({"/sources/{name}/reports/stats"})
    public String stats(@PathVariable String name, @Valid ReportForm form, BindingResult result, Model model, HttpServletRequest request) {
        Source source = Source.findByName(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(SourceReports.forSource(source).getReportConfigs());

        model.addAttribute("source", source);
        model.addAttribute("subNavItem", "stats");
        model.addAttribute("frequency", source.findEventsFrequency(form.getInterval()));
        model.addAttribute("form", form);
        model.addAttribute("values", form.buildTable(source).getSums());

        return "sources/reports/stats";
    }

    @RequestMapping({"/sources/{name}", "/sources/{name}/reports", "/sources/{name}/reports/graph"})
    public String graph(@PathVariable String name, @Valid ReportForm form, BindingResult result, Model model, HttpServletRequest request) {
        Source source = Source.findByName(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(SourceReports.forSource(source).getReportConfigs());

        model.addAttribute("source", source);
        model.addAttribute("subNavItem", "graph");
        model.addAttribute("form", form);

        Long t1 = System.currentTimeMillis();

        SimpleDataTable table = form.buildTable(source);
        table.top(TOP_COUNT);
        table.sortBySum();

        Long t2 = System.currentTimeMillis();

        DataTableGoogleChartRenderer renderer = new DataTableGoogleChartRenderer();

        String html = renderer.render(table);

        Long t3 = System.currentTimeMillis();

        List<String> times = new ArrayList<String>();
        times.add("Gathered report data in " + (t2 - t1) + "ms");
        times.add("Build html " + (t3 - t2) + "ms");

        model.addAttribute("times", times);
        model.addAttribute("html", html);

        return "sources/reports/graph";
    }

    @RequestMapping({"/sources/{name}/reports/pie"})
    public String pie(@PathVariable String name, @Valid ReportForm form, BindingResult result, Model model, HttpServletRequest request) {
        Source source = Source.findByName(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(SourceReports.forSource(source).getReportConfigs());

        model.addAttribute("source", source);
        model.addAttribute("subNavItem", "pie");
        model.addAttribute("form", form);

        Long t1 = System.currentTimeMillis();

        SimpleDataTable table = form.buildTable(source);
        table.top(TOP_COUNT);
        table.sortBySum();

        Long t2 = System.currentTimeMillis();

        DataTableGooglePieRenderer renderer = new DataTableGooglePieRenderer();

        String html = renderer.render(table);

        Long t3 = System.currentTimeMillis();

        List<String> times = new ArrayList<String>();
        times.add("Gathered report data in " + (t2 - t1) + "ms");
        times.add("Build html " + (t3 - t2) + "ms");

        model.addAttribute("times", times);
        model.addAttribute("html", html);

        return "sources/reports/pie";
    }

    @RequestMapping({"/sources/{name}/reports/csv"})
    public void csv(@PathVariable String name, ReportForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/csv");

        Source source = Source.findByName(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(SourceReports.forSource(source).getReportConfigs());

        SimpleDataTable table = form.buildTable(source);
        table.top(TOP_COUNT);
        table.sortBySum();

        DataTableCsvRenderer renderer = new DataTableCsvRenderer();

        response.getWriter().write(renderer.render(table));
    }
}
