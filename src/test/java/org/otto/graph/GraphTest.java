package org.otto.graph;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

public class GraphTest {

    private Graph graph;

    private final DateTime now = new DateTime();

    private static final String USER_LOGIN = "user login";

    @Before
    public void init() {
        graph = new Graph();
        graph.ensureColumnExists(USER_LOGIN);
    }

    @Test
    public void graph() {
        DateTime end = new DateTime();
        DateTime start = end.minusDays(1);

        Graph graph = new Graph().rows(new Interval(start, end));

        Assert.assertEquals(start, graph.getStartDate());
    }

    @Test
    public void ensureColumnExists() {
        graph.ensureColumnExists(USER_LOGIN);
        graph.ensureColumnExists(USER_LOGIN);

        Assert.assertTrue(graph.hasColumn(USER_LOGIN));
        Assert.assertEquals(1, graph.getColumnCount());
    }

    @Test
    public void hasColumn() {
        Assert.assertTrue(graph.hasColumn(USER_LOGIN));
        Assert.assertFalse(graph.hasColumn("not a column"));
    }

    @Test
    public void setRows() {
        graph.setRows(new Interval(now.minusHours(1), now));

        Assert.assertEquals("There should be 12 rows", 12, graph.getRowCount());
        Assert.assertEquals("Third row has incorrect bounds", now.minusMinutes(50), graph.getRowStartDate(2));
    }

    @Test
    public void getStartDate() {
        graph.setRows(new Interval(now.minusHours(1), now));

        Assert.assertEquals(now.minusHours(1), graph.getStartDate());
    }

    @Test
    public void getEndDate() {
        graph.setRows(new Interval(now.minusHours(1), now));

        Assert.assertEquals(now, graph.getEndDate());
    }

    @Test
    public void addRowsAutomaticallyComputeInterval() {
        graph.setRows(new Interval(now.minusHours(1), now));

        Assert.assertEquals("There should be 12 rows", 12, graph.getRowCount());
        Assert.assertEquals("Third row has incorrect bounds", now.minusMinutes(50), graph.getRowStartDate(2));
    }

    @Test
    public void setValue() {
        graph.setRows(new Interval(now.minusMinutes(10), now));

        graph.setValue(USER_LOGIN, now.minusMinutes(6), 2);
        graph.setValue(USER_LOGIN, now.minusMinutes(2), 10);

        Assert.assertEquals("First cell should contains 2", (Integer) 2, graph.getValue(USER_LOGIN, 0));
        Assert.assertEquals("Second cell should contains 10", (Integer) 10, graph.getValue(USER_LOGIN, 1));
    }

    @Test
    public void setDefaultValue() {
    	graph.setRows(new Interval(now.minusMinutes(10), now));

        graph.setDefaultValue(null);

        Assert.assertNull("Value should be null", graph.getValue(USER_LOGIN, 0));

        graph.setDefaultValue(1);

        Assert.assertEquals("Value should be 1", (Integer) 1, graph.getValue(USER_LOGIN, 0));
    }

    @Test
    public void increaseValue() {
    	graph.setRows(new Interval(now.minusMinutes(10), now));

        graph.increaseValue(USER_LOGIN, now.minusMinutes(2));

        Assert.assertEquals("Cell should contains 1", (Integer) 1, graph.getValue(USER_LOGIN, 1));

        graph.increaseValue(USER_LOGIN, now.minusMinutes(2), 10);

        Assert.assertEquals("Cell should contains 11", (Integer) 11, graph.getValue(USER_LOGIN, 1));

        graph.increaseValue(USER_LOGIN, now.minusMinutes(2), null);

        Assert.assertEquals("Cell should contains 11", (Integer) 11, graph.getValue(USER_LOGIN, 1));
    }

    @Test
    public void cumulate() {
    	graph.setRows(new Interval(now.minusMinutes(10), now));

        graph.increaseValue(USER_LOGIN, now.minusMinutes(2));
        graph.increaseValue(USER_LOGIN, now.minusMinutes(3), 10);
        graph.increaseValue(USER_LOGIN, now.minusMinutes(7), 2);

        graph.cumulate(USER_LOGIN);

        Assert.assertEquals("Cell should contains 2", (Integer) 2, graph.getValue(USER_LOGIN, 0));
        Assert.assertEquals("Cell should contains 13", (Integer) 13, graph.getValue(USER_LOGIN, 1));
    }

    @Test
    public void top() {
    	graph.setRows(new Interval(now.minusMinutes(10), now));

        graph.dropColumn(USER_LOGIN);
        graph.ensureColumnsExists("col1", "col2", "col3");

        graph.increaseValue("col1", now.minusMinutes(2), 4);
        graph.increaseValue("col2", now.minusMinutes(3), 10);
        graph.increaseValue("col3", now.minusMinutes(7), 2);
        graph.increaseValue("col1", now.minusMinutes(7), 7);

        graph.top(2, "col1", "col2", "col3");

        Assert.assertEquals("There should be only 2 columns", 2, graph.getColumnCount());
        Assert.assertEquals("Cell should contains 4", (Integer) 4, graph.getValue("col1", 1));
        Assert.assertEquals("Cell should contains 10", (Integer) 10, graph.getValue("col2", 1));
    }

    @Test
    public void toCsv() throws IOException {
        DateTime dateTime = new DateTime(2010, 10, 10, 0, 0, 0, 0);

        graph.setDefaultValue(0);

        graph.setRows(new Interval(dateTime.minusMinutes(15), dateTime));

        graph.setValue(USER_LOGIN, dateTime.minusMinutes(6), 2);
        graph.setValue(USER_LOGIN, dateTime.minusMinutes(2), 10);

        String expected = IOUtils.toString(getClass().getResourceAsStream("GraphTest-toCsv.txt"));

        Assert.assertEquals("toCsv is incorrect", expected, graph.toCsv());
    }

    @Test
    public void toGoogleJs() throws IOException {
        DateTime dateTime = new DateTime(2010, 10, 10, 0, 0, 0, 0);

        graph.setDefaultValue(0);
        graph.setRows(new Interval(dateTime.minusMinutes(15), dateTime));

        graph.setValue(USER_LOGIN, dateTime.minusMinutes(6), 2);
        graph.setValue(USER_LOGIN, dateTime.minusMinutes(2), 10);

        String expected = IOUtils.toString(getClass().getResourceAsStream("GraphTest-toGoogleJs.txt"));

        Assert.assertEquals("toGoogleJs is incorrect", expected, graph.toGoogleJs("chart_div", null, null));
    }
}
