package com.github.dbourdette.otto.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.otto.selenium.pages.HomePage;
import com.github.dbourdette.otto.selenium.pages.LoginPage;
import com.github.dbourdette.otto.selenium.pages.SourceAggregationForm;
import com.github.dbourdette.otto.selenium.pages.SourceConfigurationPage;
import com.github.dbourdette.otto.selenium.pages.SourceEditPage;
import com.github.dbourdette.otto.selenium.pages.SourceFormPage;
import com.github.dbourdette.otto.selenium.pages.SourcePage;
import com.github.dbourdette.otto.source.TimeFrame;
import com.github.dbourdette.otto.source.config.AggregationConfig;

import fr.javafreelance.fluentlenium.core.annotation.Page;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ManageSourceTest extends OttoFluentTest {
    @Page
    private HomePage homePage;

    @Page
    private LoginPage loginPage;

    @Page
    private SourcePage sourcePage;

    @Page
    private SourceFormPage sourceFormPage;

    @Page
    private SourceEditPage sourceEditPage;

    @Page
    private SourceConfigurationPage sourceConfigurationPage;

    @Page
    private SourceAggregationForm sourceAggregationForm;

    @Before
    public void init() {
        goTo(homePage);

        loginPage.isAt();
        loginPage.doLogin();

        homePage.isAt();

        deleteSource();
    }

    @After
    public void deleteSource() {
        if (homePage.hasSource(SOURCE_NAME, SOURCE_DISPLAY_NAME)) {
            goTo(sourceConfigurationPage.getUrl(SOURCE_NAME));
            sourceConfigurationPage.delete();

            homePage.isAt();
        }
    }

    @Test
    public void createSource() {
        homePage.newSourceButton().click();

        sourceFormPage.createSource(SOURCE_NAME);
    }
    
    @Test
    public void updateDisplayName() {
        createSource();

        goTo(sourceConfigurationPage.getUrl(SOURCE_NAME));

        sourceConfigurationPage.edit();

        sourceEditPage.updateSource(SOURCE_DISPLAY_NAME, "My display group");

        goTo(homePage);

        assertThat(homePage.hasSource(SOURCE_DISPLAY_NAME, SOURCE_DISPLAY_NAME)).isTrue();
    }

    @Test
    public void setupAggregation() {
        createSource();

        goTo(sourceAggregationForm.getUrl(SOURCE_NAME));

        AggregationConfig config = new AggregationConfig();
        config.setTimeFrame(TimeFrame.ONE_MINUTE);
        config.setAttributeName("hits");
        sourceAggregationForm.update(config);
    }
}
