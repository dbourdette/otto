package com.github.dbourdette.otto.data;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author damien bourdette
 */
public class DataTableGooglePieRenderer extends DataTableGoogleRenderer {
    @Override
    public String toGooglejs(DataTable table, String elementId) {
        StringBuilder builder = new StringBuilder();

        builder.append("var data = new google.visualization.DataTable();\n");

        builder.append("data.addColumn('string', 'entry');\n");
        builder.append("data.addColumn('number', 'sum');\n");

        builder.append("data.addRows(" + table.getColumns().size() + ");\n");

        int columnIndex = 0;

        for (String column : table.getColumns()) {
            builder.append("data.setValue(" + columnIndex + ", 0, '" + StringEscapeUtils.escapeJavaScript(column) + "');\n");
            builder.append("data.setValue(" + columnIndex + ", 1, " + table.getSum(column) + ");\n");

            columnIndex++;
        }

        builder.append("var chart = new google.visualization.PieChart(document.getElementById('" + elementId + "'));\n");
        builder.append("chart.draw(data, {width: " + WIDTH + ", height: " + HEIGHT + ", backgroundColor:'white'});");

        return builder.toString();
    }
}
