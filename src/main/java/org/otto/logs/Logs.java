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

package org.otto.logs;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.otto.util.Page;
import org.otto.web.util.Constants;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
@Component
public class Logs {

    private static final int PAGE_SIZE = 100;

	@Inject
	private DB mongoDb;

	@PostConstruct
	public void createCollection() {
		if (mongoDb.collectionExists(Constants.LOGS)) {
			return;
		}

		BasicDBObject capping = new BasicDBObject();

		capping.put("capped", true);
		capping.put("size", 100000);

		mongoDb.createCollection(Constants.LOGS, capping);
	}

	public void trace(String message) {
		BasicDBObject log = new BasicDBObject();

		log.put("date", new Date());
		log.put("message", message);

		logs().insert(log);
	}

	public Page<DBObject> page(Integer page) {
        return Page.fromCursor(logs().find().sort(new BasicDBObject("date", -1)), page, PAGE_SIZE);
	}

	private DBCollection logs() {
		return mongoDb.getCollection(Constants.LOGS);
	}
}
