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
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.dbourdette.otto.report.Report;
import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.web.form.ReportForm;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class ReportController {

    private static final int TOP_COUNT = 30;

    @RequestMapping({"/sources/{name}/reports/stats"})
    public String stats(@PathVariable String name, @Valid ReportForm form, BindingResult result, Model model, HttpServletRequest request) {
        Source source = Source.findByName(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(source.getReportConfigs());

        model.addAttribute("source", source);
        model.addAttribute("navItem", "reports");
        model.addAttribute("subNavItem", "stats");
        model.addAttribute("frequency", source.findEventsFrequency(form.getInterval()));
        model.addAttribute("form", form);
        model.addAttribute("values", form.buildReport(source).getSums());

        return "sources/reports/stats";
    }

    @RequestMapping({"/sources/{name}", "/sources/{name}/reports", "/sources/{name}/reports/graph"})
    public String graph(@PathVariable String name, @Valid ReportForm form, BindingResult result, Model model, HttpServletRequest request) {
        Source source = Source.findByName(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(source.getReportConfigs());

        model.addAttribute("source", source);
        model.addAttribute("navItem", "reports");
        model.addAttribute("subNavItem", "graph");
        model.addAttribute("form", form);

        Long t1 = System.currentTimeMillis();

        Report report = form.buildReport(source);
        report.top(TOP_COUNT);
        report.sortBySum();

        Long t2 = System.currentTimeMillis();

        String html = report.toGoogleChartHtml(1080, 750);

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
        form.setReportConfigs(source.getReportConfigs());

        model.addAttribute("source", source);
        model.addAttribute("navItem", "reports");
        model.addAttribute("subNavItem", "pie");
        model.addAttribute("form", form);

        Long t1 = System.currentTimeMillis();

        Report report = form.buildReport(source);
        report.top(TOP_COUNT);
        report.sortBySum();

        Long t2 = System.currentTimeMillis();

        String html = report.toGooglePieHtml(1080, 750);

        Long t3 = System.currentTimeMillis();

        List<String> times = new ArrayList<String>();
        times.add("Gathered report data in " + (t2 - t1) + "ms");
        times.add("Build html " + (t3 - t2) + "ms");

        model.addAttribute("times", times);
        model.addAttribute("html", html);

        return "sources/reports/pie";
    }

    @RequestMapping({"/sources/{name}/reports/graph.png"})
    public void png(@PathVariable String name, ReportForm form, HttpServletRequest request, final HttpServletResponse response) throws IOException {
        Source source = Source.findByName(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(source.getReportConfigs());

        Report report = form.buildReport(source);
        report.top(TOP_COUNT);
        report.sortBySum();

        response.setContentType("image/png");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : report.toGoogleImageParams(750, 400).entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");

        HttpPost post = new HttpPost("https://chart.googleapis.com/chart");
        post.setEntity(entity);

        HttpClient httpclient = new DefaultHttpClient();

        httpclient.execute(post, new ResponseHandler<Void>() {
            @Override
            public Void handleResponse(HttpResponse httpResponse) throws IOException {
                HttpEntity entity = httpResponse.getEntity();

                IOUtils.copy(entity.getContent(), response.getOutputStream());

                return null;
            }
        });
    }

    @RequestMapping({"/sources/{name}/reports/csv"})
    public void csv(@PathVariable String name, ReportForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/csv");

        Source source = Source.findByName(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(source.getReportConfigs());

        Report report = form.buildReport(source);
        report.top(TOP_COUNT);
        report.sortBySum();

        response.getWriter().write(report.toCsv());
    }

    @RequestMapping({"/sources/{name}/reports/table"})
    public String table(@PathVariable String name, ReportForm form, HttpServletRequest request, Model model) throws IOException {
        Source source = Source.findByName(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(source.getReportConfigs());

        model.addAttribute("table", form.buildReport(source).toHtmlTable());

        return "sources/reports/table";
    }
}
