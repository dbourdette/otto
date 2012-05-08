package com.github.dbourdette.otto.data;

import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author damien bourdette
 */
public class DataTableCsvRenderer implements DataTableRenderer {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Override
    public String render(DataTable table) {
        StringBuilder builder = new StringBuilder();

        builder.append("startDate,endDate");

        for (String column : table.getColumns()) {
            builder.append(",");
            builder.append(column);
        }

        builder.append("\n");

        for (Interval row : table.getRows()) {
            builder.append(DATE_TIME_FORMATTER.print(row.getStart()));
            builder.append(",");
            builder.append(DATE_TIME_FORMATTER.print(row.getEnd()));

            for (String column : table.getColumns()) {
                builder.append(",");
                builder.append(table.getValue(row, column));
            }

            builder.append("\n");
        }

        return builder.toString();
    }
}
