package com.github.dbourdette.otto.selenium;

import org.junit.Before;
import org.junit.Test;

import static fr.javafreelance.fluentlenium.core.filter.FilterConstructor.withText;

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

        $("a", withText("selenium source")).click();

        $("#sourceConfiguration").click();

        $("a", withText("add a report")).click();

        $("#title").text("selenium source report");
        $("#labelAttributes").text("slug");
        $("#valueAttribute").text("hits");
        $("#form").submit();
    }
}
