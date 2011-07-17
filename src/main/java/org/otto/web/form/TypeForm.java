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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
