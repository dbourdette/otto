package org.otto.web.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.otto.web.form.TypeForm;
import org.otto.web.util.FlashScope;
import org.otto.web.util.IntervalUtils;
import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TypesController {
    
    @Inject
    private MongoDbHelper mongoDbHelper;
    
    @Inject
    private FlashScope flashScope;

    @RequestMapping({"/types/{name}"})
    public String type(@PathVariable String name, Model model) {
        model.addAttribute("count", mongoDbHelper.count(name));
        model.addAttribute("lastWeekFrequency", mongoDbHelper.frequency(name, IntervalUtils.lastWeek()));
        model.addAttribute("yesterdayFrequency", mongoDbHelper.frequency(name, IntervalUtils.yesterday()));
        model.addAttribute("todayFrequency", mongoDbHelper.frequency(name, IntervalUtils.today()));

        return "types/type";
    }

    @RequestMapping({"/types/form"})
    public String form(Model model) {
        model.addAttribute("form", new TypeForm());

        return "types/type_form";
    }

    @RequestMapping(value = "/types", method = RequestMethod.POST)
    public String createType(@Valid @ModelAttribute("form") TypeForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "types/type_form";
        }

        mongoDbHelper.createCollection(form.getName());
        
        flashScope.message("type " + form.getName() + " has just been created");

        return "redirect:/types";
    }

    @RequestMapping(value = "/types/{name}", method = RequestMethod.DELETE)
    public String dropType(@PathVariable String name) {
    	mongoDbHelper.getCollection(name).drop();
    	
    	flashScope.message("type " + name + " has just been deleted");

        return "redirect:/types";
    }
}
