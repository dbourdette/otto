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
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.Sources;
import com.github.dbourdette.otto.web.form.GraphForm;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class StatsController {

    @Inject
    private Sources sources;

    @RequestMapping({"/sources/{name}/stats"})
    public String graph(@PathVariable String name, @Valid GraphForm form, BindingResult result, Model model, HttpServletRequest request) {
        DBSource source = sources.getSource(name);

        form.fillWithDefault(source.getDefaultGraphParameters(), request);

        model.addAttribute("navItem", "stats");
        model.addAttribute("form", form);

        if (StringUtils.isNotEmpty(form.getSumColumn())) {
            model.addAttribute("sums", form.getValues(source));
        }

        model.addAttribute("counts", form.getCounts(source));

        return "sources/stats";
    }
}
