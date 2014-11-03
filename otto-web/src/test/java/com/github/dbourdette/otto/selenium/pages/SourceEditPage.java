package com.github.dbourdette.otto.selenium.pages;

public class SourceEditPage extends PageSupport {
    public void updateSource(String displayName, String displayGroup) {
        $("#displayName").text(displayName);
        $("#displayGroup").text(displayGroup);

        $("#form").submit();
    }
}
