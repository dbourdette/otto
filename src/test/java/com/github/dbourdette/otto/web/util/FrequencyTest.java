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

package com.github.dbourdette.otto.web.util;

import org.joda.time.Duration;
import org.junit.Test;

import junit.framework.Assert;


/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class FrequencyTest {
	@Test
	public void getEventsPerMinute() {
		Frequency frequency = new Frequency(10, Duration.standardMinutes(1));
		
		Assert.assertEquals(10d, frequency.getEventsPerMinute());
	}
	
	@Test
	public void getEventsPerMinuteMoreComplex() {
		Frequency frequency = new Frequency(50, Duration.standardMinutes(10));
		
		Assert.assertEquals(5d, frequency.getEventsPerMinute());
	}
	
	@Test
	public void getEventsPerMinuteWithADot() {
		Frequency frequency = new Frequency(1, Duration.standardMinutes(10));
		
		Assert.assertEquals(0.1d, frequency.getEventsPerMinute());
	}
	
	@Test
	public void getEventsPerHour() {
		Frequency frequency = new Frequency(10, Duration.standardMinutes(60));
		
		Assert.assertEquals(10d, frequency.getEventsPerHour());
	}
}
