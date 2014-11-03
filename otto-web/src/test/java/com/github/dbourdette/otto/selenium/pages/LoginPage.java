package com.github.dbourdette.otto.selenium.pages;

import static org.fest.assertions.Assertions.assertThat;

public class LoginPage extends PageSupport {
    @Override
    public String getUrl() {
        return ROOT + "login";
    }

    @Override
    public void isAt() {
        assertThat(title()).isEqualTo("Please login");
    }

    public void doLogin() {
        fill("#username").with("admin");
        fill("#password").with("admin");
        submit("#username");
    }
}
