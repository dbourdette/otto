package org.otto.web.form;

import org.joda.time.DateTime;
import org.otto.web.util.RandomDateUtils;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public enum BatchDateType {
	CURRENT, RANDOM_TODAY, RANDOM_LAST_7_DAYS;
	
	public DateTime instanciateDate() {
		switch (this) {
		case RANDOM_TODAY:
			return RandomDateUtils.today();
		case RANDOM_LAST_7_DAYS:
			return RandomDateUtils.last7Days();
		default:
			return new DateTime();
		}
	}
}
