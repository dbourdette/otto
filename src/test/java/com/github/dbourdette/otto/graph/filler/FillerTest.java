package com.github.dbourdette.otto.graph.filler;

import org.joda.time.DateMidnight;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.otto.graph.Graph;
import com.github.dbourdette.otto.graph.GraphPeriod;
import com.mongodb.BasicDBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class FillerTest {
    private Graph graph;

    @Before
    public void init() {
        graph = new Graph();
        GraphPeriod.TODAY.setRows(graph);
    }

    @Test
    public void nofiller() {
        chain().write(event());

        Assert.assertEquals(1, graph.getValue(WrittenValue.DEFAULT_COLUMN, 0).intValue());
    }

    @Test
    public void sum() {
        SumFiller sum = new SumFiller();
        sum.setColumn("value");

        chain(sum).write(event());

        Assert.assertEquals(10, graph.getValue(WrittenValue.DEFAULT_COLUMN, 0).intValue());
    }

    private BasicDBObject event() {
        BasicDBObject event = new BasicDBObject();

        event.put("value", "10");
        event.put("date", new DateMidnight().toDate());

        return event;
    }

    private FillerChain chain(Filler... fillers) {
        FillerChain chain = FillerChain.forGraph(graph);

        for (Filler filler : fillers) {
            chain.add(filler);
        }

        return chain;
    }
}
