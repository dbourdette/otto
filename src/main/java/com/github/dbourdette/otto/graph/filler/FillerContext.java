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

package com.github.dbourdette.otto.graph.filler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.DBObject;

/**
 * @author damien bourdette
 */
public class FillerContext {
    private DBObject event;

    private ColumnValue columnValue = new ColumnValue();

    private List<FillerContext> subContextes;

    public FillerContext(DBObject event) {
        this.event = event;
    }

    public Object get(String key) {
        return event.get(key);
    }

    public String getString(String key) {
        return (String) event.get(key);
    }

    public Date getDate() {
        return (Date) event.get("date");
    }

    public void setValue(int value) {
        columnValue.setValue(value);
    }

    public int getValue() {
        return columnValue.getValue();
    }

    public String getColumn() {
        return columnValue.getColumn();
    }

    public boolean hasSubContextes() {
        return subContextes != null;
    }

    public List<FillerContext> getSubContextes() {
        return subContextes;
    }

    public void addContext(String column) {
        if (subContextes == null) {
            subContextes = new ArrayList<FillerContext>();
        }

        FillerContext context = new FillerContext(event);
        context.columnValue.setColumn(column);
        context.columnValue.setValue(columnValue.getValue());

        subContextes.add(context);
    }

    @Override
    public String toString() {
        return "FillerContext{" +
                "event=" + event +
                ", columnValue=" + columnValue +
                ", subContextes=" + subContextes +
                '}';
    }
}
