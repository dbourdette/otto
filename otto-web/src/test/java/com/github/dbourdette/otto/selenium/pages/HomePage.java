package com.github.dbourdette.otto.selenium.pages;

import fr.javafreelance.fluentlenium.core.domain.FluentWebElement;
import static fr.javafreelance.fluentlenium.core.filter.FilterConstructor.withText;
import static org.fest.assertions.Assertions.assertThat;

public class HomePage extends PageSupport {
    @Override
    public String getUrl() {
        return ROOT;
    }

    @Override
    public void isAt() {
        assertThat(title()).isEqualTo("Otto event server");
    }

    public FluentWebElement newSourceButton() {
        return button("new source");
    }

    public boolean hasSource(String name, String displayName) {
        return $("a", withText(name)).size() > 0 || $("a", withText(displayName)).size() > 0;
    }
}
