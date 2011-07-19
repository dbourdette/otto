package org.otto.web.util;

import java.io.IOException;

import junit.framework.Assert;

import org.codehaus.jackson.JsonParseException;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.DBObject;


/**
 * @author damien bourdette
 */
public class DBObjectParserTest {
	private DBObjectParser parser = new DBObjectParser();
	
	@Before
	public void init() {
		parser.initFactory();
	}  	
	
	@Test
	public void fromEmptyJson() throws JsonParseException, IOException {
		Assert.assertEquals(0, parser.fromJson("").keySet().size());
		Assert.assertEquals(0, parser.fromJson(null).keySet().size());
		Assert.assertEquals(0, parser.fromJson("{}").keySet().size());
	}
	
	@Test
	public void fromJson() throws JsonParseException, IOException {
		DBObject object = parser.fromJson("{\"name\":\"sally\"}");
		
		Assert.assertEquals(1, object.keySet().size());
		Assert.assertEquals("sally", object.get("name"));
	}
	
	@Test
	public void fromKeyValues() throws JsonParseException, IOException {
		DBObject object = parser.fromKeyValues("name=sally,gender=FEMALE");
		
		Assert.assertEquals(2, object.keySet().size());
		Assert.assertEquals("sally", object.get("name"));
	}
}
