package com.github.dbourdette.otto.selenium.pages;

import fr.javafreelance.fluentlenium.core.FluentPage;
import static fr.javafreelance.fluentlenium.core.filter.FilterConstructor.withText;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class UserFormPage extends FluentPage {
    @Override
    public void isAt() {
        assertThat(title()).isEqualTo("Edit user");
    }

    public void createUser(String user, String sources) {
        $("#username").text(user);
        $("#sources").text(sources);
        $("#form").submit();
    }

    public void updateUser(String sources) {
        $("#sources").text(sources);
        $("#form").submit();
    }

    public void deleteUser() {
        $("#deleteForm").submit();
    }

    public void cancel() {
        $("button", withText("Cancel")).click();
    }
}
