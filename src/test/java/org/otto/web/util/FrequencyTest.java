package org.otto.web.util;

import junit.framework.Assert;

import org.joda.time.Duration;
import org.junit.Test;


/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
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
