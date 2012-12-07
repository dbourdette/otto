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

package com.github.dbourdette.otto.web.util;

import com.github.dbourdette.otto.Registry;
import com.mongodb.DBCollection;

public class MongoCollections {
    public static final String LOGS = "otto.logs";

    public static final String MAIL_CONFIG = "otto.config.mail";

    public static final String USERS = "otto.users";

    public static final String SOURCES = "otto.sources";

    public static final String SOURCES_DISPLAYS = "otto.sources.displays";

    public static final String SOURCES_REPORTS = "otto.sources.reports";

    public static final String SOURCES_SCHEDULES = "otto.sources.schedules";

    public static DBCollection eventsCollection(String name) {
        return Registry.mongoDb.getCollection(eventsCollectionName(name));
    }

    public static String eventsCollectionName(String name) {
        return "otto.sources." + name + ".events";
    }
}
