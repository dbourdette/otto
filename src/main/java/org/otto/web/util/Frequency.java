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

package org.otto.web.util;

import org.joda.time.Duration;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class Frequency {
	
	private int count;
	
	private Duration duration;

	public Frequency(int count, Duration duration) {
		this.count = count;
		this.duration = duration;
	}

	public double getEventsPerMinute() {
		return count / ((double) (duration.getMillis() / (1000 * 60)));
	}

	public double getEventsPerHour() {
		return count / ((double) (duration.getMillis() / (1000 * 60 * 60)));
	}
	
	public Duration getDuration() {
		return duration;
	}

	public int getCount() {
		return count;
	}
	
}
