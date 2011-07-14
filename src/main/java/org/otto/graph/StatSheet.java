package org.otto.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.springframework.util.Assert;

import com.google.common.base.Objects;

/**
 * Class containing all data from db, being able to build csv or js inserts.
 *
 */
public class StatSheet {

    private class CellKey {

        private final StatSheetRow row;

        private final StatSheetColumn column;

        public CellKey(StatSheetRow row, StatSheetColumn column) {
            super();
            this.row = row;
            this.column = column;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((column == null) ? 0 : column.hashCode());
            result = prime * result + ((row == null) ? 0 : row.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CellKey other = (CellKey) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (column == null) {
                if (other.column != null)
                    return false;
            } else if (!column.equals(other.column))
                return false;
            if (row == null) {
                if (other.row != null)
                    return false;
            } else if (!row.equals(other.row))
                return false;
            return true;
        }

        private StatSheet getOuterType() {
            return StatSheet.this;
        }
    }

    private class ColumnSum implements Comparable<ColumnSum> {

        private int sum;

        private final StatSheetColumn column;

        public ColumnSum(StatSheetColumn column) {
            this.column = column;
        }

        public void add(Integer value) {
            if (value == null) {
                return;
            }

            sum += value;
        }

        public StatSheetColumn getColumn() {
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

    private static final int DEFAULT_WIDTH = 1200;

    private static final int DEFAULT_HEIGHT = 500;

    private String title;

    private final List<StatSheetRow> rows = new ArrayList<StatSheetRow>();

    private final List<StatSheetColumn> columns = new ArrayList<StatSheetColumn>();

    private final Map<CellKey, Integer> cells = new HashMap<CellKey, Integer>();

    private Integer defaultValue = 0;

    public StatSheet() {
    }

    public static StatSheet sheet(String title) {
        StatSheet statSheet = new StatSheet(title);

        return statSheet;
    }

    public StatSheet rows(DateTime start, DateTime end) {
        addRows(start, end);

        return this;
    }

    public StatSheet rows(DateTime start, DateTime end, Duration period) {
        addRows(start, end, period);

        return this;
    }

    public StatSheet(String title) {
        this.title = title;
    }

    public void dropColumn(String title) {
        dropColumn(getColumn(title));
    }

    public void ensureColumnExists(String title) {
        if (!hasColumn(title)) {
            columns.add(new StatSheetColumn(title));
        }
    }

    public void ensureColumnsExists(String... titles) {
        for (String title : titles) {
            ensureColumnExists(title);
        }
    }

    public boolean hasColumn(String title) {
        return safeGetColumn(title) != null;
    }

    public int getColumnCount() {
        return columns.size();
    }

    public void addRow(Interval interval) {
        Assert.notNull(interval, "Interval can not be null");

        StatSheetRow row = new StatSheetRow(interval);

        rows.add(row);

        Collections.sort(rows);
    }

    public void addRows(Interval interval) {
        addRows(interval.getStart(), interval.getEnd());
    }

    public void addRows(DateTime start, DateTime end) {
        addRows(start, end, StatSheetDurationUtils.findBest(start, end));
    }

    public void addRows(DateTime start, DateTime end, Duration period) {
        DateTime current = start;

        while (current.isBefore(end)) {
            addRow(new Interval(current, period));

            current = current.plus(period);
        }
    }

    public int getRowCount() {
        return rows.size();
    }

    public DateTime getRowStartDate(int index) {
        return rows.get(index).getStartDate();
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

    public void setValue(String columnTitle, DateTime date, Integer value) {
        StatSheetRow row = getRow(date);
        StatSheetColumn column = getColumn(columnTitle);

        setValue(row, column, value);
    }

    public void increaseValue(String columnTitle, DateTime date) {
        increaseValue(columnTitle, date, 1);
    }

    public void increaseValue(String columnTitle, DateTime date, Integer value) {
        if (value == null) {
            return;
        }

        StatSheetRow row = getRow(date);
        StatSheetColumn column = getColumn(columnTitle);

        Integer cellValue = cells.get(new CellKey(row, column));

        if (cellValue == null) {
            cellValue = 0;
        }

        cells.put(new CellKey(row, column), cellValue + value);
    }

    public Integer getValue(String columnTitle, int rowIndex) {
        StatSheetRow row = rows.get(rowIndex);
        StatSheetColumn column = getColumn(columnTitle);

        return getValue(row, column);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Integer defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void cumulate(String... columnTitles) {
        for (String title : columnTitles) {
            cumulate(title);
        }
    }

    /**
     * Apply a cumulative sum to given columns
     * 
     * @param columnTitle
     */
    public void cumulate(String columnTitle) {
        StatSheetColumn column = getColumn(columnTitle);

        Integer sum = 0;

        for (StatSheetRow row : rows) {
            Integer value = getValue(row, column);

            if (value != null) {
                sum += value;
            }

            setValue(row, column, sum);
        }
    }

    /**
     * Keeps only count top columns from given columns.
     */
    public void top(int count, String... columnTitles) {
        if (count >= columnTitles.length) {
            return;
        }

        List<ColumnSum> sums = new ArrayList<StatSheet.ColumnSum>();

        for (StatSheetColumn column : getColumns(columnTitles)) {
            ColumnSum sum = new ColumnSum(column);

            for (StatSheetRow row : rows) {
                sum.add(getValue(row, column));
            }

            sums.add(sum);
        }

        Collections.sort(sums);

        List<ColumnSum> columnsToDrop = sums.subList(0, sums.size() - count);

        for (ColumnSum sum : columnsToDrop) {
            dropColumn(sum.getColumn());
        }
    }

    public String toCsv() {
        StringBuilder builder = new StringBuilder();

        builder.append("startDate,endDate");

        for (StatSheetColumn column : columns) {
            builder.append(",");
            builder.append(column.getTitle());
        }

        builder.append("\n");

        for (StatSheetRow row : rows) {
            builder.append(row.getStartDate());
            builder.append(",");
            builder.append(row.getEndDate());

            for (StatSheetColumn column : columns) {
                builder.append(",");
                builder.append(getValue(row, column));
            }

            builder.append("\n");
        }

        return builder.toString();
    }

    /**
     * Produces google chart js code.
     */
    public String toJs(String elementId, Integer width, Integer height) {
        StringBuilder builder = new StringBuilder();

        builder.append("var data = new google.visualization.DataTable();\n");
        builder.append("data.addColumn('date', 'Date');\n");

        for (StatSheetColumn column : columns) {
            builder.append("data.addColumn('number', '" + StringEscapeUtils.escapeJavaScript(column.getTitle())
                           + "');\n");
        }
        
        builder.append("data.addRows(" + rows.size() + ");\n");

        int rowIndex = 0;
        int columnIndex = 0;

        for (StatSheetRow row : rows) {
            columnIndex = 0;

            builder.append("data.setValue(" + rowIndex + ", " + columnIndex + ", new Date("
                           + row.getStartDate().getMillis() + "));\n");

            for (StatSheetColumn column : columns) {
                columnIndex++;

                builder.append("data.setValue(" + rowIndex + ", " + columnIndex + ", " + getValue(row, column) + ");\n");
            }

            rowIndex++;
        }

        builder.append("var formatter = new google.visualization.DateFormat({pattern: '" + getDatePattern() + "'});\n");
        builder.append("formatter.format(data, 0);\n");

        builder.append("var chart = new google.visualization.LineChart(document.getElementById('" + elementId
                       + "'));\n");
        builder.append("chart.draw(data, {width: " + (width == null ? DEFAULT_WIDTH : width) + ", height: "
                       + (height == null ? DEFAULT_HEIGHT : height) + ", title: '" + title + "'});");

        return builder.toString();
    }

    public String toHtml(Integer width, Integer height) {
        String elementId = "chart_div_" + UUID.randomUUID().toString();

        StringBuilder builder = new StringBuilder();

        builder.append("<div id=\"" + elementId + "\"></div>");

        builder.append("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});");
        builder.append("google.setOnLoadCallback(drawChart);");
        builder.append("function drawChart() {");

        builder.append(toJs(elementId, width, height));

        builder.append("}");
        builder.append("</script>");

        return builder.toString();
    }

    public String getDatePattern() {
        if (rows.size() == 0) {
            return "dd MMM yyyy";
        }

        Duration duration = new Duration(rows.get(0).getStartDate(), rows.get(0).getEndDate());

        if (duration.isShorterThan(Duration.standardDays(1))) {
            return "dd MM yyyy HH:mm";
        } else {
            return "dd MMM yyyy";
        }
    }

    private StatSheetRow getRow(DateTime date) {
        for (StatSheetRow row : rows) {
            if (row.contains(date)) {
                return row;
            }
        }

        throw new IllegalArgumentException("No row found for date " + date);
    }

    private StatSheetColumn getColumn(String title) {
        StatSheetColumn column = safeGetColumn(title);

        if (column == null) {
            throw new IllegalArgumentException("No column found for title " + title);
        }

        return column;
    }

    private List<StatSheetColumn> getColumns(String... titles) {
        List<StatSheetColumn> columns = new ArrayList<StatSheetColumn>();

        for (String title : titles) {
            columns.add(getColumn(title));
        }

        return columns;
    }

    private StatSheetColumn safeGetColumn(String title) {
        for (StatSheetColumn column : columns) {
            if (Objects.equal(column.getTitle(), title)) {
                return column;
            }
        }

        return null;
    }

    private void setValue(StatSheetRow row, StatSheetColumn column, Integer value) {
        cells.put(new CellKey(row, column), value);
    }

    private Integer getValue(StatSheetRow row, StatSheetColumn column) {
        Integer value = cells.get(new CellKey(row, column));

        return value == null ? defaultValue : value;
    }

    private void dropColumn(StatSheetColumn column) {
        columns.remove(column);

        for (StatSheetRow row : rows) {
            cells.remove(new CellKey(row, column));
        }
    }
}
