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

package com.github.dbourdette.otto.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.github.dbourdette.otto.web.util.Pair;
import com.google.common.base.Objects;

/**
 * Class containing graph data.
 * It is able to build csv exports or js inserts for web pages.
 *
 * @author damien bourdette
 * @version \$Revision$
 */
public class Report {
    private static final int DEFAULT_WIDTH = 1200;

    private static final int DEFAULT_HEIGHT = 500;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private static final String[] GOOGLE_COLORS = {"99B3FF", "FF4242", "BBDDBB", "B399FF", "E699FF", "6AB4B4", "FF99E6", "DDCCBB", "99E6FF",
            "DDDDBB", "CCFF99", "FF99B3", "FF80FF", "99FFE6", "DDBBBB", "DDBBDD", "FFD65C",
            "FFB399", "99FFB3", "FFFF99", "CC99FF", "B3FF99", "E6FF99", "FFE699", "FF66A3"};

    private final Duration FIVE_MINUTES = Duration.standardMinutes(5);

    private final List<ReportRow> rows = new ArrayList<ReportRow>();

    private List<ReportColumn> columns = new ArrayList<ReportColumn>();

    private final Map<CellKey, Integer> cells = new HashMap<CellKey, Integer>();

    private Integer defaultValue = 0;

    public Report() {
    }

    public void createRows(ReportPeriod period) {
        setRows(period.getInterval(), period.getStepDuration());
    }

    public Report rows(Interval interval) {
        return rows(interval, FIVE_MINUTES);
    }

    public Report rows(Interval interval, Duration step) {
        setRows(interval, step);

        return this;
    }

    public void dropColumn(String title) {
        dropColumn(getColumn(title));
    }

    public void ensureColumnExists(String title) {
        if (!hasColumn(title)) {
            columns.add(new ReportColumn(title));
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

    public List<String> getColumnTitles() {
        List<String> titles = new ArrayList<String>();

        for (ReportColumn column : columns) {
            titles.add(column.getTitle());
        }

        return titles;
    }

    public void setRows(Interval interval) {
        setRows(interval, FIVE_MINUTES);
    }

    public void setRows(Interval interval, Duration step) {
        rows.clear();

        DateTime current = interval.getStart();

        while (current.isBefore(interval.getEnd())) {
            rows.add(new ReportRow(new Interval(current, step)));

            current = current.plus(step);
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
        ReportRow row = getRow(date);
        ReportColumn column = getColumn(columnTitle);

        setValue(row, column, value);
    }

    public void increaseValue(String columnTitle, DateTime date) {
        increaseValue(columnTitle, date, 1);
    }

    public void increaseValue(String columnTitle, DateTime date, Integer value) {
        if (value == null) {
            return;
        }

        ReportRow row = getRow(date);
        ReportColumn column = getColumn(columnTitle);

        Integer cellValue = cells.get(new CellKey(row, column));

        if (cellValue == null) {
            cellValue = 0;
        }

        cells.put(new CellKey(row, column), cellValue + value);
    }

    public Integer getValue(String columnTitle, int rowIndex) {
        ReportRow row = rows.get(rowIndex);
        ReportColumn column = getColumn(columnTitle);

        return getValue(row, column);
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
     * @param columnTitle column on which sum is operated
     */
    public void cumulate(String columnTitle) {
        ReportColumn column = getColumn(columnTitle);

        Integer sum = 0;

        for (ReportRow row : rows) {
            Integer value = getValue(row, column);

            if (value != null) {
                sum += value;
            }

            setValue(row, column, sum);
        }
    }

    public int getSum(String columnTitle) {
        ReportColumn column = getColumn(columnTitle);

        int sum = 0;

        for (ReportRow row : rows) {
            sum += getValue(row, column);
        }

        return sum;
    }

    public List<Pair> getSums() {
        List<Pair> values = new ArrayList<Pair>();

        for (String column : getColumnTitles()) {
            values.add(new Pair(column, getSum(column)));
        }

        return values;
    }

    /**
     * Keeps only count top columns from all current columns.
     */
    public void top(int count) {
        if (count >= columns.size()) {
            return;
        }

        List<ColumnSum> sums = sortedSums();

        List<ColumnSum> columnsToDrop = sums.subList(0, sums.size() - count);

        for (ColumnSum sum : columnsToDrop) {
            dropColumn(sum.getColumn());
        }
    }

    public void sortAlphabetically() {
        Collections.sort(columns, new Comparator<ReportColumn>() {
            @Override
            public int compare(ReportColumn o1, ReportColumn o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
    }

    public void sortBySum() {
        List<ColumnSum> sums = sortedSums();
        Collections.reverse(sums);

        columns.clear();

        for (ColumnSum sum : sums) {
            columns.add(sum.getColumn());
        }
    }

    public String toCsv() {
        StringBuilder builder = new StringBuilder();

        builder.append("startDate,endDate");

        for (ReportColumn column : columns) {
            builder.append(",");
            builder.append(column.getTitle());
        }

        builder.append("\n");

        for (ReportRow row : rows) {
            builder.append(DATE_TIME_FORMATTER.print(row.getStartDate()));
            builder.append(",");
            builder.append(DATE_TIME_FORMATTER.print(row.getEndDate()));

            for (ReportColumn column : columns) {
                builder.append(",");
                builder.append(getValue(row, column));
            }

            builder.append("\n");
        }

        return builder.toString();
    }

    public String toGoogleChartHtml(Integer width, Integer height) {
        String elementId = "chart_div_" + UUID.randomUUID().toString();

        return toGoogleHtml(elementId, toGoogleChartJs(elementId, width, height));
    }

    /**
     * Produces google chart js code.
     */
    public String toGoogleChartJs(String elementId, Integer width, Integer height) {
        StringBuilder builder = new StringBuilder();

        builder.append("var data = new google.visualization.DataTable();\n");
        builder.append("data.addColumn('date', 'Date');\n");

        for (ReportColumn column : columns) {
            builder.append("data.addColumn('number', '" + StringEscapeUtils.escapeJavaScript(column.getTitle())
                    + "');\n");
        }

        builder.append("data.addRows(" + rows.size() + ");\n");

        int rowIndex = 0;
        int columnIndex = 0;

        for (ReportRow row : rows) {
            columnIndex = 0;

            builder.append("data.setValue(" + rowIndex + ", " + columnIndex + ", new Date("
                    + row.getStartDate().getMillis() + "));\n");

            for (ReportColumn column : columns) {
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
                + (height == null ? DEFAULT_HEIGHT : height) + ", backgroundColor:'#FAFAFA'});");

        return builder.toString();
    }

    public String toGooglePieHtml(Integer width, Integer height) {
        String elementId = "chart_div_" + UUID.randomUUID().toString();

        return toGoogleHtml(elementId, toGooglePiejs(elementId, width, height));
    }

    public String toGooglePiejs(String elementId, Integer width, Integer height) {
        StringBuilder builder = new StringBuilder();

        builder.append("var data = new google.visualization.DataTable();\n");

        builder.append("data.addColumn('string', 'entry');\n");
        builder.append("data.addColumn('number', 'sum');\n");

        builder.append("data.addRows(" + columns.size() + ");\n");

        int columnIndex = 0;

        for (ReportColumn column : columns) {
            builder.append("data.setValue(" + columnIndex + ", 0, '" + StringEscapeUtils.escapeJavaScript(column.getTitle()) + "');\n");
            builder.append("data.setValue(" + columnIndex + ", 1, " + getSum(column.getTitle()) + ");\n");

            columnIndex++;
        }

        builder.append("var chart = new google.visualization.PieChart(document.getElementById('" + elementId + "'));\n");
        builder.append("chart.draw(data, {width: " + (width == null ? DEFAULT_WIDTH : width) + ", height: "
                + (height == null ? DEFAULT_HEIGHT : height) + ", backgroundColor:'#FAFAFA'});");

        return builder.toString();
    }

    public String toGoogleHtml(String elementId, String js) {
        StringBuilder builder = new StringBuilder();

        builder.append("<div id=\"" + elementId + "\"></div>");

        builder.append("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});");
        builder.append("google.setOnLoadCallback(drawChart);");
        builder.append("function drawChart() {");

        builder.append(js);

        builder.append("}");
        builder.append("</script>");

        return builder.toString();
    }

    /**
     * Produces google image call.
     */
    public Map<String, String> toGoogleImageParams(Integer width, Integer height) {
        Map<String, String> params = new HashMap<String, String>();

        int maxValue = getMaxValue();

        params.put("cht", "lc");
        params.put("chxt", "x,y");
        params.put("chxr", "1,0," + maxValue);
        params.put("chds", "0," + maxValue);
        params.put("chs", width + "x" + height);
        params.put("chf", "bg,s,FAFAFA");

        int columnIndex = 0;
        String colors = "";
        for (ReportColumn column : columns) {
            colors += GOOGLE_COLORS[columnIndex % GOOGLE_COLORS.length];

            if (columnIndex != columns.size() - 1) {
                colors += ",";
            }

            columnIndex++;
        }
        params.put("chco", colors);

        DateTimeFormatter dayFormatter = DateTimeFormat.forPattern("yyyy MM dd");
        String labels = "0:|";
        for (ReportRow row : rows) {
            DateTime startDate = row.getStartDate();

            if (startDate.getSecondOfDay() == 0) {
                labels += dayFormatter.print(startDate);
                labels += "|";
            }
        }
        params.put("chxl", labels);

        columnIndex = 0;
        String data = "t:";
        for (ReportColumn column : columns) {
            int rowIndex = 0;

            for (ReportRow row : rows) {
                data += getValue(row, column);

                if (rowIndex != rows.size() - 1) {
                    data += ",";
                }

                rowIndex++;
            }

            if (columnIndex != columns.size() - 1) {
                data += "|";
            }

            columnIndex++;
        }
        params.put("chd", data);

        return params;
    }

    /**
     * Gets info about curves displayed in google image
     */
    public List<Map<String, String>> getGoogleImageCurves() {
        List<Map<String, String>> curves = new ArrayList<Map<String, String>>();

        for (ReportColumn column : columns) {
            Map<String, String> curve = new HashMap<String, String>();

            curve.put("color", GOOGLE_COLORS[curves.size() % GOOGLE_COLORS.length]);
            curve.put("name", column.getTitle());

            curves.add(curve);
        }

        return curves;
    }

    public String toHtmlTable() {
        StringBuilder builder = new StringBuilder();

        builder.append("<table>");

        builder.append("<tr>");

        builder.append("<td>");
        builder.append("date");
        builder.append("</td>");

        for (ReportColumn column : columns) {
            builder.append("<td>");
            builder.append(column.getTitle());
            builder.append("</td>");
        }

        builder.append("</tr>");

        for (ReportRow row : rows) {
            builder.append("<tr>");

            builder.append("<td>");
            builder.append(row.getStartDate());
            builder.append("</td>");

            for (ReportColumn column : columns) {
                builder.append("<td>");
                builder.append(getValue(row, column));
                builder.append("</td>");
            }

            builder.append("</tr>");
        }

        builder.append("</table>");

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

    private ReportRow getRow(DateTime date) {
        for (ReportRow row : rows) {
            if (row.contains(date)) {
                return row;
            }
        }

        throw new IllegalArgumentException("No row found for date " + date);
    }

    private ReportColumn getColumn(String title) {
        ReportColumn column = safeGetColumn(title);

        if (column == null) {
            throw new IllegalArgumentException("No column found for title " + title);
        }

        return column;
    }

    private List<ReportColumn> getColumns(String... titles) {
        List<ReportColumn> columns = new ArrayList<ReportColumn>();

        for (String title : titles) {
            columns.add(getColumn(title));
        }

        return columns;
    }

    private ReportColumn safeGetColumn(String title) {
        for (ReportColumn column : columns) {
            if (Objects.equal(column.getTitle(), title)) {
                return column;
            }
        }

        return null;
    }

    private void setValue(ReportRow row, ReportColumn column, Integer value) {
        cells.put(new CellKey(row, column), value);
    }

    private Integer getValue(ReportRow row, ReportColumn column) {
        Integer value = cells.get(new CellKey(row, column));

        return value == null ? defaultValue : value;
    }

    private void dropColumn(ReportColumn column) {
        columns.remove(column);

        for (ReportRow row : rows) {
            cells.remove(new CellKey(row, column));
        }
    }

    private Integer getMaxValue() {
        Integer maxValue = 0;

        for (ReportColumn column : columns) {
            for (ReportRow row : rows) {
                maxValue = Math.max(maxValue, getValue(row, column));
            }
        }

        return maxValue;
    }

    private List<ColumnSum> sortedSums() {
        List<ColumnSum> sums = new ArrayList<ColumnSum>();

        for (ReportColumn column : columns) {
            ColumnSum sum = new ColumnSum(column);

            for (ReportRow row : rows) {
                sum.add(getValue(row, column));
            }

            sums.add(sum);
        }

        Collections.sort(sums);

        return sums;
    }

    private class CellKey {

        private final ReportRow row;

        private final ReportColumn column;

        public CellKey(ReportRow row, ReportColumn column) {
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

        private Report getOuterType() {
            return Report.this;
        }
    }

    private class ColumnSum implements Comparable<ColumnSum> {

        private int sum;

        private final ReportColumn column;

        public ColumnSum(ReportColumn column) {
            this.column = column;
        }

        public void add(Integer value) {
            if (value == null) {
                return;
            }

            sum += value;
        }

        public ReportColumn getColumn() {
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
