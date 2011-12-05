package com.github.dbourdette.otto.graph.filler;

import org.apache.commons.lang.StringUtils;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SumFiller implements Filler {
    private String column;

    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public void handle(FillerContext context) {
        if (StringUtils.isEmpty(column)) {
            return;
        }

        Object object = context.get(column);

        if (object instanceof Integer) {
            context.setValue((Integer) object);
        }
    }
}
