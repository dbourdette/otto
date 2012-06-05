package com.github.dbourdette.otto.security;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.bson.types.ObjectId;

import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author damien bourdette
 */
@Entity("otto.config.security")
public class SecurityConfig {
    @Id
    private ObjectId id;

    /**
     * Groovy code for authentication
     */
    private String code;

    public boolean authenticate(String username, String password) {
        if (isEmpty(code)) {
            return false;
        }

        Binding binding = new Binding();
        binding.setVariable("username", username);
        binding.setVariable("password", password);
        GroovyShell shell = new GroovyShell(binding);

        return Boolean.TRUE == shell.evaluate(code);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SecurityConfig withCode(String code) {
        setCode(code);

        return this;
    }

    @Override
    public String toString() {
        return code;
    }
}
