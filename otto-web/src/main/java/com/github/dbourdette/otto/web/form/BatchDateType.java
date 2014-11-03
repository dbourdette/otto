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

package com.github.dbourdette.otto.web.form;

import org.joda.time.DateTime;
import com.github.dbourdette.otto.web.util.RandomDateUtils;

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
