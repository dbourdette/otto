package com.github.dbourdette.otto.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ReadReportTest extends OttoFluentTest {
    @Before
    public void init() {
        deleteSource();

        createSource();
    }

    @After
    public void clear() {
        deleteSource();
    }

    @Test
    public void readStats() throws InterruptedException {
        goTo("/sources/" + SOURCE_NAME + "/events/batch");

        $("#count").text("1000");
        $("input[value=RANDOM_TODAY]").click();
        $("#values").text("url=http://test.com/path");
        $("#form").submit();

        goTo("/sources/" + SOURCE_NAME + "/reports/stats?period=TODAY");

        assertThat($("#total").first().getText()).isEqualTo("1000");
    }

}
