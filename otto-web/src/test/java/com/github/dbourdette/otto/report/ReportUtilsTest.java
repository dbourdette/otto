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

package com.github.dbourdette.otto.report;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import junit.framework.Assert;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ReportUtilsTest {

    @Test
    public void findBest() {
        DateTime end = new DateTime();
        DateTime start = end.minusDays(1);

        Assert.assertEquals(Duration.standardMinutes(5), ReportUtils.findBest(start, end));

        end = new DateTime();
        start = end.minusDays(30);

        Assert.assertEquals(Duration.standardDays(1), ReportUtils.findBest(start, end));
    }
}
