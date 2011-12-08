package com.github.dbourdette.otto.report.filler;

import org.apache.commons.lang.StringUtils;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SumOperation implements Operation {
    private String column;

    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public void handle(ChainContext context) {
        if (StringUtils.isEmpty(column)) {
            return;
        }

        Object object = context.get(column);

        if (object instanceof Integer) {
            context.setValue((Integer) object);
        }
    }
}
