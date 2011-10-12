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
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

import com.github.dbourdette.otto.SpringConfig;
import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.MailReportConfig;
import com.github.dbourdette.otto.source.MailReports;
import com.github.dbourdette.otto.source.Sources;
import com.github.dbourdette.otto.web.exception.SourceNotFound;

/**
 * Quartz job used to schedule and send reports.
 *
 * @author damien bourdette
 */
public class SendReportJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        MailReports mailReports = getBean(jobExecutionContext, MailReports.class);
        Sources sources = getBean(jobExecutionContext, Sources.class);

        String reportId = jobExecutionContext.getMergedJobDataMap().getString(MailReports.REPORT_ID);
        String sourceName = jobExecutionContext.getMergedJobDataMap().getString(MailReports.SOURCE_NAME);

        DBSource source = null;

        try {
            source = sources.getSource(sourceName);
        } catch (SourceNotFound e) {
            throw new JobExecutionException("No source found for name " + sourceName, e);
        }

        MailReportConfig mailReportConfig = source.getMailReport(reportId);

        try {
            mailReports.sendReport(mailReportConfig);
        } catch (Exception e) {
            throw new JobExecutionException("Failed to send report", e);
        }
    }

    public <T> T getBean(JobExecutionContext context, Class<T> beanClass) throws JobExecutionException {
        return getApplicationContext(context).getBean(beanClass);
    }

    public ApplicationContext getApplicationContext(JobExecutionContext context) throws JobExecutionException {
        try {
            return getApplicationContext(context.getScheduler().getContext());
        } catch (SchedulerException e) {
            throw new JobExecutionException("Failed to get scheduler from JobExecutionContext");
        }
    }

    public ApplicationContext getApplicationContext(SchedulerContext schedulerContext) throws JobExecutionException {
        ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get(SpringConfig.APPLICATION_CONTEXT_KEY);

        if (applicationContext == null) {
            throw new JobExecutionException("No application context available in scheduler context for key \"" + SpringConfig.APPLICATION_CONTEXT_KEY + "\"");
        }

        return applicationContext;
    }
}
