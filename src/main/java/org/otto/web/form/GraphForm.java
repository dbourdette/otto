package org.otto.web.form;

import java.util.Date;

import org.joda.time.DateMidnight;

public class GraphForm {
	public Date start;
	
	public Date end;
	
	public int stepInMinutes;
	
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
}
