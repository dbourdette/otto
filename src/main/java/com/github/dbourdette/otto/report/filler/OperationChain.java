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
public class OperationChain {
    private Report report;

    private List<Operation> operations = new ArrayList<Operation>();

    public static OperationChain forGraph(Report report) {
        OperationChain chain = new OperationChain();

        chain.report = report;

        return chain;
    }

    private OperationChain() {
    }

    public OperationChain add(Operation operation) {
        operations.add(operation);

        return this;
    }

    public void write(DBObject event) {
        LinkedList<Operation> fifo = new LinkedList<Operation>();

        fifo.addAll(operations);

        apply(fifo, new ChainContext(event));
    }

    @SuppressWarnings("unchecked")
    private void apply(LinkedList<Operation> fifo, ChainContext context) {
        if (fifo.size() == 0) {
            doWrite(context);

            return;
        }

        Operation operation = fifo.pop();

        operation.handle(context);

        if (context.hasSubContextes()) {
            for (ChainContext subContext : context.getSubContextes()) {
                apply((LinkedList<Operation>) fifo.clone(), subContext);
            }
        } else {
            apply(fifo, context);
        }
    }

    private void doWrite(ChainContext context) {
        report.ensureColumnsExists(context.getColumn());

        report.increaseValue(context.getColumn(), new DateTime(context.getDate()), context.getValue());
    }
}
