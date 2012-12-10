/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dbourdette.otto.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import com.github.dbourdette.otto.web.util.Pair;

/**
 * Simple implementation of {@link DataTable}
 *
 * @author damien bourdette
 * @version \$Revision$
 */
public class SimpleDataTable implements DataTable {
    private static final Duration FIVE_MINUTES = Duration.standardMinutes(5);

    private final List<SimpleDataTableRow> rows = new ArrayList<SimpleDataTableRow>();

    private List<SimpleDataTableColumn> columns = new ArrayList<SimpleDataTableColumn>();

    private final Map<SimpleDataTableCell, Integer> cells = new HashMap<SimpleDataTableCell, Integer>();

    private Integer defaultValue = 0;

    public static SimpleDataTable forInterval(DateTime start, DateTime end) {
        return forInterval(new Interval(start, end));
    }

    public static SimpleDataTable forInterval(Interval interval) {
        SimpleDataTable table = new SimpleDataTable();

        table.setupRows(interval);

        return table;
    }

    @Override
    public List<String> getColumns() {
        List<String> result = new ArrayList<String>();

        for (SimpleDataTableColumn column : columns) {
            result.add(column.getTitle());
        }

        return result;
    }

    @Override
    public List<Interval> getRows() {
        List<Interval> intervals = new ArrayList<Interval>();

        for (SimpleDataTableRow row : rows) {
            intervals.add(row.getInterval());
        }

        return intervals;
    }

    @Override
    public Integer getValue(Interval row, String column) {
        return getValue(new SimpleDataTableCell(row, column));
    }

    @Override
    public int getSum(String column) {
        int sum = 0;

        for (SimpleDataTableRow row : rows) {
            Integer value = getValue(new SimpleDataTableCell(row, column));

            if (value != null) {
                sum += value;
            }
        }

        return sum;
    }

    /**
     * Keeps only count top columns from all current columns.
     */
    @Override
    public void top(int count) {
        if (count >= columns.size()) {
            return;
        }

        List<ColumnSum> sums = sortedSums();

        List<ColumnSum> columnsToDrop = sums.subList(0, sums.size() - count);

        for (ColumnSum sum : columnsToDrop) {
            dropColumn(sum.getColumn().getTitle());
        }
    }

    @Override
    public void sortAlphabetically() {
        Collections.sort(columns, new Comparator<SimpleDataTableColumn>() {
            @Override
            public int compare(SimpleDataTableColumn o1, SimpleDataTableColumn o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
    }

    @Override
    public void sortBySum() {
        List<ColumnSum> sums = sortedSums();
        Collections.reverse(sums);

        columns.clear();

        for (ColumnSum sum : sums) {
            columns.add(sum.getColumn());
        }
    }

    public void setupRows(DataTablePeriod period) {
        setupRows(period.getInterval(), period.getStepDuration());
    }

    public void setupRows(Interval interval, Duration step) {
        rows.clear();

        DateTime current = interval.getStart();

        while (current.isBefore(interval.getEnd())) {
            rows.add(new SimpleDataTableRow(new Interval(current, step)));

            current = current.plus(step);
        }
    }

    public void setupRows(Interval interval) {
        setupRows(interval, FIVE_MINUTES);
    }

    public void dropColumn(String column) {
        assertExists(column);

        columns.remove(new SimpleDataTableColumn(column));

        for (SimpleDataTableRow row : rows) {
            cells.remove(new SimpleDataTableCell(row.getInterval(), column));
        }
    }

    public void ensureColumnExists(String... columns) {
        if (columns == null) {
            return;
        }

        for (String column : columns) {
            if (!hasColumn(column)) {
                this.columns.add(new SimpleDataTableColumn(column));
            }
        }
    }

    public boolean hasColumn(String column) {
        return columns.contains(new SimpleDataTableColumn(column));
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int getRowCount() {
        return rows.size();
    }

    public Interval getRow(int index) {
        return rows.get(index).getInterval();
    }

    public DateTime getStartDate() {
        if (rows.size() == 0) {
            return null;
        }

        return rows.get(0).getStartDate();
    }

    public DateTime getEndDate() {
        if (rows.size() == 0) {
            return null;
        }

        return rows.get(rows.size() - 1).getEndDate();
    }

    public void setValue(String column, DateTime date, Integer value) {
        assertExists(column);

        cells.put(new SimpleDataTableCell(getRow(date), column), value);
    }

    public void increaseValue(String column, DateTime date) {
        increaseValue(column, date, 1);
    }

    public void increaseValue(String column, DateTime date, Integer value) {
        assertExists(column);

        if (value == null) {
            return;
        }

        SimpleDataTableCell cell = new SimpleDataTableCell(getRow(date), column);

        Integer cellValue = cells.get(cell);

        if (cellValue == null) {
            cellValue = 0;
        }

        cells.put(cell, cellValue + value);
    }

    public Integer getValue(String column, int rowIndex) {
        assertExists(column);

        return getValue(new SimpleDataTableCell(rows.get(rowIndex), column));
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Integer defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<Pair> getSums() {
        List<Pair> values = new ArrayList<Pair>();

        for (String column : getColumns()) {
            values.add(new Pair(column, getSum(column)));
        }

        return values;
    }

    private SimpleDataTableRow getRow(DateTime date) {
        for (SimpleDataTableRow row : rows) {
            if (row.contains(date)) {
                return row;
            }
        }

        throw new IllegalArgumentException("No row found for date " + date);
    }

    private void assertExists(String column) {
        if (!hasColumn(column)) {
            throw new IllegalArgumentException("No column found for " + column);
        }
    }

    private Integer getValue(SimpleDataTableCell cell) {
        Integer value = cells.get(cell);

        return value == null ? defaultValue : value;
    }

    private List<ColumnSum> sortedSums() {
        List<ColumnSum> sums = new ArrayList<ColumnSum>();

        for (SimpleDataTableColumn column : columns) {
            ColumnSum sum = new ColumnSum(column);

            for (SimpleDataTableRow row : rows) {
                sum.add(getValue(new SimpleDataTableCell(row, column)));
            }

            sums.add(sum);
        }

        Collections.sort(sums);

        return sums;
    }

    private class ColumnSum implements Comparable<ColumnSum> {

        private int sum;

        private final SimpleDataTableColumn column;

        public ColumnSum(SimpleDataTableColumn column) {
            this.column = column;
        }

        public void add(Integer value) {
            if (value == null) {
                return;
            }

            sum += value;
        }

        public SimpleDataTableColumn getColumn() {
            return column;
        }

        @Override
        public int compareTo(ColumnSum o) {
            return sum - o.sum;
        }

        @Override
        public String toString() {
            return "ColumnSum [column=" + column + ", sum=" + sum + "]";
        }
    }
}
