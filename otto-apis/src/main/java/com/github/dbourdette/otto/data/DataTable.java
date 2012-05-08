package com.github.dbourdette.otto.data;

import java.util.List;

import org.joda.time.Interval;

/**
 * Table containing data read from database and transformed using operations.
 *
 * @author damien bourdette
 */
public interface DataTable {
    public List<String> getColumns();

    public List<Interval> getRows();

    public Integer getValue(Interval row, String column);

    public int getSum(String column);
}
