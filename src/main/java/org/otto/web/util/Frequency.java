package org.otto.web.util;

import org.joda.time.Duration;

public class Frequency {
	
	private int count;
	
	private Duration duration;

	public Frequency(int count, Duration duration) {
		this.count = count;
		this.duration = duration;
	}

	public double getEventsPerMinute() {
		return count / ((double) (duration.getMillis() / (1000 * 60)));
	}

	public double getEventsPerHour() {
		return count / ((double) (duration.getMillis() / (1000 * 60 * 60)));
	}
	
	public Duration getDuration() {
		return duration;
	}

	public int getCount() {
		return count;
	}
	
}
