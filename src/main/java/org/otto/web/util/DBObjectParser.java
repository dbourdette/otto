package org.otto.web.util;

import java.io.IOException;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 */
@Component
public class DBObjectParser {

	private JsonFactory jsonFactory = new JsonFactory();

	@PostConstruct
	public void initFactory() {
		jsonFactory.setCodec(new ObjectMapper());
	}

	public DBObject fromJson(String json) throws JsonParseException, IOException {
		if (Strings.isNullOrEmpty(json)) {
			return new BasicDBObject();
		}

		JsonParser jsonParser = jsonFactory.createJsonParser(json);

		JsonNode node = jsonParser.readValueAsTree();

		BasicDBObject object = new BasicDBObject();

		Iterator<String> names = node.getFieldNames();

		while (names.hasNext()) {
			String name = names.next();

			object.put(name, node.get(name).getValueAsText());
		}

		return object;
	}

	public DBObject fromKeyValues(String keyValues) {
		BasicDBObject object = new BasicDBObject();
		
		Iterable<String> tokens = Splitter.on(",").trimResults().omitEmptyStrings().split(keyValues);

		for (String token : tokens) {
			Iterator<String> iterator = Splitter.on("=").trimResults().omitEmptyStrings().split(token).iterator();

			object.append(iterator.next(), iterator.next());
		}
		
		return object;
	}
}
