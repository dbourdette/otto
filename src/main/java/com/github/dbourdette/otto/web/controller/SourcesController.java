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

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import com.github.dbourdette.otto.source.AggregationConfig;
import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.DefaultGraphParameters;
import com.github.dbourdette.otto.source.Sources;
import com.github.dbourdette.otto.source.TimeFrame;
import com.github.dbourdette.otto.web.form.CappingForm;
import com.github.dbourdette.otto.web.form.SourceForm;
import com.github.dbourdette.otto.web.util.FlashScope;
import com.github.dbourdette.otto.web.util.IntervalUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class SourcesController {

    @Inject
    private Sources sources;

    @Inject
    private FlashScope flashScope;

    @RequestMapping({"/sources/{name}"})
    public String source(@PathVariable String name, Model model) {
        model.addAttribute("navItem", "index");

        DBSource source = sources.getSource(name);

        model.addAttribute("source", source);
        model.addAttribute("aggregation", source.getAggregation());
        model.addAttribute("lastWeekFrequency", source.findEventsFrequency(IntervalUtils.lastWeek()));
        model.addAttribute("yesterdayFrequency", source.findEventsFrequency(IntervalUtils.yesterday()));
        model.addAttribute("todayFrequency", source.findEventsFrequency(IntervalUtils.today()));
        model.addAttribute("defaultGraphParameters", source.getDefaultGraphParameters());

        return "sources/source";
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

        if (result.hasErrors()) {
            return "sources/source_form";
        }

        sources.createSource(form);

        flashScope.message("source " + form.getName() + " has just been created");

        return "redirect:/sources";
    }

    @RequestMapping("/sources/{name}/delete")
    public String dropSourceForm(@PathVariable String name, Model model) {
        model.addAttribute("navItem", "index");

        return "sources/source_delete_form";
    }

    @RequestMapping(value = "/sources/{name}", method = RequestMethod.DELETE)
    public String dropSource(@PathVariable String name) {
        sources.getSource(name).drop();

        flashScope.message("source " + name + " has just been deleted");

        return "redirect:/sources";
    }

    @RequestMapping("/sources/{name}/aggregation/form")
    public String aggregation(@PathVariable String name, Model model) {
        model.addAttribute("form", sources.getSource(name).getAggregation());
        model.addAttribute("timeFrames", TimeFrame.values());

        return "sources/aggregation_form";
    }

    @RequestMapping(value = "/sources/{name}/aggregation", method = RequestMethod.POST)
    public String saveAggregation(@PathVariable String name, @Valid @ModelAttribute("form") AggregationConfig form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("timeFrames", TimeFrame.values());

            return "sources/aggregation_form";
        }

        sources.getSource(name).saveAggregation(form);

        return "redirect:/sources/{name}";
    }

    @RequestMapping("/sources/{name}/default-graph-params/form")
    public String defaultGraphParameters(@PathVariable String name, Model model) {
        model.addAttribute("form", sources.getSource(name).getDefaultGraphParameters());

        return "sources/default_graph_params_form";
    }

    @RequestMapping(value = "/sources/{name}/default-graph-params", method = RequestMethod.POST)
    public String saveDefaultGraphParameters(@PathVariable String name, @Valid @ModelAttribute("form") DefaultGraphParameters form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "sources/default_graph_params_form";
        }

        sources.getSource(name).setDefaultGraphParameters(form);

        return "redirect:/sources/{name}";
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

        sources.getSource(name).cap(form);

        return "redirect:/sources/{name}";
    }
}
