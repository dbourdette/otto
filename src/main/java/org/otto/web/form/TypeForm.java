/**
 * $URL$
 *
 * $LastChangedBy$ - $LastChangedDate$
 */
package org.otto.web.form;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class TypeForm {

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String name;
    
    private Integer sizeInBytes;
    
    private Integer maxEvents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Integer getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(Integer sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}

	public Integer getMaxEvents() {
		return maxEvents;
	}

	public void setMaxEvents(Integer maxEventCount) {
		this.maxEvents = maxEventCount;
	}

}
