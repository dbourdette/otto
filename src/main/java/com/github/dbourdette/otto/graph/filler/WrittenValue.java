package com.github.dbourdette.otto.graph.filler;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class WrittenValue {
    public static final String DEFAULT_COLUMN = "default";

    private String column = DEFAULT_COLUMN;

    private int value;

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
