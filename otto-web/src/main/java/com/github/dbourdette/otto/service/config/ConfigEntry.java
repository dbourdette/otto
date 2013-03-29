package com.github.dbourdette.otto.service.config;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;

/**
 * Global configuration entry
 *
 * @author damien bourdette
 */
@Entity("otto.config.entries")
public class ConfigEntry {
    @Property
    private String key;

    @Property
    private String value;

    public ConfigEntry() {
    }

    public ConfigEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
