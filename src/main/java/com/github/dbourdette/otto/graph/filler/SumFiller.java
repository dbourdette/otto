package com.github.dbourdette.otto.graph.filler;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SumFiller implements Filler {
    private String column;

    public void setColumn(String column) {
        this.column = column;
    }

    public void modify(WrittenValue value) {

    }
}
