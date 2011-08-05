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

package org.otto.web.form;

import org.joda.time.DateMidnight;

import java.util.Date;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class GraphForm {
	public Date start;
	
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
}
