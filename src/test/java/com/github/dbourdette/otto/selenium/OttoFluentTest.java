package com.github.dbourdette.otto.selenium;

import org.fest.assertions.StringAssert;

import fr.javafreelance.fluentlenium.core.domain.FluentList;
import fr.javafreelance.fluentlenium.core.domain.FluentWebElement;
import static fr.javafreelance.fluentlenium.core.filter.FilterConstructor.withText;
import fr.javafreelance.fluentlenium.core.test.FluentTest;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class OttoFluentTest extends FluentTest {
    public static final String SOURCE_NAME = "selenium source";

    public static final String SOURCE_DISPLAY_NAME = "My display name";
    
    public void goToHome() {
        goTo("/");
    }

    @Override
    public void goTo(String url) {
        if (!url.startsWith("http")) {
            url = "http://admin:tomlechat@localhost:8080" + url;
        }

        super.goTo(url);
    }

    /**
     * Creates a test source
     */
    protected void createSource() {
        goToHome();

        link("create a new event source").click();

        $("#name").text(SOURCE_NAME);
        $("#form").submit();
    }

    /**
     * deletes our test source if any
     */
    protected void deleteSource() {
        goToHome();

        FluentList deleteLinks = $("a", withText(SOURCE_NAME));

        if (deleteLinks.size() == 0) {
            deleteLinks = $("a", withText(SOURCE_DISPLAY_NAME));

            if (deleteLinks.size() == 0) {
                return;
            }
        }

        deleteLinks.click();

        $("#sourceConfiguration").click();

        link("delete source").click();

        $("#deleteForm").submit();
    }
    
    protected FluentWebElement link(String text) {
        return $("a", withText(text)).first();
    }

    protected StringAssert assertThatMessage() {
        return assertThat($(".message").first().getText());
    }
}
