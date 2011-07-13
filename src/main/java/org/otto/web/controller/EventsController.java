package org.otto.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.validation.Valid;
import org.otto.web.form.EventsForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;

@Controller
public class EventsController {

    public static final String EVENTS_PREFIX = "events.";

    @Inject
    private DB mongoDb;

    @ModelAttribute("form")
    public EventsForm form() {
        return new EventsForm();
    }

    @RequestMapping({ "/", "/events" })
    public String index(Model model) {
        List<String> collections = new ArrayList<String>();

        for (String collection : mongoDb.getCollectionNames()) {
            if (collection.startsWith(EVENTS_PREFIX)) {
                collections.add(collection.substring(EVENTS_PREFIX.length()));
            }
        }

        model.addAttribute("collections", collections);

        return "events";
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute("form") EventsForm form, BindingResult result) {
        if (!form.getName().matches("^[A-Za-z0-9]+$")) {
            result.rejectValue("name", "name.invalid");
        }

        if (result.hasErrors()) {
            return "events";
        }

        mongoDb.createCollection(EVENTS_PREFIX + form.getName(), new BasicDBObject("capped", false));

        return "redirect:/events";
    }

    @RequestMapping(value = "/events/{name}", method = RequestMethod.DELETE)
    public String drop(@PathVariable String name) {
        mongoDb.getCollection(EVENTS_PREFIX + name).drop();

        return "redirect:/events";
    }
}
