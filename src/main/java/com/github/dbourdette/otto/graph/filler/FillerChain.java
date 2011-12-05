package com.github.dbourdette.otto.graph.filler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.github.dbourdette.otto.graph.Graph;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class FillerChain {
    private Graph graph;

    private String defaultColumn;

    private List<Filler> fillers = new ArrayList<Filler>();

    public static FillerChain forGraph(Graph graph) {
        FillerChain chain = new FillerChain();

        chain.graph = graph;

        return chain;
    }

    private FillerChain() {
    }

    public FillerChain defaultColumn(String defaultColumn) {
        this.defaultColumn = defaultColumn;

        return this;
    }

    public FillerChain add(Filler filler) {
        fillers.add(filler);

        return this;
    }

    public void write(DBObject event) {
        WrittenValue value = new WrittenValue();

        if (StringUtils.isNotEmpty(defaultColumn)) {
            value.setColumn(defaultColumn);
        }

        for (Filler filler : fillers) {
            filler.modify(value);
        }

        Date date = (Date) event.get("date");

        graph.ensureColumnsExists(value.getColumn());

        graph.increaseValue(value.getColumn(), new DateTime(date), value.getValue());
    }
}
