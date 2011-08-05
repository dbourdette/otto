package org.otto.web.util;

import com.mongodb.BasicDBObject;
import org.joda.time.DateMidnight;
import org.joda.time.Interval;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class IntervalUtils {
	public static Interval yesterday() {
		return new Interval(new DateMidnight().minusDays(1), new DateMidnight());
	}
	
	public static Interval today() {
		return new Interval(new DateMidnight(), new DateMidnight().plusDays(1));
	}
	
	public static Interval lastWeek() {
		DateMidnight monday = new DateMidnight().withDayOfWeek(1);
		
		return new Interval(monday.minusDays(7), monday);
	}

	public static BasicDBObject query(Interval interval) {
		BasicDBObject criteria = new BasicDBObject();

		criteria.append("$gt", interval.getStart().toDate());
		criteria.append("$lte", interval.getEnd().toDate());

		return new BasicDBObject("date", criteria);
	}
}
