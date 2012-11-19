package com.github.dbourdette.otto.selenium.pages;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SourceEditPage extends PageSupport {
    public void updateSource(String displayName, String displayGroup) {
        $("#displayName").text(displayName);
        $("#displayGroup").text(displayGroup);

        $("#form").submit();
    }
}
