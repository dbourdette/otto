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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.github.dbourdette.otto.web.exception.SourceAlreadyExists;
import com.github.dbourdette.otto.web.form.SourceForm;
import com.github.dbourdette.otto.web.util.Constants;
import com.github.dbourdette.otto.web.util.SizeInBytes;
import com.github.dbourdette.otto.web.util.SourceGroup;
import com.github.dbourdette.otto.web.util.SourceGroups;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Component
public class Sources {

    @Inject
    private DB mongoDb;

    private volatile Map<String, DBSource> cache = new HashMap<String, DBSource>();

    @PostConstruct
    public void loadSources() {
        Map<String, DBSource> newCache = new HashMap<String, DBSource>();

        for (String name : getNames()) {
            newCache.put(name, loadSource(name));
        }

        cache = newCache;
    }

    public DBSource getSource(String name) {
       return cache.get(name);
    }

    public DBSource createSource(SourceForm form) {
        String collectionName = DBSource.qualifiedName(form.getName());

        if (mongoDb.collectionExists(collectionName)) {
            throw new SourceAlreadyExists();
        }

        BasicDBObject capping = new BasicDBObject();

        SizeInBytes sizeInBytes = form.getSizeInBytes();

        if (sizeInBytes == null) {
            capping.put("capped", false);
        } else {
            capping.put("capped", true);
            capping.put("size", sizeInBytes.getValue());

            if (form.getMaxEvents() != null) {
                capping.put("max", form.getMaxEvents());
            }
        }

        mongoDb.createCollection(collectionName, capping);

        loadSources();

        DBSource source = getSource(form.getName());

        source.updateDisplayGroupAndName(form.getDisplayGroup(), form.getDisplayName());

        return source;
    }

    public void dropSource(String name) {
        DBSource source = getSource(name);

        source.drop();

        cache.remove(name);
    }

    public Collection<DBSource> getSources() {
        return cache.values();
    }

    public SourceGroups getSourceGroups() {
        SourceGroups groups = new SourceGroups();

        groups.addAll(cache.values());

        return groups;
    }

    public List<String> getNames() {
        List<String> sources = new ArrayList<String>();

        for (String name : mongoDb.getCollectionNames()) {
            if (name.startsWith(Constants.SOURCES) && name.endsWith(Constants.EVENTS)) {
                sources.add(StringUtils.substringBetween(name, Constants.SOURCES, Constants.EVENTS));
            }
        }

        return sources;
    }

    private DBSource loadSource(String name) {
       return DBSource.fromDb(mongoDb, name);
    }
}
