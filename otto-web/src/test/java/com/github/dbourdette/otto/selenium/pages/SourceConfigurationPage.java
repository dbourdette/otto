package com.github.dbourdette.otto.selenium.pages;

import static fr.javafreelance.fluentlenium.core.filter.FilterConstructor.withId;

public class SourceConfigurationPage extends PageSupport {
    public String getUrl(String name) {
        return ROOT + "/sources/" + name + "/configuration";
    }

    public void delete() {
        link("Delete").click();

        $("#deleteForm").submit();
    }

    public void edit() {
        $("a", withId("edit")).click();
    }
}
