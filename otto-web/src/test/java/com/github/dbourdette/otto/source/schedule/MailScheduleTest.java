package com.github.dbourdette.otto.source.schedule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class MailScheduleTest {
    @Test
    public void render() {
        MailSchedule mailSchedule = new MailSchedule();
        mailSchedule.setGroovyTemplate("Hello $name");

        Map<String, Object> bindings = new HashMap<String, Object>();
        bindings.put("name", "john");

        assertThat(mailSchedule.render(bindings)).isEqualTo("Hello john");
    }

    @Test
    public void buildTemplate() throws ClassNotFoundException, IOException {
        MailSchedule mailSchedule = new MailSchedule();
        mailSchedule.setGroovyTemplate("Hello $name");

        assertThat(mailSchedule.buildTemplate()).isNotNull();
    }

    @Test
    public void buildDefaultTemplate() throws ClassNotFoundException, IOException {
        MailSchedule mailSchedule = new MailSchedule();

        assertThat(mailSchedule.buildTemplate()).isNotNull();
    }

    @Test
    public void renderDefaultTemplate() {
        MailSchedule mailSchedule = new MailSchedule();

        Map<String, Object> bindings = new HashMap<String, Object>();

        assertThat(mailSchedule.render(bindings)).isNotNull();
    }
}
