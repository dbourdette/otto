package org.otto.web.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.otto.event.DBSource;
import org.otto.event.Event;
import org.otto.event.Sources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sources/{name}/events")
public class EventsController {

	@Inject
	private Sources sources;

	@RequestMapping
	public String events(@PathVariable String name, Model model) {
		DBSource source = sources.getSource(name);
		
		model.addAttribute("navItem", "logs");
		model.addAttribute("events", source.findEvents(100));

		return "sources/events";
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
