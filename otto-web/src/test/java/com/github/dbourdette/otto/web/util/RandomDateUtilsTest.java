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

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Test;

import junit.framework.Assert;

public class RandomDateUtilsTest {
	@Test
	public void today() {
		List<DateTime> dates = new ArrayList<DateTime>();
		
		for (int i = 0; i < 100; i++) {
			DateTime randomDate = RandomDateUtils.today();
			
			Assert.assertFalse("Same date generated twice", dates.contains(randomDate));
			
			Assert.assertTrue(randomDate.isAfter(new DateMidnight().minus(1)));
			Assert.assertTrue(randomDate.isBefore(new DateMidnight().plusDays(1).plus(1)));
			
			dates.add(randomDate);
		}
	}
}
