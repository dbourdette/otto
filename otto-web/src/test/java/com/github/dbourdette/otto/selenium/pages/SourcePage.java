package com.github.dbourdette.otto.selenium.pages;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SourcePage extends PageSupport {
    public void gotToConfiguration() {
        link("Configuration").click();
    }
}
