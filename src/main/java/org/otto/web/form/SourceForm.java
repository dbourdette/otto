/**
 * $URL$
 *
 * $LastChangedBy$ - $LastChangedDate$
 */
package org.otto.web.form;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.otto.web.util.SizeInBytes;

import javax.validation.constraints.Pattern;

public class SourceForm {

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String name;

    private String size;

    private Integer maxEvents;

    public SizeInBytes getSizeInBytes() {
        if (StringUtils.isNotEmpty(size)) {
            return SizeInBytes.fromValue(size);
        } else {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxEvents() {
        return maxEvents;
    }

    public void setMaxEvents(Integer maxEventCount) {
        this.maxEvents = maxEventCount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
