package com.github.dbourdette.otto.report.filler;

/**
 * A baic operation to transform the value that will be written in the report.
 *
 * @author damien bourdette
 * @version \$Revision$
 */
public interface Operation {
    public void handle(ChainContext context);
}
