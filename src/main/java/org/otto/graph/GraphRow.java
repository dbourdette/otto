package org.otto.graph;

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class GraphRow implements Comparable<GraphRow> {

    private Interval interval;

    public GraphRow() {
    }

    public GraphRow(Interval interval) {
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
    public int compareTo(GraphRow row) {
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
        GraphRow other = (GraphRow) obj;
        if (interval == null) {
            if (other.interval != null)
                return false;
        } else if (!interval.equals(other.interval))
            return false;
        return true;
    }

}
