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

package com.github.dbourdette.otto.report;

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ReportRow implements Comparable<ReportRow> {

    private Interval interval;

    public ReportRow() {
    }

    public ReportRow(Interval interval) {
        super();
        this.interval = interval;
    }

    public boolean contains(DateTime date) {
        return interval.contains(date);
    }

    public DateTime getStartDate() {
        return interval.getStart();
    }

    public DateTime getEndDate() {
        return interval.getEnd();
    }

    @Override
    public int compareTo(ReportRow row) {
        return getStartDate().compareTo(row.getStartDate());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((interval == null) ? 0 : interval.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReportRow other = (ReportRow) obj;
        if (interval == null) {
            if (other.interval != null)
                return false;
        } else if (!interval.equals(other.interval))
            return false;
        return true;
    }

}
