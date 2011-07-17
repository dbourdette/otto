package org.otto.web.controller;

import java.util.Iterator;

import javax.inject.Inject;
import javax.validation.Valid;

import org.otto.web.form.BatchForm;
import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Controller
public class EventsController {

	@Inject
    private MongoDbHelper mongoDbHelper;

    @RequestMapping("/types/{name}/events")
    public String events(@PathVariable String name, Model model) {
    	if (mongoDbHelper.notExists(name)) {
            return "redirect:/types";
        }

        DBCollection collection = mongoDbHelper.getCollection(name);

        Iterator<DBObject> events = collection.find().sort(new BasicDBObject("date", -1)).limit(50).iterator();

        model.addAttribute("events", events);

        return "types/events";
    }

    @RequestMapping("/types/{name}/events/batch")
    public String form(@PathVariable String name, Model model) {
    	if (mongoDbHelper.notExists(name)) {
            return "redirect:/types";
        }
    	
    	model.addAttribute("form", new BatchForm());

        return "types/batch_add_event_form";
    }

    @RequestMapping(value = "/types/{name}/events/batch", method = RequestMethod.POST)
    public String postEvent(@PathVariable String name, @Valid @ModelAttribute("form") BatchForm form, BindingResult bindingResult) {
    	if (mongoDbHelper.notExists(name)) {
            return "redirect:/types";
        }
        
        if (bindingResult.hasErrors()) {
        	return "types/batch_add_event_form";
        }

        DBCollection collection = mongoDbHelper.getCollection(name);

        for (BasicDBObject object : form.buildDBObjects()) {
        	collection.insert(object);
        }

        return "redirect:/types/{name}/events/batch";
    }
}
