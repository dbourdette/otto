package com.github.dbourdette.otto.selenium;

import org.junit.Before;
import org.junit.Test;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ManageUsersTest extends OttoFluentTest {
    @Before
    public void init() {
        goTo("/users");

        try {
            link("selenium user");

            doDeleteUser();
        } catch (Exception e) {
            // well there was probably no selenium user
        }
    }

    @Test
    public void createUser() {
        link("Add user").click();

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

        link("selenium user").click();

        $("#sources").text("source,anothersource");
        $("#form").submit();

        assertThatMessage().isEqualTo("user selenium user has been updated");
    }

    private void doDeleteUser() {
        goTo("/users");

        link("selenium user").click();

        $("#deleteForm").submit();
    }
}
