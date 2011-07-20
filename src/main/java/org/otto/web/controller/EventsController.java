package org.otto.web.controller;

import java.util.Date;
import java.util.Iterator;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.otto.web.util.DBObjectParser;
import org.otto.web.util.FlashScope;
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

	@Inject
	private DBObjectParser parser;

	@RequestMapping
	public String events(@PathVariable String name, Model model) {
		DBCollection collection = mongoDbHelper.getCollection(name);
		
		Iterator<DBObject> events = collection.find().sort(new BasicDBObject("date", -1)).limit(50).iterator();

		model.addAttribute("events", events);

		return "types/events";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void post(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) {
		DBCollection collection = mongoDbHelper.getCollection(name);
		
		@SuppressWarnings("unchecked")
		DBObject object = parser.fromMap(request.getParameterMap());
		
		if (object.containsField("date") || (!(object.get("date") instanceof Date))) {
			object.put("date", new Date());
		}

		collection.insert(object);
		
		response.setStatus(200);
	}

}
