package org.otto.web.form;

import javax.validation.constraints.Min;

/**
 * @author damien bourdette
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
