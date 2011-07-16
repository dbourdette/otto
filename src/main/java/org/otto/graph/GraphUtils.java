package org.otto.graph;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class GraphUtils {

    private static final Duration ONE_DAY = Duration.standardDays(1);

    private static final Duration FIVE_DAYS = Duration.standardDays(5);

    public static Duration findBest(DateTime start, DateTime end) {
        Duration duration = new Duration(start, end);

        if (duration.isShorterThan(ONE_DAY) || duration.equals(ONE_DAY)) {
            return Duration.standardMinutes(5);
        } else if (duration.isShorterThan(FIVE_DAYS) || duration.equals(FIVE_DAYS)) {
            return Duration.standardMinutes(30);
        } else {
            return Duration.standardDays(1);
        }
    }
}
