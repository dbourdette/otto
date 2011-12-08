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

package com.github.dbourdette.otto.report.filler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.DBObject;

/**
 * Context that will be transmitted to all chain elements.
 *
 * @author damien bourdette
 */
public class ChainContext {
    private DBObject event;

    private ChainContextValue value = new ChainContextValue();

    private List<ChainContext> subContextes;

    public ChainContext(DBObject event) {
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
        this.value.setValue(value);
    }

    public int getValue() {
        return value.getValue();
    }

    public String getColumn() {
        return value.getColumn();
    }

    public void setColumn(String column) {
        value.setColumn(column);
    }

    public boolean hasSubContextes() {
        return subContextes != null;
    }

    public List<ChainContext> getSubContextes() {
        return subContextes;
    }

    public void addContext(String column) {
        if (subContextes == null) {
            subContextes = new ArrayList<ChainContext>();
        }

        ChainContext context = new ChainContext(event);
        context.value.setColumn(column);
        context.value.setValue(value.getValue());

        subContextes.add(context);
    }

    @Override
    public String toString() {
        return "ChainContext{" +
                "event=" + event +
                ", value=" + value +
                ", subContextes=" + subContextes +
                '}';
    }
}
