package com.github.dbourdette.otto.data;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.Duration;
import org.joda.time.Interval;

/**
 * @author damien bourdette
 */
public class DataTableGoogleChartRenderer extends DataTableGoogleRenderer {
    @Override
    protected String toGooglejs(DataTable table, String elementId) {
        StringBuilder builder = new StringBuilder();

        builder.append("var data = new google.visualization.DataTable();\n");
        builder.append("data.addColumn('date', 'Date');\n");

        for (String column : table.getColumns()) {
            builder.append("data.addColumn('number', '" + StringEscapeUtils.escapeJavaScript(column) + "');\n");
        }

        builder.append("data.addRows(" + table.getRows().size() + ");\n");

        int rowIndex = 0;
        int columnIndex = 0;

        for (Interval row : table.getRows()) {
            columnIndex = 0;

            builder.append("data.setValue(" + rowIndex + ", " + columnIndex + ", new Date(" + row.getStart().getMillis() + "));\n");

            for (String column : table.getColumns()) {
                columnIndex++;

                builder.append("data.setValue(" + rowIndex + ", " + columnIndex + ", " + table.getValue(row, column) + ");\n");
            }

            rowIndex++;
        }

        builder.append("var formatter = new google.visualization.DateFormat({pattern: '" + getDatePattern(table) + "'});\n");
        builder.append("formatter.format(data, 0);\n");

        builder.append("var chart = new google.visualization.LineChart(document.getElementById('" + elementId
                + "'));\n");
        builder.append("chart.draw(data, {width: " + WIDTH + ", height: " + HEIGHT + ", backgroundColor:'white'});");

        return builder.toString();

    }

    private String getDatePattern(DataTable table) {
        List<Interval> intervals = table.getRows();

        if (intervals.size() == 0) {
            return "dd MMM yyyy";
        }

        if (intervals.get(0).toDuration().isShorterThan(Duration.standardDays(1))) {
            return "dd MM yyyy HH:mm";
        } else {
            return "dd MMM yyyy";
        }
    }
}
