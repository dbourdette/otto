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

package com.github.dbourdette.otto.source.schedule;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Test;

import java.text.ParseException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author damien bourdette
 */
public class SourceScheduleWatcherTest {
    private SourceScheduleWatcher watcher = new SourceScheduleWatcher();

    @Test
    public void isEligibleWithEmpty() {
        assertThat(watcher.isEligible(null, new DateTime())).isFalse();
        assertThat(watcher.isEligible("", new DateTime())).isFalse();
    }

    @Test
    public void isEligible() throws ParseException {
        assertThat(watcher.isEligible("10 * * * *", anyDateWithMinutesAt(10))).isTrue();
        assertThat(watcher.isEligible("15 * * * *", anyDateWithMinutesAt(10))).isFalse();
        assertThat(watcher.isEligible("0 18 * * *", anyDateWithHourAndMinutesAt(18, 0))).isTrue();
        assertThat(watcher.isEligible("0 18 * * *", anyDateWithHourAndMinutesAt(18, 1))).isFalse();
    }

    @Test
    public void isEligibleNicknames() {
        assertThat(watcher.isEligible("@monthly", startOfMonth())).isTrue();
    }

    @Test
    public void parseCronExpression() throws ParseException {
        assertThat(watcher.parse("30 0 * * MON")).isNotNull();
    }

    private DateTime anyDateWithMinutesAt(int minute) {
        return new DateTime().withMinuteOfHour(minute);
    }

    private DateTime anyDateWithHourAndMinutesAt(int hour, int minute) {
        return anyDateWithMinutesAt(minute).withHourOfDay(hour);
    }

    private DateTime startOfMonth() {
        return new DateMidnight().withDayOfMonth(1).toDateTime();
    }
}
