package com.github.dbourdette.otto.data;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

/**
 * @author damien bourdette
 */
public class DataTableGoogleChartRendererTest {
    private DataTableGoogleChartRenderer renderer = new DataTableGoogleChartRenderer();

    private SimpleDataTable table;

    DateTime dateTime = new DateTime(2010, 10, 10, 0, 0, 0, 0, DateTimeZone.forID("+02:00"));

    @Before
    public void init() {
        table = SimpleDataTable.forInterval(dateTime.minusMinutes(15), dateTime);
        table.setDefaultValue(0);
        table.ensureColumnExists("login");
    }

    @Test
    public void toGoogleChartJs() throws IOException {
        table.setValue("login", dateTime.minusMinutes(6), 2);
        table.setValue("login", dateTime.minusMinutes(2), 10);

        String expected = IOUtils.toString(getClass().getResourceAsStream("DataTableGoogleChartRendererTest.txt"));

        expected = StringUtils.replace(expected, "\r\n", "\n");

        Assert.assertEquals("toGoogleJs is incorrect", expected, renderer.toGooglejs(table, "chart_div"));
    }
}
