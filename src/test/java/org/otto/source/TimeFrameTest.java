/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.otto.source;

import org.joda.time.DateTime;
import org.junit.Test;

import junit.framework.Assert;

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
