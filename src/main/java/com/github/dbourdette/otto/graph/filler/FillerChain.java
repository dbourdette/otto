package com.github.dbourdette.otto.graph.filler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

import com.github.dbourdette.otto.graph.Graph;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class FillerChain {
    private Graph graph;

    private List<Filler> fillers = new ArrayList<Filler>();

    public static FillerChain forGraph(Graph graph) {
        FillerChain chain = new FillerChain();

        chain.graph = graph;

        return chain;
    }

    private FillerChain() {
    }

    public FillerChain add(Filler filler) {
        fillers.add(filler);

        return this;
    }

    public void write(DBObject event) {
        LinkedList<Filler> fifo = new LinkedList<Filler>();

        fifo.addAll(fillers);

        apply(fifo, new FillerContext(event));
    }

    @SuppressWarnings("unchecked")
    private void apply(LinkedList<Filler> fifo, FillerContext context) {
        if (fifo.size() == 0) {
            doWrite(context);

            return;
        }

        Filler filler = fifo.pop();

        filler.handle(context);

        if (context.hasSubContextes()) {
            for (FillerContext subContext : context.getSubContextes()) {
                apply((LinkedList<Filler>) fifo.clone(), subContext);
            }
        } else {
            apply(fifo, context);
        }
    }

    private void doWrite(FillerContext context) {
        graph.ensureColumnsExists(context.getColumn());

        graph.increaseValue(context.getColumn(), new DateTime(context.getDate()), context.getValue());
    }
}
