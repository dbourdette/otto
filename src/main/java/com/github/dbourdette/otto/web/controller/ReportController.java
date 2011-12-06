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

import javax.inject.Inject;
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

import com.github.dbourdette.otto.graph.Graph;
import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.Sources;
import com.github.dbourdette.otto.web.form.ReportForm;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class ReportController {

    private static final int TOP_COUNT = 30;

    @Inject
    private Sources sources;

    @RequestMapping({"/sources/{name}/reports/stats"})
    public String stats(@PathVariable String name, @Valid ReportForm form, BindingResult result, Model model, HttpServletRequest request) {
        DBSource source = sources.getSource(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(source.getReportConfigs());

        model.addAttribute("navItem", "reports");
        model.addAttribute("subNavItem", "stats");
        model.addAttribute("frequency", source.findEventsFrequency(form.getInterval()));
        model.addAttribute("form", form);
        model.addAttribute("values", form.getValues(source));

        return "sources/reports/stats";
    }

    @RequestMapping({"/sources/{name}/reports", "/sources/{name}/reports/graph"})
    public String graph(@PathVariable String name, @Valid ReportForm form, BindingResult result, Model model, HttpServletRequest request) {
        DBSource source = sources.getSource(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(source.getReportConfigs());

        model.addAttribute("navItem", "reports");
        model.addAttribute("subNavItem", "graph");
        model.addAttribute("form", form);

        Long t1 = System.currentTimeMillis();

        Graph graph = form.buildGraph(source);
        graph.top(TOP_COUNT);
        graph.sortBySum();

        Long t2 = System.currentTimeMillis();

        String html = graph.toGoogleHtml(1080, 750);

        Long t3 = System.currentTimeMillis();

        List<String> times = new ArrayList<String>();
        times.add("Gathered graph data in " + (t2 - t1) + "ms");
        times.add("Build html " + (t3 - t2) + "ms");

        model.addAttribute("times", times);
        model.addAttribute("graph", html);

        return "sources/reports/graph";
    }

    @RequestMapping({"/sources/{name}/reports/graph.png"})
    public void png(@PathVariable String name, ReportForm form, HttpServletRequest request, final HttpServletResponse response) throws IOException {
        DBSource source = sources.getSource(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(source.getReportConfigs());

        Graph graph = form.buildGraph(source);
        graph.top(TOP_COUNT);
        graph.sortBySum();

        response.setContentType("image/png");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : graph.toGoogleImageParams(750, 400).entrySet()) {
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

        DBSource source = sources.getSource(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(source.getReportConfigs());

        Graph graph = form.buildGraph(source);
        graph.top(TOP_COUNT);
        graph.sortBySum();

        response.getWriter().write(graph.toCsv());
    }

    @RequestMapping({"/sources/{name}/reports/table"})
    public String table(@PathVariable String name, ReportForm form, HttpServletRequest request, Model model) throws IOException {
        DBSource source = sources.getSource(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(source.getReportConfigs());

        model.addAttribute("table", form.buildGraph(source).toHtmlTable());

        return "sources/reports/table";
    }
}
