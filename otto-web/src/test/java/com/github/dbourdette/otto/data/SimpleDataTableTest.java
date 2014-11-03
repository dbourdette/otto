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

package com.github.dbourdette.otto.data;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class SimpleDataTableTest {

    private SimpleDataTable table;

    private final DateTime now = new DateTime();

    private static final String USER_LOGIN = "user login";

    private static final String USER_LOGOUT = "user logout";

    @Before
    public void init() {
        table = new SimpleDataTable();
        table.ensureColumnExists(USER_LOGIN);
    }

    @Test
    public void graph() {
        DateTime to = new DateTime();
        DateTime from = to.minusDays(1);

        Assert.assertEquals(from, SimpleDataTable.forInterval(from, to).getStartDate());
    }

    @Test
    public void ensureColumnExists() {
        table.ensureColumnExists(USER_LOGIN);
        table.ensureColumnExists(USER_LOGIN);

        Assert.assertTrue(table.hasColumn(USER_LOGIN));
        Assert.assertEquals(1, table.getColumnCount());
    }

    @Test
    public void hasColumn() {
        Assert.assertTrue(table.hasColumn(USER_LOGIN));
        Assert.assertFalse(table.hasColumn("not a column"));
    }

    @Test
    public void setRows() {
        table.setupRows(new Interval(now.minusHours(1), now));

        Assert.assertEquals("There should be 12 rows", 12, table.getRowCount());
        Assert.assertEquals("Third row has incorrect bounds", now.minusMinutes(50), table.getRow(2).getStart());
    }

    @Test
    public void getStartDate() {
        table.setupRows(new Interval(now.minusHours(1), now));

        Assert.assertEquals(now.minusHours(1), table.getStartDate());
    }

    @Test
    public void getEndDate() {
        table.setupRows(new Interval(now.minusHours(1), now));

        Assert.assertEquals(now, table.getEndDate());
    }

    @Test
    public void addRowsAutomaticallyComputeInterval() {
        table.setupRows(new Interval(now.minusHours(1), now));

        Assert.assertEquals("There should be 12 rows", 12, table.getRowCount());
        Assert.assertEquals("Third row has incorrect bounds", now.minusMinutes(50), table.getRow(2).getStart());
    }

    @Test
    public void setValue() {
        table.setupRows(new Interval(now.minusMinutes(10), now));

        table.setValue(USER_LOGIN, now.minusMinutes(6), 2);
        table.setValue(USER_LOGIN, now.minusMinutes(2), 10);

        Assert.assertEquals("First cell should contains 2", (Integer) 2, table.getValue(USER_LOGIN, 0));
        Assert.assertEquals("Second cell should contains 10", (Integer) 10, table.getValue(USER_LOGIN, 1));
    }

    @Test
    public void setDefaultValue() {
        table.setupRows(new Interval(now.minusMinutes(10), now));

        table.setDefaultValue(null);

        Assert.assertNull("Value should be null", table.getValue(USER_LOGIN, 0));

        table.setDefaultValue(1);

        Assert.assertEquals("Value should be 1", (Integer) 1, table.getValue(USER_LOGIN, 0));
    }

    @Test
    public void increaseValue() {
        table.setupRows(new Interval(now.minusMinutes(10), now));

        table.increaseValue(USER_LOGIN, now.minusMinutes(2));

        Assert.assertEquals("Cell should contains 1", (Integer) 1, table.getValue(USER_LOGIN, 1));

        table.increaseValue(USER_LOGIN, now.minusMinutes(2), 10);

        Assert.assertEquals("Cell should contains 11", (Integer) 11, table.getValue(USER_LOGIN, 1));

        table.increaseValue(USER_LOGIN, now.minusMinutes(2), null);

        Assert.assertEquals("Cell should contains 11", (Integer) 11, table.getValue(USER_LOGIN, 1));
    }

    @Test
    public void top() {
        table.setupRows(new Interval(now.minusMinutes(10), now));

        table.dropColumn(USER_LOGIN);
        table.ensureColumnExists("col1", "col2", "col3");

        table.increaseValue("col1", now.minusMinutes(2), 4);
        table.increaseValue("col2", now.minusMinutes(3), 10);
        table.increaseValue("col3", now.minusMinutes(7), 2);
        table.increaseValue("col1", now.minusMinutes(7), 7);

        table.top(2);

        Assert.assertEquals("There should be only 2 columns", 2, table.getColumnCount());
        Assert.assertEquals("Cell should contains 4", (Integer) 4, table.getValue("col1", 1));
        Assert.assertEquals("Cell should contains 10", (Integer) 10, table.getValue("col2", 1));
    }

    @Test
    public void sortAlphabetically() {
        table.setupRows(new Interval(now.minusMinutes(10), now));

        table.dropColumn(USER_LOGIN);
        table.ensureColumnExists("col2", "col3", "col1");

        table.sortAlphabetically();

        Assert.assertEquals("first column is col1", "col1", table.getColumns().get(0));
        Assert.assertEquals("last column is col3", "col3", table.getColumns().get(2));
    }

    @Test
    public void sortBySum() throws IOException {
        table.ensureColumnExists(USER_LOGOUT);

        DateTime dateTime = new DateTime(2010, 10, 10, 0, 0, 0, 0, DateTimeZone.forID("+02:00"));

        table.setDefaultValue(0);
        table.setupRows(new Interval(dateTime.minusMinutes(15), dateTime));

        table.setValue(USER_LOGIN, dateTime.minusMinutes(6), 2);
        table.setValue(USER_LOGIN, dateTime.minusMinutes(2), 2);
        table.setValue(USER_LOGOUT, dateTime.minusMinutes(2), 5);

        Assert.assertEquals(USER_LOGIN, table.getColumns().get(0));

        table.sortBySum();

        Assert.assertEquals(USER_LOGOUT, table.getColumns().get(0));
    }
}
