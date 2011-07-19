package org.otto.web.controller;

import java.util.Iterator;

import javax.inject.Inject;

import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Controller
public class EventsController {

	@Inject
	private MongoDbHelper mongoDbHelper;

	@RequestMapping("/types/{name}/events")
	public String events(@PathVariable String name, Model model) {
		DBCollection collection = mongoDbHelper.getCollection(name);

		Iterator<DBObject> events = collection.find().sort(new BasicDBObject("date", -1)).limit(50).iterator();

		model.addAttribute("events", events);

		return "types/events";
	}

}
