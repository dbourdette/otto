package com.github.dbourdette.otto.security;

import com.github.dbourdette.otto.SpringConfig;
import com.github.dbourdette.otto.service.logs.Logs;
import com.github.dbourdette.otto.service.user.User;
import com.google.code.morphia.Datastore;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * Utility class to check authorizations.
 * Can be used from jsp files.
 *
 * @author damien bourdette
 */
@Component
public class Security {
    @Inject
    private Datastore datastore;

    @Inject
    private SpringConfig springConfig;

    @Inject
    private Logs logs;

    /**
     * Currently configured plugin
     */
    public static volatile AuthProviderPlugin plugin;

    public static boolean hasSource(String source) {
        Set<String> roles = getAuthoritySet();

        return roles.contains("ROLE_ADMIN") || roles.contains("ROLE_SOURCE_" + source.toUpperCase() + "_USER");
    }

    @PostConstruct
    public void loadPlugin() {
        SecurityConfig config = fromDb();

        if (config == null || isEmpty(config.getAuthProviderClass())) {
            return;
        }

        try {
            plugin = config.instanciatePlugin();
        } catch (Exception e) {
            logs.error("Failed to load auth plugin", e);
        }
    }

    public SecurityConfig fromDb() {
        SecurityConfig config = datastore.find(SecurityConfig.class).get();

        return config == null ? new SecurityConfig() : config;
    }

    public void save(SecurityConfig config) throws Exception {
        if (isEmpty(config.getAuthProviderClass())) {
            datastore.delete(datastore.find(SecurityConfig.class));
        } else {
            plugin = config.instanciatePlugin();

            datastore.delete(datastore.find(SecurityConfig.class));

            datastore.save(config);
        }
    }

    /**
     * Reads admin user form spring config
     */
    public User getAdminUser() {
        if (StringUtils.isEmpty(springConfig.getSecurityAdminUsername()) || StringUtils.isEmpty(springConfig.getSecurityAdminPassword())) {
            return null;
        }

        User user = new User();

        user.setAdmin(true);
        user.setUsername(springConfig.getSecurityAdminUsername());
        user.setPassword(springConfig.getSecurityAdminPassword());

        return user;
    }

    /**
     * If an admin account is specified, check for it.
     */
    public boolean isAdminAccount(String username, String password) {
        User user = getAdminUser();

        return user != null && user.getUsername().equals(username) && user.getPassword().equals(password);
    }

    private static Set<String> getAuthoritySet() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return AuthorityUtils.authorityListToSet(authentication.getAuthorities());
    }
}
