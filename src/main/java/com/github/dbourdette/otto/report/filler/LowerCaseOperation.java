package com.github.dbourdette.otto.report.filler;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class LowerCaseOperation implements Operation {
    @Override
    public void handle(ChainContext context) {
        context.setColumn(context.getColumn().toLowerCase());
    }
}
