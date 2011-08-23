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

package org.otto.source;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.joda.time.DateTime;
import org.junit.Test;

import com.mongodb.DBObject;

import junit.framework.Assert;


/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class EventTest {
	
	@Test
	public void fromEmptyJson() throws JsonParseException, IOException {
		Assert.assertEquals(0, Event.fromJson("").size());
		Assert.assertEquals(0, Event.fromJson(null).size());
		Assert.assertEquals(0, Event.fromJson("{}").size());
	}
	
	@Test
	public void fromJson() throws JsonParseException, IOException {
		Event event = Event.fromJson("{\"name\":\"sally\"}");
		
		Assert.assertEquals(1, event.size());
		Assert.assertEquals("sally", event.get("name"));
	}
	
	@Test
	public void fromKeyValues() throws JsonParseException, IOException {
		Event event = Event.fromKeyValues("name=sally,gender=FEMALE");
		
		Assert.assertEquals(2, event.size());
		Assert.assertEquals("sally", event.get("name"));
	}
	
	@Test
	public void setDateIfNoneDefined() throws JsonParseException, IOException {
		Event event = Event.fromKeyValues("name=sally,gender=FEMALE");
		
		DateTime date = new DateTime(2010, 10, 10, 0, 0, 0, 0);
		
		event.setDateIfNoneDefined(date);
		
		Assert.assertEquals(date, event.get("date"));
	}
	
	@Test
	public void putValue() {
		Event event = new Event().putValue("name", "sally");
		
		Assert.assertEquals("sally", event.get("name"));
	}
	
	@Test
	public void putIntegerValue() {
		Event event = new Event().putValue("i_count", "3");
		
		Assert.assertEquals(3, event.get("count"));
	}
	
	@Test
	public void putBooleanValue() {
		Event event = new Event().putValue("b_question", "false");
		
		Assert.assertEquals(false, event.get("question"));
	}
	
	@Test
	public void putDateValue() {
		Event event = new Event().putValue("d_date", "2010-10-10T00:00:00Z");
		
		Assert.assertEquals(new DateTime(2010, 10, 10, 0, 0, 0, 0), event.get("date"));
	}
	
	@Test
	public void toDBObject() throws JsonParseException, IOException {
		Event event = Event.fromKeyValues("name=sally,gender=FEMALE");
		
		DBObject object = event.toDBObject();
		
		Assert.assertEquals("sally", object.get("name"));
		Assert.assertEquals("FEMALE", object.get("gender"));
	}
}
