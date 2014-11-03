package com.github.dbourdette.otto.selenium.pages;

public class SourcePage extends PageSupport {
    public void gotToConfiguration() {
        link("Configuration").click();
    }
}
