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

package org.otto.graph;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class GraphUtils {

    private static final Duration ONE_DAY = Duration.standardDays(1);

    private static final Duration FIVE_DAYS = Duration.standardDays(5);

    public static Duration findBest(DateTime start, DateTime end) {
        Duration duration = new Duration(start, end);

        if (duration.isShorterThan(ONE_DAY) || duration.equals(ONE_DAY)) {
            return Duration.standardMinutes(5);
        } else if (duration.isShorterThan(FIVE_DAYS) || duration.equals(FIVE_DAYS)) {
            return Duration.standardMinutes(30);
        } else {
            return Duration.standardDays(1);
        }
    }
}
