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

import javax.validation.constraints.Min;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class BatchForm {
	private String values;
	
	private BatchValuesType valuesType = BatchValuesType.KEY_VALUES;
	
	private BatchDateType dateType = BatchDateType.CURRENT;

	@Min(1)
	private Integer count = 1;

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public BatchValuesType getValuesType() {
		return valuesType;
	}

	public void setValuesType(BatchValuesType valuesType) {
		this.valuesType = valuesType;
	}

	public BatchDateType getDateType() {
		return dateType;
	}

	public void setDateType(BatchDateType dateType) {
		this.dateType = dateType;
	}
}
