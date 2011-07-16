package org.otto.graph;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

public class GraphUtilsTest {

    @Test
    public void findBest() {
        DateTime end = new DateTime();
        DateTime start = end.minusDays(1);

        Assert.assertEquals(Duration.standardMinutes(5), GraphUtils.findBest(start, end));

        end = new DateTime();
        start = end.minusDays(30);

        Assert.assertEquals(Duration.standardDays(1), GraphUtils.findBest(start, end));
    }
}
