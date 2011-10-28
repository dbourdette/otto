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

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.github.dbourdette.otto.graph.GraphPeriod;
import com.github.dbourdette.otto.web.form.GraphForm;

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
    private GraphPeriod period;

    public String sumColumn;

    public String splitColumn;

    public GraphForm toGraphForm() {
        GraphForm form = new GraphForm();

        form.setPeriod(period);
        form.setSplitColumn(splitColumn);
        form.setSumColumn(sumColumn);

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

    public GraphPeriod getPeriod() {
        return period;
    }

    public GraphPeriod[] getPeriods() {
        return GraphPeriod.values();
    }

    public void setPeriod(GraphPeriod period) {
        this.period = period;
    }

    public String getSumColumn() {
        return sumColumn;
    }

    public void setSumColumn(String sumColumn) {
        this.sumColumn = sumColumn;
    }

    public String getSplitColumn() {
        return splitColumn;
    }

    public void setSplitColumn(String splitColumn) {
        this.splitColumn = splitColumn;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public String toString() {
        return "MailReportConfig{" +
                "id='" + id + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", to='" + to + '\'' +
                ", title='" + title + '\'' +
                ", period=" + period +
                ", sumColumn='" + sumColumn + '\'' +
                ", splitColumn='" + splitColumn + '\'' +
                '}';
    }
}
