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

package com.github.dbourdette.otto.web.controller.sources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
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

import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.source.reports.ReportConfigs;
import com.github.dbourdette.otto.source.reports.ReportFormat;
import com.github.dbourdette.otto.source.reports.ReportFormats;
import com.github.dbourdette.otto.web.editor.DatePropertyEditor;
import com.github.dbourdette.otto.web.form.ReportForm;

@Controller
public class ReportsController {
    @Inject
    private ReportFormats reportFormats;

    @InitBinder
    public void binder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DatePropertyEditor());
    }

    @RequestMapping({"/sources/{name}", "/sources/{name}/reports"})
    public String index(@PathVariable String name) {
        Source source = Source.findByName(name);

        ReportFormat reportFormat = findAllFormats(source).get(0);

        return "redirect:/sources/{name}/reports/" + reportFormat.getName();
    }

    @RequestMapping({"/sources/{name}/reports/{format}"})
    public String format(@PathVariable String name, @PathVariable String format, @Valid ReportForm form, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Source source = Source.findByName(name);
        ReportFormat reportFormat = reportFormats.findByName(format);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);
        form.setReportConfigs(ReportConfigs.forSource(source).getReportConfigs());

        if (reportFormat.isDownloadAs()) {
            response.setContentType(reportFormat.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename(source, reportFormat) + "\"");

            response.getWriter().write(reportFormat.render(form.buildTable(source)));

            return null;
        } else {
            model.addAttribute("source", source);
            model.addAttribute("form", form);
            model.addAttribute("currentFormat", format);
            model.addAttribute("formats", findAllFormats(source));
            model.addAttribute("html", reportFormat.render(form.buildTable(source)));

            return "sources/report";
        }
    }

    private List<ReportFormat> findAllFormats(Source source) {
        List<ReportFormat> allFormats = new ArrayList<ReportFormat>();

        allFormats.addAll(reportFormats.findBySource(source));
        allFormats.addAll(reportFormats.findForAllSources());

        return allFormats;
    }

    private String filename(Source source, ReportFormat reportFormat) {
        return source.getName() + "." + reportFormat.getExtension();
    }
}
