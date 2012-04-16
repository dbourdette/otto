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

import com.github.dbourdette.otto.report.ReportPeriod;
import com.github.dbourdette.otto.source.*;
import com.github.dbourdette.otto.source.config.*;
import com.github.dbourdette.otto.source.reports.ReportConfig;
import com.github.dbourdette.otto.source.reports.SourceReports;
import com.github.dbourdette.otto.source.schedule.MailSchedule;
import com.github.dbourdette.otto.source.schedule.SourceScheduleExecutor;
import com.github.dbourdette.otto.source.schedule.SourceSchedules;
import com.github.dbourdette.otto.web.form.CappingForm;
import com.github.dbourdette.otto.web.form.IndexForm;
import com.github.dbourdette.otto.web.form.SourceForm;
import com.github.dbourdette.otto.web.form.TransformForm;
import com.github.dbourdette.otto.web.util.FlashScope;
import com.github.dbourdette.otto.web.util.IntervalUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class SourcesController {

    @Inject
    private FlashScope flashScope;

    @Inject
    private MailReports mailReports;

    @Inject
    private SourceScheduleExecutor scheduleExecutor;

    @RequestMapping({"/sources/{name}/configuration"})
    public String source(@PathVariable String name, Model model) {
        Source source = Source.findByName(name);

        model.addAttribute("source", source);
        model.addAttribute("aggregation", source.getAggregationConfig());
        model.addAttribute("transform", source.getTransformConfig());
        model.addAttribute("defaultGraphParameters", source.getDefaultGraphParameters());
        model.addAttribute("reports", SourceReports.forSource(source).getReportConfigs());
        model.addAttribute("schedules", SourceSchedules.forSource(Source.findByName(name)).getSchedules());
        model.addAttribute("mailReports", source.getMailReports());
        model.addAttribute("indexes", source.getIndexes());



        return "sources/source";
    }

    @RequestMapping({"/sources/{name}/statistics"})
    public String statistics(@PathVariable String name, Model model) {
        Source source = Source.findByName(name);

        model.addAttribute("source", source);
        model.addAttribute("lastWeekFrequency", source.findEventsFrequency(IntervalUtils.lastWeek()));
        model.addAttribute("yesterdayFrequency", source.findEventsFrequency(IntervalUtils.yesterday()));
        model.addAttribute("todayFrequency", source.findEventsFrequency(IntervalUtils.today()));

        return "sources/statistics";
    }

    @RequestMapping("/sources/form")
    public String form(Model model) {
        model.addAttribute("form", new SourceForm());

        return "sources/source_form";
    }

    @RequestMapping(value = "/sources", method = RequestMethod.POST)
    public String createSource(@Valid @ModelAttribute("form") SourceForm form, BindingResult result) {
        if (StringUtils.isNotEmpty(form.getSize())) {
            try {
                form.getSizeInBytes();
            } catch (Exception e) {
                result.rejectValue("size", "size.invalidPattern");
            }
        }

        if (Source.exists(form.getName())) {
            result.rejectValue("name", "source.alreadyExists");
        }

        if (result.hasErrors()) {
            return "sources/source_form";
        }

        Source.create(form);

        flashScope.message("source " + form.getName() + " has just been created");

        return "redirect:/sources/" + form.getName() + "/configuration";
    }

    @RequestMapping("/sources/{name}/edit")
    public String form(@PathVariable String name, Model model) {
        model.addAttribute("form", Source.findByName(name));

        return "sources/source_edit_form";
    }

    @RequestMapping(value = "/sources/edit", method = RequestMethod.POST)
    public String form(@Valid @ModelAttribute("form") SourceForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "sources/source_edit_form";
        }

        Source source = Source.findByName(form.getName());

        source.updateDisplayGroupAndName(form.getDisplayGroup(), form.getDisplayName());

        return "redirect:/sources/" + form.getName() + "/configuration";
    }

    @RequestMapping("/sources/{name}/delete")
    public String dropSourceForm(@PathVariable String name, Model model) {
        return "sources/source_delete_form";
    }

    @RequestMapping(value = "/sources/{name}", method = RequestMethod.DELETE)
    public String dropSource(@PathVariable String name) throws SchedulerException {
        Source.findByName(name).drop();

        flashScope.message("source " + name + " has just been deleted");

        return "redirect:/sources";
    }

    @RequestMapping("/sources/{name}/aggregation/form")
    public String aggregation(@PathVariable String name, Model model) {
        model.addAttribute("form", Source.findByName(name).getAggregationConfig());
        model.addAttribute("timeFrames", TimeFrame.values());

        return "sources/aggregation_form";
    }

    @RequestMapping(value = "/sources/{name}/aggregation", method = RequestMethod.POST)
    public String saveAggregation(@PathVariable String name, @Valid @ModelAttribute("form") AggregationConfig form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("timeFrames", TimeFrame.values());

            return "sources/aggregation_form";
        }

        Source.findByName(name).saveAggregation(form);

        flashScope.message("Aggregation has been modified for source " + name);

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/default-graph-params/form")
    public String defaultGraphParameters(@PathVariable String name, Model model) {
        model.addAttribute("form", Source.findByName(name).getDefaultGraphParameters());
        model.addAttribute("periods", ReportPeriod.values());

        return "sources/default_graph_params_form";
    }

    @RequestMapping(value = "/sources/{name}/default-graph-params", method = RequestMethod.POST)
    public String saveDefaultGraphParameters(@PathVariable String name, @Valid @ModelAttribute("form") DefaultGraphParameters form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("periods", ReportPeriod.values());

            return "sources/default_graph_params_form";
        }

        flashScope.message("Default graph parameters have been set for source " + name);

        Source.findByName(name).setDefaultGraphParameters(form);

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/capping/form")
    public String capping(@PathVariable String name, Model model) {
        model.addAttribute("form", new CappingForm());

        return "sources/capping_form";
    }

    @RequestMapping(value = "/sources/{name}/capping", method = RequestMethod.POST)
    public String saveCapping(@PathVariable String name, @Valid @ModelAttribute("form") CappingForm form, BindingResult result, Model model) {
        if (StringUtils.isNotEmpty(form.getSize())) {
            try {
                form.getSizeInBytes();
            } catch (Exception e) {
                result.rejectValue("size", "size.invalidPattern");
            }
        }

        if (result.hasErrors()) {
            return "sources/capping_form";
        }

        flashScope.message("Source " + name + " has been capped");

        Source.findByName(name).cap(form);

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/report")
    public String report(@PathVariable String name, Model model) {
        model.addAttribute("form", new ReportConfig());

        return "sources/report_form";
    }

    @RequestMapping(value = "/sources/{name}/report", method = RequestMethod.POST)
    public String report(@PathVariable String name, @Valid @ModelAttribute("form") ReportConfig form, BindingResult result, Model model) throws SchedulerException, ParseException {
        if (result.hasErrors()) {
            return "sources/report_form";
        }

        SourceReports.forSource(Source.findByName(name)).saveReportConfig(form);

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/report/{id}")
    public String report(@PathVariable String name, @PathVariable String id, Model model) {
        model.addAttribute("form", SourceReports.forSource(Source.findByName(name)).getReportConfig(id));

        return "sources/report_form";
    }

    @RequestMapping("/sources/{name}/report/{id}/delete")
    public String deleteReport(@PathVariable String name, @PathVariable String id, Model model) {
        SourceReports.forSource(Source.findByName(name)).deleteReportConfig(id);

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/schedule")
    public String schedule(@PathVariable String name, Model model) {
        model.addAttribute("reports", SourceReports.forSource(Source.findByName(name)).getReportConfigs());
        model.addAttribute("form", new MailSchedule());

        return "sources/schedule_form";
    }

    @RequestMapping(value = "/sources/{name}/schedule", method = RequestMethod.POST)
    public String schedule(@PathVariable String name, @Valid @ModelAttribute("form") MailSchedule form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("reports", SourceReports.forSource(Source.findByName(name)).getReportConfigs());

            return "sources/schedule_form";
        }

        SourceSchedules.forSource(Source.findByName(name)).schedule(form);

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/schedule/{id}")
    public String schedule(@PathVariable String name, @PathVariable String id, Model model) {
        model.addAttribute("reports", SourceReports.forSource(Source.findByName(name)).getReportConfigs());
        model.addAttribute("form", SourceSchedules.forSource(Source.findByName(name)).getSchedule(id));

        return "sources/schedule_form";
    }

    @RequestMapping("/sources/{name}/schedule/{id}/delete")
    public String deleteSchedule(@PathVariable String name, @PathVariable String id, Model model) throws SchedulerException {
        SourceSchedules.forSource(Source.findByName(name)).deleteSchedule(id);

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/schedule/{id}/send")
    public String sendScheduledMailReport(@PathVariable String name, @PathVariable String id, Model model) throws MessagingException, UnsupportedEncodingException {
        Source source = Source.findByName(name);

        scheduleExecutor.execute(source, SourceSchedules.forSource(source).getSchedule(id));

        flashScope.message("your email report has been sent");

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/mailreport")
    public String mailreport(@PathVariable String name, Model model) {
        model.addAttribute("form", new MailReportConfig());

        return "sources/mail_report_form";
    }

    @RequestMapping(value = "/sources/{name}/mailreport", method = RequestMethod.POST)
    public String mailreport(@PathVariable String name, @Valid @ModelAttribute("form") MailReportConfig form, BindingResult result, Model model) throws SchedulerException, ParseException {
        if (result.hasErrors()) {
            return "sources/mail_report_form";
        }

        Source.findByName(name).saveMailReport(form);

        mailReports.onReportChange(form);

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/mailreport/{id}")
    public String mailreport(@PathVariable String name, @PathVariable String id, Model model) {
        model.addAttribute("form", Source.findByName(name).getMailReport(id));

        return "sources/mail_report_form";
    }

    @RequestMapping("/sources/{name}/mailreport/{id}/delete")
    public String delete(@PathVariable String name, @PathVariable String id, Model model) throws SchedulerException {
        MailReportConfig mailReportConfig = Source.findByName(name).deleteMailReport(id);

        if (mailReportConfig != null) {
            mailReports.onReportDeleted(mailReportConfig);
        }

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/mailreport/{id}/send")
    public String sendMailReport(@PathVariable String name, @PathVariable String id, Model model) throws MessagingException, UnsupportedEncodingException {
        Source source = Source.findByName(name);

        MailReportConfig mailReport = source.getMailReport(id);

        mailReports.sendReport(mailReport);

        flashScope.message("your email report has been sent");

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/transform")
    public String transform(@PathVariable String name, Model model) {
        model.addAttribute("form", new TransformForm());

        return "sources/transform_form";
    }

    @RequestMapping(value = "/sources/{name}/transform", method = RequestMethod.POST)
    public String transform(@PathVariable String name, @Valid @ModelAttribute("form") TransformForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "sources/transform_form";
        }

        Source source = Source.findByName(name);

        TransformConfig config = source.getTransformConfig();

        config.forParam(form.getParameter()).replace(form.getOperations());

        source.saveTransformConfig(config);

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/transform/{parameter}")
    public String transform(@PathVariable String name, @PathVariable String parameter, Model model) {
        TransformConfig config = Source.findByName(name).getTransformConfig();

        TransformForm form = new TransformForm();
        form.setParameter(parameter);
        form.setOperations(config.getOperationsLiteral(parameter));

        model.addAttribute("form", form);

        return "sources/transform_form";
    }

    @RequestMapping("/sources/{name}/indexes/form")
    public String indexes(@PathVariable String name, Model model) {
        model.addAttribute("form", new IndexForm());

        return "sources/mongodb_index/add";
    }

    @RequestMapping(value = "/sources/{name}/indexes", method = RequestMethod.POST)
    public String indexes(@PathVariable String name, @Valid @ModelAttribute("form") IndexForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "sources/mongodb_index/add";
        }

        Source source = Source.findByName(name);

        source.createIndex(form);

        return "redirect:/sources/{name}/configuration";
    }

    @RequestMapping("/sources/{name}/indexes/{index}/drop")
    public String dropIndex(@PathVariable String name, @PathVariable String index) {
        Source source = Source.findByName(name);

        source.dropIndex(index);

        return "redirect:/sources/{name}/configuration";
    }


}
