package org.otto.graph;

import java.io.IOException;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

public class StatSheetTest {

    private StatSheet sheet;

    private final DateTime now = new DateTime();

    private final Duration FIVE_MINUTES = Duration.standardMinutes(5);

    private static final String USER_LOGIN = "user login";

    @Before
    public void init() {
        sheet = new StatSheet("uber sheet");
        sheet.ensureColumnExists(USER_LOGIN);
    }

    @Test
    public void sheet() {
        DateTime end = new DateTime();
        DateTime start = end.minusDays(1);

        StatSheet sheet = StatSheet.sheet("stat sheet").rows(start, end);

        Assert.assertEquals("stat sheet", sheet.getTitle());
        Assert.assertEquals(start, sheet.getStartDate());
    }

    @Test
    public void ensureColumnExists() {
        sheet.ensureColumnExists(USER_LOGIN);
        sheet.ensureColumnExists(USER_LOGIN);

        Assert.assertTrue(sheet.hasColumn(USER_LOGIN));
        Assert.assertEquals(1, sheet.getColumnCount());
    }

    @Test
    public void hasColumn() {
        Assert.assertTrue(sheet.hasColumn(USER_LOGIN));
        Assert.assertFalse(sheet.hasColumn("not a column"));
    }

    @Test
    public void addRow() {
        sheet.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        sheet.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));
        sheet.addRow(new Interval(now.minusMinutes(20), FIVE_MINUTES));
        sheet.addRow(new Interval(now.minusMinutes(15), FIVE_MINUTES));

        Assert.assertEquals("Rows should be sorted", sheet.getRowStartDate(0), now.minusMinutes(20));
        Assert.assertEquals("Rows should be sorted", sheet.getRowStartDate(1), now.minusMinutes(15));
        Assert.assertEquals("Rows should be sorted", sheet.getRowStartDate(2), now.minusMinutes(10));
        Assert.assertEquals("Rows should be sorted", sheet.getRowStartDate(3), now.minusMinutes(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRowNull() {
        sheet.addRow(null);
    }

    @Test
    public void addRows() {
        sheet.addRows(now.minusHours(1), now, FIVE_MINUTES);

        Assert.assertEquals("There should be 12 rows", 12, sheet.getRowCount());
        Assert.assertEquals("Third row has incorrect bounds", now.minusMinutes(50), sheet.getRowStartDate(2));
    }

    @Test
    public void getStartDate() {
        sheet.addRows(now.minusHours(1), now);

        Assert.assertEquals(now.minusHours(1), sheet.getStartDate());
    }

    @Test
    public void getEndDate() {
        sheet.addRows(now.minusHours(1), now);

        Assert.assertEquals(now, sheet.getEndDate());
    }

    @Test
    public void addRowsAutomaticallyComputeInterval() {
        sheet.addRows(now.minusHours(1), now);

        Assert.assertEquals("There should be 12 rows", 12, sheet.getRowCount());
        Assert.assertEquals("Third row has incorrect bounds", now.minusMinutes(50), sheet.getRowStartDate(2));
    }

    @Test
    public void setValue() {
        sheet.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        sheet.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));

        sheet.setValue(USER_LOGIN, now.minusMinutes(6), 2);
        sheet.setValue(USER_LOGIN, now.minusMinutes(2), 10);

        Assert.assertEquals("First cell should contains 2", (Integer) 2, sheet.getValue(USER_LOGIN, 0));
        Assert.assertEquals("Second cell should contains 10", (Integer) 10, sheet.getValue(USER_LOGIN, 1));
    }

    @Test
    public void setDefaultValue() {
        sheet.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        sheet.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));

        sheet.setDefaultValue(null);

        Assert.assertNull("Value should be null", sheet.getValue(USER_LOGIN, 0));

        sheet.setDefaultValue(1);

        Assert.assertEquals("Value should be 1", (Integer) 1, sheet.getValue(USER_LOGIN, 0));
    }

    @Test
    public void increaseValue() {
        sheet.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        sheet.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));

        sheet.increaseValue(USER_LOGIN, now.minusMinutes(2));

        Assert.assertEquals("Cell should contains 1", (Integer) 1, sheet.getValue(USER_LOGIN, 1));

        sheet.increaseValue(USER_LOGIN, now.minusMinutes(2), 10);

        Assert.assertEquals("Cell should contains 11", (Integer) 11, sheet.getValue(USER_LOGIN, 1));

        sheet.increaseValue(USER_LOGIN, now.minusMinutes(2), null);

        Assert.assertEquals("Cell should contains 11", (Integer) 11, sheet.getValue(USER_LOGIN, 1));
    }

    @Test
    public void cumulate() {
        sheet.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        sheet.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));

        sheet.increaseValue(USER_LOGIN, now.minusMinutes(2));
        sheet.increaseValue(USER_LOGIN, now.minusMinutes(3), 10);
        sheet.increaseValue(USER_LOGIN, now.minusMinutes(7), 2);

        sheet.cumulate(USER_LOGIN);

        Assert.assertEquals("Cell should contains 2", (Integer) 2, sheet.getValue(USER_LOGIN, 0));
        Assert.assertEquals("Cell should contains 13", (Integer) 13, sheet.getValue(USER_LOGIN, 1));
    }

    @Test
    public void top() {
        sheet.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        sheet.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));

        sheet.dropColumn(USER_LOGIN);
        sheet.ensureColumnsExists("col1", "col2", "col3");

        sheet.increaseValue("col1", now.minusMinutes(2), 4);
        sheet.increaseValue("col2", now.minusMinutes(3), 10);
        sheet.increaseValue("col3", now.minusMinutes(7), 2);
        sheet.increaseValue("col1", now.minusMinutes(7), 7);

        sheet.top(2, "col1", "col2", "col3");

        Assert.assertEquals("There should be only 2 columns", 2, sheet.getColumnCount());
        Assert.assertEquals("Cell should contains 4", (Integer) 4, sheet.getValue("col1", 1));
        Assert.assertEquals("Cell should contains 10", (Integer) 10, sheet.getValue("col2", 1));
    }

    @Test
    public void toCsv() throws IOException {
        DateTime dateTime = new DateTime(2010, 10, 10, 0, 0, 0, 0);

        sheet.setDefaultValue(0);

        sheet.addRow(new Interval(dateTime.minusMinutes(15), FIVE_MINUTES));
        sheet.addRow(new Interval(dateTime.minusMinutes(10), FIVE_MINUTES));
        sheet.addRow(new Interval(dateTime.minusMinutes(5), FIVE_MINUTES));

        sheet.setValue(USER_LOGIN, dateTime.minusMinutes(6), 2);
        sheet.setValue(USER_LOGIN, dateTime.minusMinutes(2), 10);

        String expected = IOUtils.toString(getClass().getResourceAsStream("StatSheetTest-toCsv.txt"));

        Assert.assertEquals("toCsv is incorrect", expected, sheet.toCsv());
    }

    @Test
    public void toJs() throws IOException {
        DateTime dateTime = new DateTime(2010, 10, 10, 0, 0, 0, 0);

        sheet.setDefaultValue(0);
        sheet.addRow(new Interval(dateTime.minusMinutes(15), FIVE_MINUTES));
        sheet.addRow(new Interval(dateTime.minusMinutes(10), FIVE_MINUTES));
        sheet.addRow(new Interval(dateTime.minusMinutes(5), FIVE_MINUTES));

        sheet.setValue(USER_LOGIN, dateTime.minusMinutes(6), 2);
        sheet.setValue(USER_LOGIN, dateTime.minusMinutes(2), 10);

        String expected = IOUtils.toString(getClass().getResourceAsStream("StatSheetTest-toJs.txt"));

        Assert.assertEquals("toJs is incorrect", expected, sheet.toJs("chart_div", null, null));
    }
}
