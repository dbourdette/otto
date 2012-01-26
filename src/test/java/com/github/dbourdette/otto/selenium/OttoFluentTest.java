package com.github.dbourdette.otto.selenium;

import org.fest.assertions.StringAssert;

import static fr.javafreelance.fluentlenium.core.filter.FilterConstructor.withText;
import fr.javafreelance.fluentlenium.core.test.FluentTest;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class OttoFluentTest extends FluentTest {
    public static final String SOURCE_NAME = "selenium source";
    
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

        $("a", withText("create a new event source")).click();

        $("#name").text(SOURCE_NAME);
        $("#form").submit();
    }

    /**
     * deletes our test source if any
     */
    protected void deleteSource() {
        goToHome();

        if ($("a", withText(SOURCE_NAME)).size() == 0) {
            return;
        }

        $("a", withText(SOURCE_NAME)).click();

        $("#sourceConfiguration").click();

        $("a", withText("delete source")).click();

        $("#deleteForm").submit();
    }

    protected StringAssert assertThatMessage() {
        return assertThat($(".message").first().getText());
    }
}
