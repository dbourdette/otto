package com.github.dbourdette.otto.selenium;

import org.junit.After;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ManageSourceTest extends OttoFluentTest {
    @After
    public void deleteSource() {
        super.deleteSource();
    }

    @Test
    public void createSource() {
        super.createSource();
    }
    
    @Test
    public void displayName() {
        super.createSource();

        goTo("/sources/" + SOURCE_NAME + "/edit");

        $("#displayName").text(SOURCE_DISPLAY_NAME);
        $("#displayGroup").text("My display group");

        $("#form").submit();

        goToHome();
        link(SOURCE_DISPLAY_NAME).click();

        checkH1();

        $("#sourceConfiguration").click();
        checkH1();

        $("#sourceStatistics").click();
        checkH1();

        $("#sourceLogs").click();
        checkH1();

        $("#sourceBatch").click();
        checkH1();
    }

    private void checkH1() {
        assertThat($("h1").first().getText()).isEqualTo("Source My display group / My display name edit");
    }
}
