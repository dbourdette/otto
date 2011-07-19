package org.otto.web.util;

import java.util.Random;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * @author damien bourdette
 */
public class RandomDateUtils {
	public static DateTime today() {
		return in(new Interval(new DateMidnight(), new DateTime()));
	}
	
	public static DateTime last7Days() {
		return in(new Interval(new DateMidnight().minusDays(6), new DateTime()));
	}
	
	public static DateTime in(Interval interval) {
		Random random = new Random();
		
		long millis = (long) ((interval.getEndMillis() - interval.getStartMillis()) * random.nextDouble());
		
		return interval.getStart().plus(millis);
	}
}
