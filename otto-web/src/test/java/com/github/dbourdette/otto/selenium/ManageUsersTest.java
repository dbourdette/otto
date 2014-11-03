package com.github.dbourdette.otto.selenium;

import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.otto.selenium.pages.LoginPage;
import com.github.dbourdette.otto.selenium.pages.UserFormPage;
import com.github.dbourdette.otto.selenium.pages.UsersPage;

import fr.javafreelance.fluentlenium.core.annotation.Page;

public class ManageUsersTest extends OttoFluentTest {
    private static final String TEST_USER = "selenium user";

    @Page
    private LoginPage loginPage;

    @Page
    private UsersPage usersPage;

    @Page
    private UserFormPage userFormPage;

    @Before
    public void init() {
        goTo(usersPage);

        loginPage.isAt();
        loginPage.doLogin();

        usersPage.isAt();

        if (usersPage.hasUser(TEST_USER)) {
            doDeleteUser(TEST_USER);
        }
    }

    @Test
    public void createUser() {
        usersPage.isAt();
        usersPage.addUserButton().click();

        userFormPage.isAt();
        userFormPage.createUser(TEST_USER, "source");

        usersPage.assertUserCreated();
    }

    @Test
    public void deleteUser() {
        createUser();

        doDeleteUser(TEST_USER);
    }

    @Test
    public void updateUser() {
        createUser();

        usersPage.isAt();
        usersPage.pickUser(TEST_USER);

        userFormPage.isAt();
        userFormPage.updateUser("source,anothersource");

        usersPage.assertUserUpdated();
    }

    @Test
    public void cancelUpdateUser() {
        createUser();

        usersPage.isAt();
        usersPage.pickUser(TEST_USER);

        userFormPage.isAt();
        userFormPage.cancel();

        usersPage.isAt();
    }

    private void doDeleteUser(String name) {
        usersPage.isAt();
        usersPage.pickUser(TEST_USER);

        userFormPage.deleteUser();
    }
}
