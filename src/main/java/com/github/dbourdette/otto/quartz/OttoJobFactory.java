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

package com.github.dbourdette.otto.quartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.stereotype.Component;

/**
 * @author damien bourdette
 */
@Component
public class OttoJobFactory implements org.quartz.spi.JobFactory {

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        return createJob(bundle.getJobDetail());
    }

    private Job createJob(JobDetail jobDetail) throws SchedulerException {
        Class<? extends Job> jobClass = jobDetail.getJobClass();

        try {
            return (Job) jobClass.newInstance();
        } catch (Exception e) {
            throw new SchedulerException("Problem instantiating class '" + jobDetail.getJobClass().getName() + "'", e);
        }
    }
}
