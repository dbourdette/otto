package com.github.dbourdette.otto.selenium.pages;

import org.fest.assertions.StringAssert;

import fr.javafreelance.fluentlenium.core.FluentPage;
import fr.javafreelance.fluentlenium.core.domain.FluentWebElement;
import static fr.javafreelance.fluentlenium.core.filter.FilterConstructor.withText;
import static org.fest.assertions.Assertions.assertThat;

public class PageSupport extends FluentPage {
    protected static final String ROOT = "http://localhost:8080";

    protected FluentWebElement link(String text) {
        return $("a", withText(text)).first();
    }

    protected FluentWebElement button(String text) {
        return $("button", withText(text)).first();
    }

    protected StringAssert assertThatMessage() {
        return assertThat($(".message").first().getText());
    }
}
