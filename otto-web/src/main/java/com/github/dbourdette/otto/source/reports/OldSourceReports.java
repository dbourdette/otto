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

package com.github.dbourdette.otto.source.reports;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.source.OldSource;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * All reports available for a {@link com.github.dbourdette.otto.source.OldSource}
 *
 * @author damien bourdette
 */
public class OldSourceReports {
    private DBCollection reports;

    private OldSource source;

    private OldSourceReports() {}

    public static OldSourceReports forSource(OldSource source) {
        OldSourceReports reports = new OldSourceReports();

        reports.source = source;
        reports.reports = Registry.mongoDb.getCollection(OldSource.qualifiedReportsName(source.getName()));

        return reports;
    }

    public void saveReportConfig(OldReportConfig reportConfig) {
        if (StringUtils.isEmpty(reportConfig.getId())) {
            BasicDBObject object = reportConfig.toDBObject();

            reports.save(object);

            reportConfig.setId(((ObjectId) object.get("_id")).toString());
        } else {
            reports.update(new BasicDBObject("_id", new ObjectId(reportConfig.getId())), reportConfig.toDBObject());
        }
    }

    public List<OldReportConfig> getReportConfigs() {
        return OldReportConfig.readAll(reports.find().sort(new BasicDBObject("name", 1)));
    }

    public OldReportConfig getReportConfig(String id) {
        return OldReportConfig.read((BasicDBObject) reports.findOne(new BasicDBObject("_id", new ObjectId(id))));
    }

    public OldReportConfig getReportConfigByTitle(String title) {
        return OldReportConfig.read((BasicDBObject) reports.findOne(new BasicDBObject("title", title)));
    }

    public void deleteReportConfig(String id) {
        reports.remove(new BasicDBObject("_id", new ObjectId(id)));
    }

    /**
     * Drop underlying mongodb collection
     */
    public void dropCollection() {
        reports.drop();
    }
}
