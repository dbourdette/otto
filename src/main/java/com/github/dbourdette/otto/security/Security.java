package com.github.dbourdette.otto.security;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class to check authorizations.
 * Can be used from jsp files.
 *
 * @author damien bourdette
 */
public class Security {
    public static boolean hasSource(String source) {
        Set<String> roles = getAuthoritySet();

        return roles.contains("ROLE_ADMIN") || roles.contains("ROLE_SOURCE_" + source.toUpperCase() + "_USER");
    }

    private static Set<String> getAuthoritySet() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return AuthorityUtils.authorityListToSet(authentication.getAuthorities());
    }
}
