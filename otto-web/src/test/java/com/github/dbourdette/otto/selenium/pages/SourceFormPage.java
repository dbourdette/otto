package com.github.dbourdette.otto.selenium.pages;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SourceFormPage extends PageSupport {
    @Override
    public void isAt() {
        assertThat(title()).isEqualTo("Edit source");
    }

    public void createSource(String name) {
        $("#name").text(name);
        $("#form").submit();
    }
}
