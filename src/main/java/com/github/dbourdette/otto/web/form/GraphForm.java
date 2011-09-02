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

package com.github.dbourdette.otto.web.form;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.springframework.format.annotation.DateTimeFormat;

import com.github.dbourdette.otto.graph.Graph;
import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.DefaultGraphParameters;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class GraphForm {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date end;
	
	public int stepInMinutes;
	
	public String sumColumn;

    public String splitColumn;
	
	public GraphForm() {
		DateMidnight today = new DateMidnight();
		
		start = today.minusDays(1).toDate();
		end = today.plusDays(1).toDate();
		
		stepInMinutes = 5;
	}

    public Interval getInterval() {
        DateMidnight start = new DateMidnight(this.start);
        DateMidnight end = new DateMidnight(this.end);

        return new Interval(start, end);
    }

    public void fillWithDefault(DefaultGraphParameters defaultParameters, HttpServletRequest request) {
        Map<String, String> map = request.getParameterMap();

        if (!map.containsKey("stepInMinutes")) {
            setStepInMinutes(defaultParameters.getStepInMinutes());
        }

        if (!map.containsKey("splitColumn")) {
            setSplitColumn(defaultParameters.getSplitColumn());
        }

        if (!map.containsKey("sumColumn")) {
            setSumColumn(defaultParameters.getSumColumn());
        }
    }

    public Graph buildGraph(DBSource source) {
        Graph graph = new Graph();

        Interval interval = getInterval();

        graph.setRows(interval, Duration.standardMinutes(getStepInMinutes()));

        Iterator<DBObject> events = source.findEvents(interval);

        while (events.hasNext()) {
            DBObject event = events.next();

            String columnName = getSplitColumnName(source, event);

            graph.ensureColumnsExists(columnName);

            Date date = (Date) event.get("date");

            if (StringUtils.isEmpty(getSumColumn())) {
                graph.increaseValue(columnName, new DateTime(date));
            } else {
                Integer value = (Integer) event.get(getSumColumn());

                graph.increaseValue(columnName, new DateTime(date), value);
            }
        }

        if (graph.getColumnCount() == 0) {
            graph.ensureColumnExists("no data");
        }

        return graph;
    }

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public int getStepInMinutes() {
		return stepInMinutes;
	}

	public void setStepInMinutes(int stepInMinutes) {
		this.stepInMinutes = stepInMinutes;
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

    private String getSplitColumnName(DBSource source, DBObject event) {
        if (StringUtils.isEmpty(getSplitColumn())) {
            return source.getName();
        }

        Object value = event.get(getSplitColumn());

        if (value == null) {
            return source.getName();
        }

        return value.toString();
    }
}
