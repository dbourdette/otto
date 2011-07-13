/**
 * $URL$
 *
 * $LastChangedBy$ - $LastChangedDate$
 */
package org.otto.web.form;

import org.hibernate.validator.constraints.NotEmpty;

public class EventsForm {

    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
