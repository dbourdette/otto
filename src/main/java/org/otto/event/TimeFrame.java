package org.otto.event;

import org.joda.time.DateTime;

/**
 * @author damien bourdette
 */
public enum TimeFrame {
	MILLISECOND, THIRTY_SECONDS, ONE_MINUTE, FIVE_MINUTES, THIRTY_MINUTES, ONE_HOUR, TWELVE_HOURS, ONE_DAY;

	public DateTime roundDate(DateTime dateTime) {
		switch (this) {
		case MILLISECOND:
			return dateTime;
		case THIRTY_SECONDS :
			return thirtySeconds(dateTime);
		case ONE_MINUTE :
			return oneMinute(dateTime);
		case FIVE_MINUTES :
			return fiveMinutes(dateTime);
		case THIRTY_MINUTES :
			return thirtyMinutes(dateTime);
		case ONE_HOUR :
			return oneHour(dateTime);
		case TWELVE_HOURS :
			return twelveHours(dateTime);
		case ONE_DAY :
			return oneDay(dateTime);
		}
		return dateTime;
	}
	
	private DateTime thirtySeconds(DateTime dateTime) {
		dateTime = dateTime.withMillisOfSecond(0);
		
		if (dateTime.getSecondOfMinute() < 30) {
			return dateTime.withSecondOfMinute(0);
		} else {
			return dateTime.withSecondOfMinute(30);
		}
	}
	
	private DateTime oneMinute(DateTime dateTime) {
		dateTime = dateTime.withMillisOfSecond(0);
		return dateTime.withSecondOfMinute(0);
	}
	
	private DateTime fiveMinutes(DateTime dateTime) {
		dateTime = dateTime.withMillisOfSecond(0);
		dateTime = dateTime.withSecondOfMinute(0);
		
		int minutes = dateTime.getMinuteOfHour();
		
		return dateTime.withMinuteOfHour(minutes / 5 * 5);
	}
	
	private DateTime thirtyMinutes(DateTime dateTime) {
		dateTime = dateTime.withMillisOfSecond(0);
		dateTime = dateTime.withSecondOfMinute(0);
		
		if (dateTime.getMinuteOfHour() < 30) {
			return dateTime.withMinuteOfHour(0);
		} else {
			return dateTime.withMinuteOfHour(30);
		}
	}
	
	private DateTime oneHour(DateTime dateTime) {
		dateTime = dateTime.withMillisOfSecond(0);
		dateTime = dateTime.withSecondOfMinute(0);
		return dateTime.withMinuteOfHour(0);
	}
	
	private DateTime twelveHours(DateTime dateTime) {
		dateTime = dateTime.withMillisOfSecond(0);
		dateTime = dateTime.withSecondOfMinute(0);
		dateTime = dateTime.withMinuteOfHour(0);
		
		if (dateTime.getHourOfDay() < 12) {
			return dateTime.withHourOfDay(0);
		} else {
			return dateTime.withHourOfDay(12);
		}
	}
	
	private DateTime oneDay(DateTime dateTime) {
		dateTime = dateTime.withMillisOfSecond(0);
		dateTime = dateTime.withSecondOfMinute(0);
		dateTime = dateTime.withMinuteOfHour(0);
		return dateTime.withHourOfDay(0);
	}
}
