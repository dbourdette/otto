package com.github.dbourdette.otto.security;

import com.github.dbourdette.otto.SpringConfig;
import com.github.dbourdette.otto.service.user.User;
import com.google.code.morphia.Datastore;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

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

    public static boolean hasSource(String source) {
        Set<String> roles = getAuthoritySet();

        return roles.contains("ROLE_ADMIN") || roles.contains("ROLE_SOURCE_" + source.toUpperCase() + "_USER");
    }

    public boolean authenticate(String username, String password) {
        return getConfig().authenticate(username, password);
    }

    public SecurityConfig getConfig() {
        SecurityConfig config = datastore.find(SecurityConfig.class).get();

        return config == null ? new SecurityConfig() : config;
    }

    public void save(SecurityConfig config) throws Exception {
        datastore.delete(datastore.find(SecurityConfig.class));

        if (isNotEmpty(config.getCode())) {
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
