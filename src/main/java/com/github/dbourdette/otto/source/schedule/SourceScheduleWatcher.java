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

import com.github.dbourdette.otto.service.logs.Logs;
import com.github.dbourdette.otto.source.Source;
import com.kenai.crontabparser.CronTabExpression;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.Calendar;

/**
 * Finds and executes all eligible schedule every minute
 *
 * @author damien bourdette
 */
@Component
public class SourceScheduleWatcher {
    @Inject
    private SourceScheduleExecutor executor;

    @Inject
    private Logs logs;

    @Scheduled(cron = "0 * * * * *")
    public void run() {
        DateTime now = new DateTime();

        for (Source source : Source.findAll()) {
            for (MailSchedule schedule : SourceSchedules.forSource(source).getSchedules()) {
                if (isEligible(schedule.getCronExpression(), now)) {
                    logs.trace(executor.executionMessage(source, schedule));

                    executor.execute(source, schedule);
                }
            }
        }
    }

    public boolean isEligible(String cronExpression, DateTime dateTime) {
        if (StringUtils.isEmpty(cronExpression)) {
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.toDate());

        try {
            return CronTabExpression.parse(cronExpression).matches(calendar);
        } catch (ParseException e) {
            logs.error("Failed to see if schedule is eligible for running, reason : " + e.getMessage(), e);

            return false;
        }

    }

    public CronTabExpression parse(String cronExpression) throws ParseException {
        return CronTabExpression.parse(cronExpression);
    }
}
