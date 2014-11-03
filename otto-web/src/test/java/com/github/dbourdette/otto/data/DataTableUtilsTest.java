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

package com.github.dbourdette.otto.data;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.junit.Test;

import junit.framework.Assert;

public class DataTableUtilsTest {

    @Test
    public void findBest() {
        DateTime now = new DateTime();

        Interval interval = new Interval(now.minusDays(1), now);
        Assert.assertEquals(Duration.standardMinutes(5), DataTableUtils.findBest(interval));

        interval = new Interval(now.minusDays(30), now);
        Assert.assertEquals(Duration.standardDays(1), DataTableUtils.findBest(interval));
    }
}
