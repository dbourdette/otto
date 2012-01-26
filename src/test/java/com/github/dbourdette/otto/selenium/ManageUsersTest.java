package com.github.dbourdette.otto.selenium;

import org.junit.Before;
import org.junit.Test;

import static fr.javafreelance.fluentlenium.core.filter.FilterConstructor.withText;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ManageUsersTest extends OttoFluentTest {
    @Before
    public void init() {
        goTo("/users");

        try {
            $("a", withText("selenium user"));

            doDeleteUser();
        } catch (Exception e) {
            // well there was probably no selenium user
        }
    }

    @Test
    public void createUser() {
        $("a", withText("Add user")).click();

        $("#username").text("selenium user");
        $("#sources").text("source");
        $("#form").submit();

        assertThatMessage().isEqualTo("user selenium user has been created");
    }

    @Test
    public void deleteUser() {
        createUser();

        doDeleteUser();
    }

    @Test
    public void updateUser() {
        createUser();

        goTo("/users");

        $("a", withText("selenium user")).click();

        $("#sources").text("source,anothersource");
        $("#form").submit();

        assertThatMessage().isEqualTo("user selenium user has been updated");
    }

    private void doDeleteUser() {
        goTo("/users");

        $("a", withText("selenium user")).click();

        $("#deleteForm").submit();
    }
}
