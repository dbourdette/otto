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

import com.github.dbourdette.otto.service.config.Config;
import com.github.dbourdette.otto.web.form.ConfigForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.dbourdette.otto.SpringConfig;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class ConfigurationController {
    @Inject
    private SpringConfig springConfig;

    @Inject
    private Config config;

    @RequestMapping("/configuration")
    public String configuration(@RequestParam(required = false) Integer page, Model model) {
        model.addAttribute("navItem", "configuration");
        model.addAttribute("config", springConfig);
        model.addAttribute("form", ConfigForm.read(config));

        return "admin/configuration";
    }

    @RequestMapping(value = "/configuration", method = RequestMethod.POST)
    public String update(@ModelAttribute("form") ConfigForm form, BindingResult result, Model model) {
        config.set(Config.MONITORING_SOURCE, form.getMonitoringSource());

        return "redirect:/configuration";
    }
}
