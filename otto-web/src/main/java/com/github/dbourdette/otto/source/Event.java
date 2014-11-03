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

package com.github.dbourdette.otto.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.google.common.base.Strings;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Event {

    public static final String DATE_KEY = "date";

    public static final String INTEGER_PREFIX = "i_";

    public static final String BOOLEAN_PREFIX = "b_";

    public static final String DATE_PREFIX = "d_";

    public static final String DATE_PATTERN = "yyyy-dd-MM'T'HH:mm:ss'Z'";

    private Map<String, EventValue> values = new HashMap<String, EventValue>();

    public static Event fromJson(String json) throws JsonParseException, IOException {
        Event event = new Event();

        if (Strings.isNullOrEmpty(json)) {
            return event;
        }

        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.setCodec(new ObjectMapper());

        JsonParser jsonParser = jsonFactory.createJsonParser(json);

        JsonNode node = jsonParser.readValueAsTree();

        Iterator<String> names = node.getFieldNames();

        while (names.hasNext()) {
            String name = names.next();

            event.parseValue(name, node.get(name).getValueAsText());
        }

        return event;
    }

    public static Event fromKeyValues(String keyValues) {
        Event event = new Event();

        for (String token : StringUtils.split(keyValues, ",")) {
            String[] keyValue = StringUtils.split(token, "=");

            event.parseValue(keyValue[0], keyValue[1]);
        }

        return event;
    }

    public static Event fromMap(Map<String, String> map) {
        Event event = new Event();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            event.parseValue(entry.getKey(), entry.getValue());
        }

        return event;
    }

    public Event parseValue(String key, String value) {
        if (StringUtils.startsWith(key, INTEGER_PREFIX)) {
            key = StringUtils.substringAfter(key, INTEGER_PREFIX);

            putInt(key, Integer.parseInt(value));
        } else if (StringUtils.startsWith(key, BOOLEAN_PREFIX)) {
            key = StringUtils.substringAfter(key, BOOLEAN_PREFIX);

            putBoolean(key, Boolean.valueOf(value));
        } else if (StringUtils.startsWith(key, DATE_PREFIX)) {
            key = StringUtils.substringAfter(key, DATE_PREFIX);

            putDate(key, DateTimeFormat.forPattern(DATE_PATTERN).parseDateTime(value));
        } else {
            putString(key, value);
        }

        return this;
    }

    public Event putValue(String key, Object value) {
        if (value instanceof String) {
            putString(key, (String) value);
        } else if (value instanceof Integer) {
            putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            putBoolean(key, (Boolean) value);
        } else if (value instanceof DateTime) {
            putDate(key, (DateTime) value);
        }

        return this;
    }

    public Event putString(String key, String value) {
        values.put(key, new EventValue(EventValueType.STRING, value));

        return this;
    }

    public Event putInt(String key, int value) {
        values.put(key, new EventValue(EventValueType.INTEGER, value));

        return this;
    }

    public Event putBoolean(String key, boolean value) {
        values.put(key, new EventValue(EventValueType.BOOLEAN, value));

        return this;
    }

    public Event putDate(String key, DateTime value) {
        values.put(key, new EventValue(EventValueType.DATE, value));

        return this;
    }

    public DBObject toDBObject() {
        BasicDBObject object = new BasicDBObject();

        for (Map.Entry<String, EventValue> entry : values.entrySet()) {
            Object value = entry.getValue().getValue();

            if (entry.getValue().getType() == EventValueType.DATE) {
                object.put(entry.getKey(), ((DateTime) value).toDate());
            } else {
                object.put(entry.getKey(), value);
            }
        }

        return object;
    }

    public int size() {
        return values.size();
    }

    public Object get(String key) {
        EventValue value = values.get(key);

        if (value == null) {
            return null;
        }

        return value.getValue();
    }

    public DateTime getDate() {
        return (DateTime) get(DATE_KEY);
    }

    public void setDate(DateTime date) {
        values.put(DATE_KEY, new EventValue(EventValueType.DATE, date));
    }

    public void setDateIfNoneDefined(DateTime date) {
        EventValue value = values.get(DATE_KEY);

        if (value == null || value.getType() != EventValueType.DATE) {
            setDate(date);
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                "values=" + values +
                '}';
    }
}
