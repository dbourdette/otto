package org.otto.web.controller;

import java.util.Iterator;

import javax.inject.Inject;
import javax.validation.Valid;

import org.otto.web.form.TypeForm;
import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Controller
public class TypesController {

    @Inject
    private DB mongoDb;

    @RequestMapping({"/types/{name}"})
    public String type(@PathVariable String name, Model model) {
        if (!mongoDb.collectionExists(MongoDbHelper.EVENTS_PREFIX + name)) {
            return "redirect:/types";
        }

        return "types/type";
    }

    @RequestMapping({"/types/form"})
    public String form(Model model) {
        model.addAttribute("form", new TypeForm());

        return "types/form";
    }

    @RequestMapping(value = "/types", method = RequestMethod.POST)
    public String createType(@Valid @ModelAttribute("form") TypeForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "index";
        }

        mongoDb.createCollection(MongoDbHelper.EVENTS_PREFIX + form.getName(), new BasicDBObject("capped", false));

        return "redirect:/types";
    }

    @RequestMapping(value = "/types/{name}", method = RequestMethod.DELETE)
    public String dropType(@PathVariable String name) {
        if (!mongoDb.collectionExists(MongoDbHelper.EVENTS_PREFIX + name)) {
            return "redirect:/types";
        }

        mongoDb.getCollection(MongoDbHelper.EVENTS_PREFIX + name).drop();

        return "redirect:/types";
    }
}
