package org.otto.graph;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

public class GraphTest {

    private Graph graph;

    private final DateTime now = new DateTime();

    private final Duration FIVE_MINUTES = Duration.standardMinutes(5);

    private static final String USER_LOGIN = "user login";

    @Before
    public void init() {
        graph = new Graph("uber graph");
        graph.ensureColumnExists(USER_LOGIN);
    }

    @Test
    public void graph() {
        DateTime end = new DateTime();
        DateTime start = end.minusDays(1);

        Graph graph = Graph.graph("graph").rows(start, end);

        Assert.assertEquals("graph", graph.getTitle());
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
    public void addRow() {
        graph.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        graph.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));
        graph.addRow(new Interval(now.minusMinutes(20), FIVE_MINUTES));
        graph.addRow(new Interval(now.minusMinutes(15), FIVE_MINUTES));

        Assert.assertEquals("Rows should be sorted", graph.getRowStartDate(0), now.minusMinutes(20));
        Assert.assertEquals("Rows should be sorted", graph.getRowStartDate(1), now.minusMinutes(15));
        Assert.assertEquals("Rows should be sorted", graph.getRowStartDate(2), now.minusMinutes(10));
        Assert.assertEquals("Rows should be sorted", graph.getRowStartDate(3), now.minusMinutes(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRowNull() {
        graph.addRow(null);
    }

    @Test
    public void addRows() {
        graph.addRows(now.minusHours(1), now, FIVE_MINUTES);

        Assert.assertEquals("There should be 12 rows", 12, graph.getRowCount());
        Assert.assertEquals("Third row has incorrect bounds", now.minusMinutes(50), graph.getRowStartDate(2));
    }

    @Test
    public void getStartDate() {
        graph.addRows(now.minusHours(1), now);

        Assert.assertEquals(now.minusHours(1), graph.getStartDate());
    }

    @Test
    public void getEndDate() {
        graph.addRows(now.minusHours(1), now);

        Assert.assertEquals(now, graph.getEndDate());
    }

    @Test
    public void addRowsAutomaticallyComputeInterval() {
        graph.addRows(now.minusHours(1), now);

        Assert.assertEquals("There should be 12 rows", 12, graph.getRowCount());
        Assert.assertEquals("Third row has incorrect bounds", now.minusMinutes(50), graph.getRowStartDate(2));
    }

    @Test
    public void setValue() {
        graph.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        graph.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));

        graph.setValue(USER_LOGIN, now.minusMinutes(6), 2);
        graph.setValue(USER_LOGIN, now.minusMinutes(2), 10);

        Assert.assertEquals("First cell should contains 2", (Integer) 2, graph.getValue(USER_LOGIN, 0));
        Assert.assertEquals("Second cell should contains 10", (Integer) 10, graph.getValue(USER_LOGIN, 1));
    }

    @Test
    public void setDefaultValue() {
        graph.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        graph.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));

        graph.setDefaultValue(null);

        Assert.assertNull("Value should be null", graph.getValue(USER_LOGIN, 0));

        graph.setDefaultValue(1);

        Assert.assertEquals("Value should be 1", (Integer) 1, graph.getValue(USER_LOGIN, 0));
    }

    @Test
    public void increaseValue() {
        graph.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        graph.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));

        graph.increaseValue(USER_LOGIN, now.minusMinutes(2));

        Assert.assertEquals("Cell should contains 1", (Integer) 1, graph.getValue(USER_LOGIN, 1));

        graph.increaseValue(USER_LOGIN, now.minusMinutes(2), 10);

        Assert.assertEquals("Cell should contains 11", (Integer) 11, graph.getValue(USER_LOGIN, 1));

        graph.increaseValue(USER_LOGIN, now.minusMinutes(2), null);

        Assert.assertEquals("Cell should contains 11", (Integer) 11, graph.getValue(USER_LOGIN, 1));
    }

    @Test
    public void cumulate() {
        graph.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        graph.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));

        graph.increaseValue(USER_LOGIN, now.minusMinutes(2));
        graph.increaseValue(USER_LOGIN, now.minusMinutes(3), 10);
        graph.increaseValue(USER_LOGIN, now.minusMinutes(7), 2);

        graph.cumulate(USER_LOGIN);

        Assert.assertEquals("Cell should contains 2", (Integer) 2, graph.getValue(USER_LOGIN, 0));
        Assert.assertEquals("Cell should contains 13", (Integer) 13, graph.getValue(USER_LOGIN, 1));
    }

    @Test
    public void top() {
        graph.addRow(new Interval(now.minusMinutes(10), FIVE_MINUTES));
        graph.addRow(new Interval(now.minusMinutes(5), FIVE_MINUTES));

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

        graph.addRow(new Interval(dateTime.minusMinutes(15), FIVE_MINUTES));
        graph.addRow(new Interval(dateTime.minusMinutes(10), FIVE_MINUTES));
        graph.addRow(new Interval(dateTime.minusMinutes(5), FIVE_MINUTES));

        graph.setValue(USER_LOGIN, dateTime.minusMinutes(6), 2);
        graph.setValue(USER_LOGIN, dateTime.minusMinutes(2), 10);

        String expected = IOUtils.toString(getClass().getResourceAsStream("GraphTest-toCsv.txt"));

        Assert.assertEquals("toCsv is incorrect", expected, graph.toCsv());
    }

    @Test
    public void toJs() throws IOException {
        DateTime dateTime = new DateTime(2010, 10, 10, 0, 0, 0, 0);

        graph.setDefaultValue(0);
        graph.addRow(new Interval(dateTime.minusMinutes(15), FIVE_MINUTES));
        graph.addRow(new Interval(dateTime.minusMinutes(10), FIVE_MINUTES));
        graph.addRow(new Interval(dateTime.minusMinutes(5), FIVE_MINUTES));

        graph.setValue(USER_LOGIN, dateTime.minusMinutes(6), 2);
        graph.setValue(USER_LOGIN, dateTime.minusMinutes(2), 10);

        String expected = IOUtils.toString(getClass().getResourceAsStream("GraphTest-toJs.txt"));

        Assert.assertEquals("toJs is incorrect", expected, graph.toJs("chart_div", null, null));
    }
}
