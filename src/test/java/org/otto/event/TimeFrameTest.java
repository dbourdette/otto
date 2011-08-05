package org.otto.event;

import junit.framework.Assert;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class TimeFrameTest {
    @Test
    public void roundDateMillisecond() {
        DateTime source = new DateTime(2010, 10, 10, 11, 12, 13, 5);

        Assert.assertEquals(source, TimeFrame.MILLISECOND.roundDate(source));
    }

    @Test
    public void roundDate30Seconds() {
        DateTime source = new DateTime(2010, 10, 10, 11, 12, 13, 5);
        DateTime expected = new DateTime(2010, 10, 10, 11, 12, 0, 0);

        Assert.assertEquals(expected, TimeFrame.THIRTY_SECONDS.roundDate(source));

        source = new DateTime(2010, 10, 10, 11, 12, 34, 5);
        expected = new DateTime(2010, 10, 10, 11, 12, 30, 0);

        Assert.assertEquals(expected, TimeFrame.THIRTY_SECONDS.roundDate(source));
    }

    @Test
    public void roundDateMinute() {
        DateTime source = new DateTime(2010, 10, 10, 11, 12, 13, 5);
        DateTime expected = new DateTime(2010, 10, 10, 11, 12, 0, 0);

        Assert.assertEquals(expected, TimeFrame.ONE_MINUTE.roundDate(source));

        source = new DateTime(2010, 10, 10, 11, 12, 34, 5);
        expected = new DateTime(2010, 10, 10, 11, 12, 0, 0);

        Assert.assertEquals(expected, TimeFrame.ONE_MINUTE.roundDate(source));
    }

    @Test
    public void roundDateFiveMinutes() {
        DateTime source = new DateTime(2010, 10, 10, 11, 12, 13, 5);
        DateTime expected = new DateTime(2010, 10, 10, 11, 10, 0, 0);

        Assert.assertEquals(expected, TimeFrame.FIVE_MINUTES.roundDate(source));

        source = new DateTime(2010, 10, 10, 11, 35, 34, 5);
        expected = new DateTime(2010, 10, 10, 11, 35, 0, 0);

        Assert.assertEquals(expected, TimeFrame.FIVE_MINUTES.roundDate(source));
    }

    @Test
    public void roundDateThirtyMinutes() {
        DateTime source = new DateTime(2010, 10, 10, 11, 12, 13, 5);
        DateTime expected = new DateTime(2010, 10, 10, 11, 0, 0, 0);

        Assert.assertEquals(expected, TimeFrame.THIRTY_MINUTES.roundDate(source));

        source = new DateTime(2010, 10, 10, 11, 35, 34, 5);
        expected = new DateTime(2010, 10, 10, 11, 30, 0, 0);

        Assert.assertEquals(expected, TimeFrame.THIRTY_MINUTES.roundDate(source));
    }
}
