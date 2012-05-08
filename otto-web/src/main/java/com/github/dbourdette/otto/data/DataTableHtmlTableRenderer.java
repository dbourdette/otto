package com.github.dbourdette.otto.data;

import org.joda.time.Interval;

/**
 * @author damien bourdette
 */
public class DataTableHtmlTableRenderer implements DataTableRenderer {
    @Override
    public String render(DataTable table) {
        StringBuilder builder = new StringBuilder();

        builder.append("<table>");

        builder.append("<tr>");

        builder.append("<td>");
        builder.append("date");
        builder.append("</td>");

        for (String column : table.getColumns()) {
            builder.append("<td>");
            builder.append(column);
            builder.append("</td>");
        }

        builder.append("</tr>");

        for (Interval row : table.getRows()) {
            builder.append("<tr>");

            builder.append("<td>");
            builder.append(row.getStart());
            builder.append("</td>");

            for (String column : table.getColumns()) {
                builder.append("<td>");
                builder.append(table.getValue(row, column));
                builder.append("</td>");
            }

            builder.append("</tr>");
        }

        builder.append("</table>");

        return builder.toString();
    }
}
