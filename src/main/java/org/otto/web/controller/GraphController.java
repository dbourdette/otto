package org.otto.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
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
        model.addAttribute("graph", buildGraph(name).toHtml(1280, 750));

        return "types/graph";
    }
    
    @RequestMapping({"/types/{name}/graph.csv"})
    public void csv(@PathVariable String name, HttpServletResponse response) throws IOException {
        response.setContentType("application/csv");
        
        response.getWriter().write(buildGraph(name).toCsv());
    }
    
    private Graph buildGraph(String name) {
    	Interval interval = new Interval(new DateTime().minusDays(1), new DateTime());

        Graph graph = new Graph(name);
        graph.ensureColumnsExists(name);
        graph.addRows(interval);

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
