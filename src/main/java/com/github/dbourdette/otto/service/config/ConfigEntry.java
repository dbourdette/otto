package com.github.dbourdette.otto.service.config;

import com.github.dbourdette.otto.web.util.Constants;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;

/**
 * Created by IntelliJ IDEA.
 * User: dbourdette
 * Date: 30/10/11
 * Time: 23:32
 * To change this template use File | Settings | File Templates.
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
