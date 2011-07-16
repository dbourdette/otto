package org.otto.web.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.otto.graph.Graph;
import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.Date;
import java.util.Iterator;

@Controller
public class GraphController {

    @Inject
    private MongoDbHelper mongoDbHelper;

    @RequestMapping({"/events/{name}/graph"})
    public String graph(@PathVariable String name, Model model) {
        if (mongoDbHelper.notExists(name)) {
            return "redirect:/events";
        }

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

        model.addAttribute("graph", graph.toHtml(1280, 750));

        return "graph";
    }

    private Iterator<DBObject> findEvents(String name, Interval interval) {
        DBCollection collection = mongoDbHelper.getCollection(name);

        BasicDBObject query = new BasicDBObject();
        query.append("date", new BasicDBObject("$gt", interval.getStart().toDate()));
        query.append("date", new BasicDBObject("$lte", interval.getEnd().toDate()));

        return collection.find(query).sort(new BasicDBObject("date", -1)).iterator();
    }
}
