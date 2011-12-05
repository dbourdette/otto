package com.github.dbourdette.otto.graph;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Interval;

/**
 * @author damien bourdette
 */
public enum ReportPeriod {
    RECENT(5), TODAY(60), YESTERDAY(60), THIS_WEEK(6 * 60), LAST_WEEK(6 * 60), THIS_MONTH(24 * 60), LAST_MONTH(24 * 60);

    private int stepInMinutes;

    public Interval getInterval() {
        switch (this) {
            case RECENT: {
                DateTime start = new DateTime().minusHours(2).withMinuteOfHour(0);

                return new Interval(start, start.plusHours(3));
            }
            case TODAY: {
                DateMidnight start = new DateMidnight();

                return new Interval(start, start.plusDays(1));
            }
            case YESTERDAY: {
                DateMidnight end = new DateMidnight();

                return new Interval(end.minusDays(1), end);
            }
            case THIS_WEEK: {
                DateMidnight start = new DateMidnight().withDayOfWeek(DateTimeConstants.MONDAY);

                return new Interval(start, start.plusDays(7));
            }
            case LAST_WEEK: {
                DateMidnight end = new DateMidnight().withDayOfWeek(DateTimeConstants.MONDAY);

                return new Interval(end.minusDays(7), end);
            }
            case THIS_MONTH: {
                DateMidnight start = new DateMidnight().withDayOfMonth(1);

                return new Interval(start, start.plusMonths(1));
            }
            case LAST_MONTH: {
                DateMidnight end = new DateMidnight().withDayOfMonth(1);

                return new Interval(end.minusMonths(1), end);
            }
        }

        return null;
    }

    public int getStepInMinutes() {
        return stepInMinutes;
    }

    public void createRows(Graph graph) {
        graph.setRows(getInterval(), Duration.standardMinutes(getStepInMinutes()));
    }

    private ReportPeriod(int stepInMinutes) {
        this.stepInMinutes = stepInMinutes;
    }
}
