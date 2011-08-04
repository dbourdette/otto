package org.otto.web.controller;

import org.joda.time.DateTime;
import org.otto.event.DBSource;
import org.otto.event.Event;
import org.otto.event.Sources;
import org.otto.web.util.FlashScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/sources/{name}/events")
public class EventsController {

    @Inject
    private Sources sources;

    @Inject
    private FlashScope flashScope;

    @RequestMapping
    public String events(@PathVariable String name, Model model) {
        DBSource source = sources.getSource(name);

        model.addAttribute("navItem", "logs");
        model.addAttribute("events", source.findEvents(100));

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
        @SuppressWarnings("unchecked")
        Event event = Event.fromMap(request.getParameterMap());

        event.setDateIfNoneDefined(new DateTime());

        sources.getSource(name).post(event);

        response.setStatus(200);
    }

}
