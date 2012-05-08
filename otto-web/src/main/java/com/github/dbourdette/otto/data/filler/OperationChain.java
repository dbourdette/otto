package com.github.dbourdette.otto.data.filler;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.github.dbourdette.otto.data.SimpleDataTable;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class OperationChain {
    public static final String DEFAULT_COLUMN = "default";

    private SimpleDataTable table;

    private String labelAttributes;

    private String valueAttribute;

    private List<Operation> operations = new ArrayList<Operation>();

    public static OperationChain forTable(SimpleDataTable table) {
        OperationChain chain = new OperationChain();

        chain.table = table;

        return chain;
    }

    private OperationChain() {
    }

    public void setLabelAttributes(String labelAttributes) {
        this.labelAttributes = labelAttributes;
    }

    public OperationChain labelAttributes(String labelAttributes) {
        setLabelAttributes(labelAttributes);

        return this;
    }

    public void setValueAttribute(String valueAttribute) {
        this.valueAttribute = valueAttribute;
    }

    public OperationChain valueAttribute(String valueAttribute) {
        setValueAttribute(valueAttribute);

        return this;
    }

    public OperationChain add(Operation operation) {
        operations.add(operation);

        return this;
    }

    public void write(DBObject event) {
        LinkedList<Operation> fifo = new LinkedList<Operation>();

        fifo.addAll(operations);

        apply(fifo, (Date) event.get("date"), readColumn(event), readValue(event));
    }

    @SuppressWarnings("unchecked")
    private void apply(LinkedList<Operation> fifo, Date date, String column, int value) {
        if (fifo.size() == 0) {
            doWrite(date, column, value);

            return;
        }

        Operation operation = fifo.pop();

        List<String> columns = operation.handle(column);

        for (String newColumn : columns) {
            apply((LinkedList<Operation>) fifo.clone(), date, newColumn, value);
        }
    }

    private void doWrite(Date date, String column, int value) {
        table.ensureColumnExists(column);

        table.increaseValue(column, new DateTime(date), value);
    }

    private String readColumn(DBObject event) {
        if (StringUtils.isEmpty(labelAttributes)) {
            return DEFAULT_COLUMN;
        }

        StringBuilder result = new StringBuilder();

        for (String attribute : StringUtils.split(labelAttributes, ",")) {
            Object value = event.get(attribute);

            if (value != null) {
                if (result.length() != 0) {
                    result.append(" - ");
                }

                result.append(value.toString());
            }
        }

        String column = result.toString();

        if (StringUtils.isEmpty(column)) {
            return DEFAULT_COLUMN;
        }

        return column;
    }

    private int readValue(DBObject event) {
        if (StringUtils.isEmpty(valueAttribute)) {
            return 1;
        }

        Object object = event.get(valueAttribute);

        if (object instanceof Integer) {
            return (Integer) object;
        }

        return 1;
    }
}
