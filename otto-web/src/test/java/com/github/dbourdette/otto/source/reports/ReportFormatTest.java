package com.github.dbourdette.otto.source.reports;

import java.io.IOException;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ReportFormatTest {
    @Test
    public void loadDefault() throws IOException {
        assertThat(ReportFormat.loadDefault("pie", "google-pie").getCode()).isNotEmpty();
    }
}
