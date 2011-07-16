package org.otto.web.controller;

import java.util.Date;
import java.util.Iterator;

import javax.inject.Inject;

import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Splitter;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Controller
public class EventsController {

	@Inject
    private DB mongoDb;

    @RequestMapping("/types/{name}/events")
    public String events(@PathVariable String name, Model model) {
    	if (!mongoDb.collectionExists(MongoDbHelper.EVENTS_PREFIX + name)) {
            return "redirect:/types";
        }

        DBCollection collection = mongoDb.getCollection(MongoDbHelper.EVENTS_PREFIX + name);

        Iterator<DBObject> events = collection.find().sort(new BasicDBObject("date", -1)).limit(50).iterator();

        model.addAttribute("events", events);

        return "types/events";
    }

    @RequestMapping(value = "/types/{name}/events", method = RequestMethod.POST)
    public String postEvent(@PathVariable String name, String values) {
        if (!mongoDb.collectionExists(MongoDbHelper.EVENTS_PREFIX + name)) {
            return "redirect:/types";
        }

        DBCollection collection = mongoDb.getCollection(MongoDbHelper.EVENTS_PREFIX + name);

        collection.insert(buildDBObject(values));

        return "redirect:/types/{name}/events";
    }

    private BasicDBObject buildDBObject(String values) {
        BasicDBObject object = new BasicDBObject();

        Iterable<String> keyValues = Splitter.on(",").trimResults().omitEmptyStrings().split(values);

        for (String keyValue : keyValues) {
            Iterator<String> iterator = Splitter.on("=").trimResults().omitEmptyStrings().split(keyValue).iterator();

            object.append(iterator.next(), iterator.next());
        }

        if (object.containsField("date") || (!(object.get("date") instanceof Date))) {
            object.append("date", new Date());
        }

        return object;
    }
}
