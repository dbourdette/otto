package com.github.dbourdette.otto.security;

import java.util.Set;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Datastore;

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

    public static boolean hasSource(String source) {
        Set<String> roles = getAuthoritySet();

        return roles.contains("ROLE_ADMIN") || roles.contains("ROLE_SOURCE_" + source.toUpperCase() + "_USER");
    }

    public SecurityConfig fromDb() {
        SecurityConfig config = datastore.find(SecurityConfig.class).get();

        return config == null ? new SecurityConfig() : config;
    }

    public void save(SecurityConfig config) {
        datastore.delete(datastore.find(SecurityConfig.class));

        datastore.save(config);
    }

    public AuthProviderPlugin instanciatePlugin() throws Exception {
        SecurityConfig config = fromDb();

        if (config == null) {
            return null;
        }

        AuthProviderPlugin plugin = (AuthProviderPlugin) Class.forName(config.getAuthProviderClass()).newInstance();

        plugin.configure(config.getProperties());

        return plugin;
    }

    private static Set<String> getAuthoritySet() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return AuthorityUtils.authorityListToSet(authentication.getAuthorities());
    }
}
