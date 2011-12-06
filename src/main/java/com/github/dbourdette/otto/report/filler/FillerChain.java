package com.github.dbourdette.otto.report.filler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

import com.github.dbourdette.otto.report.Report;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class FillerChain {
    private Report report;

    private List<Filler> fillers = new ArrayList<Filler>();

    public static FillerChain forGraph(Report report) {
        FillerChain chain = new FillerChain();

        chain.report = report;

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
        report.ensureColumnsExists(context.getColumn());

        report.increaseValue(context.getColumn(), new DateTime(context.getDate()), context.getValue());
    }
}
