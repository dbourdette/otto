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

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.source.Source;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * All reports available for a {@link com.github.dbourdette.otto.source.Source}
 *
 * @author damien bourdette
 */
public class SourceReports {
    private DBCollection reports;

    private Source source;

    private SourceReports() {}

    public static SourceReports forSource(Source source) {
        SourceReports reports = new SourceReports();

        reports.source = source;
        reports.reports = Registry.mongoDb.getCollection(Source.qualifiedReportsName(source.getName()));

        return reports;
    }

    public void saveReportConfig(ReportConfig reportConfig) {
        if (StringUtils.isEmpty(reportConfig.getId())) {
            BasicDBObject object = reportConfig.toDBObject();

            reports.save(object);

            reportConfig.setId(((ObjectId) object.get("_id")).toString());
        } else {
            reports.update(new BasicDBObject("_id", new ObjectId(reportConfig.getId())), reportConfig.toDBObject());
        }
    }

    public List<ReportConfig> getReportConfigs() {
        return ReportConfig.readAll(reports.find().sort(new BasicDBObject("name", 1)));
    }

    public ReportConfig getReportConfig(String id) {
        return ReportConfig.read((BasicDBObject) reports.findOne(new BasicDBObject("_id", new ObjectId(id))));
    }

    public ReportConfig getReportConfigByTitle(String title) {
        return ReportConfig.read((BasicDBObject) reports.findOne(new BasicDBObject("title", title)));
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
