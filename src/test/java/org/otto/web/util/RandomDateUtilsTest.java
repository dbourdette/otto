package org.otto.web.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * @author damien bourdette
 */
public class RandomDateUtilsTest {
	@Test
	public void today() {
		List<DateTime> dates = new ArrayList<DateTime>();
		
		for (int i = 0; i < 100; i++) {
			DateTime randomDate = RandomDateUtils.today();
			
			Assert.assertFalse("Same date generated twice", dates.contains(randomDate));
			
			Assert.assertTrue(randomDate.isAfter(new DateMidnight().minus(1)));
			Assert.assertTrue(randomDate.isBefore(new DateTime().plus(1)));
			
			dates.add(randomDate);
		}
	}
}
