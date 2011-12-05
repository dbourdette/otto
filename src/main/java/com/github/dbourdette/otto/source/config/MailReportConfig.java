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

package com.github.dbourdette.otto.source.config;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.github.dbourdette.otto.graph.ReportPeriod;
import com.github.dbourdette.otto.web.form.ReportForm;
import com.github.dbourdette.otto.web.form.Sort;

/**
 * @author damien bourdette
 */
public class MailReportConfig {
    private String id;

    private String sourceName;

    private String cronExpression;

    @NotEmpty
    private String to;

    @NotEmpty
    private String title;

    @NotNull
    private ReportPeriod period;

    private String reportId;

    public ReportForm toReportForm() {
        ReportForm form = new ReportForm();

        form.setPeriod(period);
        form.setReportId(reportId);

        return form;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ReportPeriod getPeriod() {
        return period;
    }

    public ReportPeriod[] getPeriods() {
        return ReportPeriod.values();
    }

    public void setPeriod(ReportPeriod period) {
        this.period = period;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Sort[] getSorts() {
        return Sort.values();
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
}
