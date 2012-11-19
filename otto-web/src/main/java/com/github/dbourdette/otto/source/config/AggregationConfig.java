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

import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;

import com.github.dbourdette.otto.source.TimeFrame;
import com.google.code.morphia.annotations.Property;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class AggregationConfig {
    @Property
    private TimeFrame timeFrame = TimeFrame.MILLISECOND;

    @Pattern(regexp = "^[A-Za-z0-9-_]+$")
    @Property
    private String attributeName = "count";

    public boolean isAggregating() {
        return timeFrame != null && timeFrame != TimeFrame.MILLISECOND;
    }

    @Override
    public String toString() {
        return "AggregationConfig{" +
                "timeFrame=" + timeFrame +
                ", attributeName='" + attributeName + '\'' +
                '}';
    }

    public TimeFrame getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(TimeFrame timeFrame) {
		this.timeFrame = timeFrame;
	}

    public String getAttributeName() {
        return StringUtils.isEmpty(attributeName) ? "count" : attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
