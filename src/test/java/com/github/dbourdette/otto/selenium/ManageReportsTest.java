package com.github.dbourdette.otto.selenium;

import org.junit.Before;
import org.junit.Test;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ManageReportsTest extends OttoFluentTest {
    @Before
    public void init() {
        deleteSource();

        createSource();
    }

    @Test
    public void createReport() {
        goToHome();

        link(SOURCE_NAME).click();

        $("#sourceConfiguration").click();

        link("add a report").click();

        $("#title").text("selenium source report");
        $("#labelAttributes").text("slug");
        $("#valueAttribute").text("hits");
        $("#form").submit();
    }
}
