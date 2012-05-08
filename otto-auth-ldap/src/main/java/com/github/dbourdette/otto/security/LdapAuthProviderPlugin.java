package com.github.dbourdette.otto.security;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

/**
 * @author damien bourdette
 */
public class LdapAuthProviderPlugin implements AuthProviderPlugin {

    public static final String PROVIDER_URL = "providerUrl";

    public static final String USER_DN = "userDn";

    public static final String PASSWORD = "password";

    public static final String SEARCH_BASE = "searchBase";

    public static final String SEARCH_FILTER = "searchFilter";

    private LdapAuthenticationProvider ldapAuthenticationProvider;

    @Override
    public void configure(Properties properties) throws Exception {
        LdapContextSource source = buildLdapContextSource(properties);

        BindAuthenticator authenticator = new BindAuthenticator(source);
        authenticator.setUserSearch(buildLdapUserSearch(properties, source));
        authenticator.afterPropertiesSet();

        ldapAuthenticationProvider = new LdapAuthenticationProvider(authenticator, new LdapAuthoritiesPopulator() {
            @Override
            public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
                return new ArrayList<GrantedAuthority>();
            }
        });
    }

    @Override
    public boolean authenticate(String user, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, password);

        try {
            return ldapAuthenticationProvider.authenticate(token) != null;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    private LdapContextSource buildLdapContextSource(Properties properties) throws Exception {
        DefaultSpringSecurityContextSource source = new DefaultSpringSecurityContextSource(properties.getProperty(PROVIDER_URL));

        source.setUserDn(properties.getProperty(USER_DN));
        source.setPassword(properties.getProperty(PASSWORD));
        source.afterPropertiesSet();

        return source;
    }

    private LdapUserSearch buildLdapUserSearch(Properties properties, LdapContextSource source) throws Exception {
        return new FilterBasedLdapUserSearch(properties.getProperty(SEARCH_BASE), properties.getProperty(SEARCH_FILTER), source);
    }
}
