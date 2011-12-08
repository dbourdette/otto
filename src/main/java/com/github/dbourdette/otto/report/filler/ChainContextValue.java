package com.github.dbourdette.otto.report.filler;

/**
 * Holds data that will be written in report.
 *
 * @author damien bourdette
 * @version \$Revision$
 */
public class ChainContextValue {
    public static final String DEFAULT_COLUMN = "default";

    private String column = DEFAULT_COLUMN;

    private int value = 1;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
