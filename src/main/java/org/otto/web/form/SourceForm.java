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

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
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
