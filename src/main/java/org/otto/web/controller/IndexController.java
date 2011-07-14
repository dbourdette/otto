package org.otto.web.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import org.otto.web.form.EventsForm;
import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Inject
    private DB mongoDb;

    @ModelAttribute("form")
    public EventsForm form() {
        return new EventsForm();
    }

    @RequestMapping({"/", "/events"})
    public String index(Model model) {
        List<String> collections = new ArrayList<String>();

        for (String collection : mongoDb.getCollectionNames()) {
            if (collection.startsWith(MongoDbHelper.EVENTS_PREFIX)) {
                collections.add(collection.substring(MongoDbHelper.EVENTS_PREFIX.length()));
            }
        }

        model.addAttribute("collections", collections);

        return "index";
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public String createCollection(@Valid @ModelAttribute("form") EventsForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "index";
        }

        mongoDb.createCollection(MongoDbHelper.EVENTS_PREFIX + form.getName(), new BasicDBObject("capped", false));

        return "redirect:/events";
    }

    @RequestMapping(value = "/events/{name}", method = RequestMethod.DELETE)
    public String dropCollection(@PathVariable String name) {
        if (!mongoDb.collectionExists(MongoDbHelper.EVENTS_PREFIX + name)) {
            return "redirect:/events";
        }

        mongoDb.getCollection(MongoDbHelper.EVENTS_PREFIX + name).drop();

        return "redirect:/events";
    }
}
