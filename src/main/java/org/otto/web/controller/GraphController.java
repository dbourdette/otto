package org.otto.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.otto.graph.Graph;
import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Controller
public class GraphController {

    @Inject
    private MongoDbHelper mongoDbHelper;

    @RequestMapping({"/types/{name}/graph"})
    public String graph(@PathVariable String name, Model model) {
    	model.addAttribute("navItem", "graph");
        model.addAttribute("graph", buildGraph(name).toGoogleHtml(1280, 750));

        return "types/graph";
    }
    
    @RequestMapping({"/types/{name}/graph.csv"})
    public void csv(@PathVariable String name, HttpServletResponse response) throws IOException {
        response.setContentType("application/csv");
        
        response.getWriter().write(buildGraph(name).toCsv());
    }
    
    @RequestMapping({"/types/{name}/graph/table"})
    public String table(@PathVariable String name, Model model) throws IOException {
    	model.addAttribute("table", buildGraph(name).toHtmlTable());
        
        return "types/graph_table";
    }
    
    private Graph buildGraph(String name) {
    	DateMidnight today = new DateMidnight();
    	
    	Interval interval = new Interval(today.minusDays(1), today.plusDays(1));

        Graph graph = new Graph(name);
        graph.ensureColumnsExists(name);
        graph.setRows(interval, Duration.standardMinutes(5));

        Iterator<DBObject> events = findEvents(name, interval);

        while (events.hasNext()) {
            DBObject event = events.next();

            Date date = (Date) event.get("date");

            graph.increaseValue(name, new DateTime(date));
        }

        return graph;
    }

    private Iterator<DBObject> findEvents(String name, Interval interval) {
        DBCollection collection = mongoDbHelper.getCollection(name);

        BasicDBObject query = mongoDbHelper.intervalQuery(interval);

        return collection.find(query).sort(new BasicDBObject("date", -1)).iterator();
    }
}
