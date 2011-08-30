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
import javax.servlet.http.HttpServletResponse;

import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.Sources;
import com.github.dbourdette.otto.web.service.RemoteEventsFacade;
import com.github.dbourdette.otto.web.util.FlashScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
@RequestMapping("/sources/{name}/events")
public class EventsController {

    @Inject
    private Sources sources;

    @Inject
    private FlashScope flashScope;

    @Inject
    private RemoteEventsFacade remoteEventsFacade;

    @RequestMapping
    public String events(@PathVariable String name, @RequestParam(required = false) Integer page, Model model) {
        DBSource source = sources.getSource(name);

        model.addAttribute("navItem", "logs");
        model.addAttribute("events", source.findEvents(page));

        return "sources/events";
    }

    @RequestMapping("/delete")
    public String clearForm(@PathVariable String name, Model model) {
        model.addAttribute("navItem", "logs");

        DBSource source = sources.getSource(name);

        if (source.isCapped()) {
            return "sources/events_delete_capped";
        } else {
            return "sources/events_delete_form";
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String clear(@PathVariable String name) {
        DBSource source = sources.getSource(name);

        if (source.isCapped()) {
            return "redirect:/sources/{name}/events";
        }

        source.clearEvents();

        flashScope.message("Events have been cleared for source " + name);

        return "redirect:/sources/{name}/events";
    }

    @RequestMapping(method = RequestMethod.POST)
    public void post(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) {
        remoteEventsFacade.post(name, request);

        response.setStatus(200);
    }

}
