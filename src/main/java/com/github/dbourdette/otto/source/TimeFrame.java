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

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public enum TimeFrame {
    MILLISECOND, THIRTY_SECONDS, ONE_MINUTE, FIVE_MINUTES, THIRTY_MINUTES, ONE_HOUR, TWELVE_HOURS, ONE_DAY;

    public static TimeFrame safeValueOf(String name, TimeFrame defaultValue) {
        if (StringUtils.isEmpty(name)) {
            return defaultValue;
        }

        try {
            return TimeFrame.valueOf(name);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public DateTime roundDate(DateTime dateTime) {
        switch (this) {
            case MILLISECOND:
                return dateTime;
            case THIRTY_SECONDS:
                return thirtySeconds(dateTime);
            case ONE_MINUTE:
                return oneMinute(dateTime);
            case FIVE_MINUTES:
                return fiveMinutes(dateTime);
            case THIRTY_MINUTES:
                return thirtyMinutes(dateTime);
            case ONE_HOUR:
                return oneHour(dateTime);
            case TWELVE_HOURS:
                return twelveHours(dateTime);
            case ONE_DAY:
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
