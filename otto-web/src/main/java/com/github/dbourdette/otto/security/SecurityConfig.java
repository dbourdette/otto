package com.github.dbourdette.otto.security;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author damien bourdette
 */
@Entity("otto.config.security")
public class SecurityConfig {
    @Id
    private ObjectId id;

    /**
     * Class implementing {@link AuthProviderPlugin}
     */
    private String authProviderClass;

    /**
     * Key values configuration properties for plugin
     */
    private String configuration;

    public Properties getProperties() throws IOException {
        Properties properties = new Properties();

        properties.load(new StringReader(configuration == null ? "" : configuration));

        return properties;
    }

    public AuthProviderPlugin instanciatePlugin() throws Exception {
        if (isEmpty(authProviderClass)) {
            return null;
        }

        AuthProviderPlugin plugin = (AuthProviderPlugin) Class.forName(authProviderClass).newInstance();

        plugin.configure(getProperties());

        return plugin;
    }

    public String getAuthProviderClass() {
        return authProviderClass;
    }

    public void setAuthProviderClass(String authProviderClass) {
        this.authProviderClass = authProviderClass;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
