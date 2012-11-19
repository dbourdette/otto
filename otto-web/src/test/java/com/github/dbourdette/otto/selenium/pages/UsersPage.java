package com.github.dbourdette.otto.selenium.pages;

import fr.javafreelance.fluentlenium.core.domain.FluentWebElement;
import static fr.javafreelance.fluentlenium.core.filter.FilterConstructor.withText;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class UsersPage extends PageSupport {
    @Override
    public String getUrl() {
        return ROOT + "/users";
    }

    @Override
    public void isAt() {
        assertThat(title()).isEqualTo("All users");
    }

    public FluentWebElement addUserButton() {
        return link("Add user");
    }

    public boolean hasUser(String name) {
        return $("a", withText(name)).size() > 0;
    }

    public void pickUser(String name) {
        link(name).click();
    }

    public void assertUserCreated() {
        assertThatMessage().isEqualTo("user selenium user has been created");
    }

    public void assertUserUpdated() {
        assertThatMessage().isEqualTo("user selenium user has been updated");
    }
}
