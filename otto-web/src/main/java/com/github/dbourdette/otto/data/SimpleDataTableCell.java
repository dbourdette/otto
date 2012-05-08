package com.github.dbourdette.otto.data;

import org.joda.time.Interval;

/**
 * A cell (row and column) in {@link SimpleDataTable}
 *
 * @author damien bourdette
 */
public class SimpleDataTableCell {
    private final SimpleDataTableRow row;

    private final SimpleDataTableColumn column;

    public SimpleDataTableCell(Interval row, String column) {
        this(new SimpleDataTableRow(row), new SimpleDataTableColumn(column));
    }

    public SimpleDataTableCell(SimpleDataTableRow row, String column) {
        this(row, new SimpleDataTableColumn(column));
    }

    public SimpleDataTableCell(SimpleDataTableRow row, SimpleDataTableColumn column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleDataTableCell that = (SimpleDataTableCell) o;

        if (column != null ? !column.equals(that.column) : that.column != null) return false;
        if (row != null ? !row.equals(that.row) : that.row != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = row != null ? row.hashCode() : 0;
        result = 31 * result + (column != null ? column.hashCode() : 0);
        return result;
    }
}
