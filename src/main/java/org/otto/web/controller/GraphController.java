package org.otto.web.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.otto.event.DBSource;
import org.otto.event.Sources;
import org.otto.graph.Graph;
import org.otto.web.form.GraphForm;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mongodb.DBObject;

@Controller
public class GraphController {

    @Inject
    private Sources sources;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, "start", new CustomDateEditor(dateFormat, false));
        binder.registerCustomEditor(Date.class, "end", new CustomDateEditor(dateFormat, false));
    }
    
    @RequestMapping({"/sources/{name}/graph"})
    public String graph(@PathVariable String name, GraphForm form, BindingResult result, Model model) {
    	model.addAttribute("navItem", "graph");
    	model.addAttribute("form", form);
        model.addAttribute("graph", buildGraph(name, form).toGoogleHtml(1080, 750));

        return "sources/graph";
    }
    
    @RequestMapping({"/sources/{name}/graph.csv"})
    public void csv(@PathVariable String name, GraphForm form, HttpServletResponse response) throws IOException {
        response.setContentType("application/csv");
        
        response.getWriter().write(buildGraph(name, form).toCsv());
    }
    
    @RequestMapping({"/sources/{name}/graph/table"})
    public String table(@PathVariable String name, GraphForm form, Model model) throws IOException {
    	model.addAttribute("table", buildGraph(name, form).toHtmlTable());
        
        return "sources/graph_table";
    }
    
    private Graph buildGraph(String name, GraphForm form) {
    	DateMidnight start = new DateMidnight(form.getStart());
    	DateMidnight end = new DateMidnight(form.getEnd());
    	
    	Interval interval = new Interval(start, end);

        Graph graph = new Graph();
        graph.ensureColumnsExists(name);
        graph.setRows(interval, Duration.standardMinutes(form.getStepInMinutes()));

        DBSource source = sources.getSource(name);
        
        Iterator<DBObject> events = source.findEvents(interval);

        while (events.hasNext()) {
            DBObject event = events.next();

            Date date = (Date) event.get("date");

            graph.increaseValue(name, new DateTime(date));
        }

        return graph;
    }
}
