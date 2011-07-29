package org.otto.web.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.otto.event.DBSource;
import org.otto.event.Sources;
import org.otto.event.TimeFrame;
import org.otto.web.form.SourceForm;
import org.otto.web.form.AggregationForm;
import org.otto.web.util.FlashScope;
import org.otto.web.util.IntervalUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SourcesController {
    
    @Inject
    private Sources sources;
    
    @Inject
    private FlashScope flashScope;

    @RequestMapping({"/sources/{name}"})
    public String source(@PathVariable String name, Model model) {
    	model.addAttribute("navItem", "index");
    	
    	DBSource source = sources.getSource(name);
    	
        model.addAttribute("count", source.count());
        model.addAttribute("capped", source.isCapped());
        model.addAttribute("timeFrame", source.getTimeFrame());
        model.addAttribute("lastWeekFrequency", source.frequency(IntervalUtils.lastWeek()));
        model.addAttribute("yesterdayFrequency", source.frequency(IntervalUtils.yesterday()));
        model.addAttribute("todayFrequency", source.frequency(IntervalUtils.today()));
        
        return "sources/source";
    }

    @RequestMapping("/sources/form")
    public String form(Model model) {
        model.addAttribute("form", new SourceForm());

        return "sources/source_form";
    }

    @RequestMapping(value = "/sources", method = RequestMethod.POST)
    public String createSource(@Valid @ModelAttribute("form") SourceForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "sources/source_form";
        }

        sources.createCollection(form);
        
        flashScope.message("source " + form.getName() + " has just been created");

        return "redirect:/sources";
    }

    @RequestMapping(value = "/sources/{name}", method = RequestMethod.DELETE)
    public String dropSource(@PathVariable String name) {
    	sources.getSource(name).drop();
    	
    	flashScope.message("source " + name + " has just been deleted");
    	
        return "redirect:/sources";
    }
    
    @RequestMapping("/sources/{name}/aggregation/form")
    public String aggregation(@PathVariable String name, Model model) {
    	AggregationForm form = new AggregationForm();
    	form.setTimeFrame(sources.getSource(name).getTimeFrame());
    	
        model.addAttribute("form", form);
        model.addAttribute("timeFrames", TimeFrame.values());

        return "sources/aggregation_form";
    }
    
    @RequestMapping(value = "/sources/{name}/aggregation", method = RequestMethod.POST)
    public String saveAggregation(@PathVariable String name, @Valid @ModelAttribute("form") AggregationForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		model.addAttribute("timeFrames", TimeFrame.values());
    		
            return "sources/aggregation_form";
        }
    	
    	sources.getSource(name).saveTimeFrame(form.getTimeFrame());

    	return "redirect:/sources/{name}";
    }
}
