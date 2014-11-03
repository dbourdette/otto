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

package com.github.dbourdette.otto.source;

import org.joda.time.DateTime;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TimeFrameTest {
    @Test
    public void roundDateMillisecond() {
        for (DateTime date : refDates()) {
            assertEquals(date, TimeFrame.MILLISECOND.roundDate(date));
        }
    }

    @Test
    public void roundDate30Seconds() {
        for (DateTime date : refDates()) {
            int times = date.getSecondOfMinute() / 30;

            assertEquals(date.withSecondOfMinute(times * 30).withMillisOfSecond(0), TimeFrame.THIRTY_SECONDS.roundDate(date));
        }
    }

    @Test
    public void roundDateMinute() {
        for (DateTime date : refDates()) {
            assertEquals(date.withSecondOfMinute(0).withMillisOfSecond(0), TimeFrame.ONE_MINUTE.roundDate(date));
        }
    }

    @Test
    public void roundDateFiveMinutes() {
        for (DateTime date : refDates()) {
            int times = date.getMinuteOfHour() / 5;
            
            assertEquals(date.withMinuteOfHour(times * 5).withSecondOfMinute(0).withMillisOfSecond(0), TimeFrame.FIVE_MINUTES.roundDate(date));
        }
    }

    @Test
    public void roundDateThirtyMinutes() {
        for (DateTime date : refDates()) {
            int times = date.getMinuteOfHour() / 30;

            assertEquals(date.withMinuteOfHour(times * 30).withSecondOfMinute(0).withMillisOfSecond(0), TimeFrame.THIRTY_MINUTES.roundDate(date));
        }
    }

    private DateTime[] refDates() {
        return new DateTime[] {new DateTime(2010, 10, 10, 11, 12, 13, 5), new DateTime(2010, 10, 10, 11, 35, 34, 5)};
    }
}
