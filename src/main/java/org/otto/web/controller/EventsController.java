package org.otto.web.controller;

import java.util.Iterator;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.otto.event.Event;
import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/types/{name}/events")
public class EventsController {

	@Inject
	private MongoDbHelper mongoDbHelper;

	@RequestMapping
	public String events(@PathVariable String name, Model model) {
		DBCollection collection = mongoDbHelper.getCollection(name);
		
		Iterator<DBObject> events = collection.find().sort(new BasicDBObject("date", -1)).limit(100).iterator();

		model.addAttribute("navItem", "logs");
		model.addAttribute("events", events);

		return "types/events";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void post(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) {
		DBCollection collection = mongoDbHelper.getCollection(name);
		
		@SuppressWarnings("unchecked")
		Event event = Event.fromMap(request.getParameterMap());
		
		event.setDateIfNoneDefined(new DateTime());

		collection.insert(event.toDBObject());
		
		response.setStatus(200);
	}

}
