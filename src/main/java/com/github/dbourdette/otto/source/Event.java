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

/**
 * @author damien bourdette
 * @version \$Revision$
 */
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

			event.putValue(name, node.get(name).getValueAsText());
		}

		return event;
	}

	public static Event fromKeyValues(String keyValues) {
		Event event = new Event();
		
		for (String token : StringUtils.split(keyValues, ",")) {
			String[] keyValue = StringUtils.split(token, "=");

			event.putValue(keyValue[0], keyValue[1]);
		}
		
		return event;
	}
	
	public static Event fromMap(Map<String, String> map) {
		Event event = new Event();
		
		for (Map.Entry<String, String> entry : map.entrySet()) {
			event.putValue(entry.getKey(), entry.getValue());
		}
		
		return event;
	}
	
	public Event putValue(String key, String value) {
		if (StringUtils.startsWith(key, INTEGER_PREFIX)) {
			key = StringUtils.substringAfter(key, INTEGER_PREFIX);
			int intValue = Integer.parseInt(value);
			
			values.put(key, new EventValue(EventValueType.INTEGER, intValue));
		} else if (StringUtils.startsWith(key, BOOLEAN_PREFIX)) {
			key = StringUtils.substringAfter(key, BOOLEAN_PREFIX);
			Boolean booleanValue = Boolean.valueOf(value);
			
			values.put(key, new EventValue(EventValueType.BOOLEAN, booleanValue));
		} else if (StringUtils.startsWith(key, DATE_PREFIX)) {
			key = StringUtils.substringAfter(key, DATE_PREFIX);
			DateTime dateValue = DateTimeFormat.forPattern(DATE_PATTERN).parseDateTime(value);
			
			values.put(key, new EventValue(EventValueType.DATE, dateValue));
		} else {
			values.put(key, new EventValue(EventValueType.STRING, value));	
		} 
		
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
}
