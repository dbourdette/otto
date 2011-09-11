/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dbourdette.otto.security;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import com.github.dbourdette.otto.SpringConfig;
import com.github.dbourdette.otto.service.user.User;
import com.github.dbourdette.otto.service.user.Users;

/**
 * Authenticate user through ldap and load authorities from mongodb.
 * Only works if ldap parameters have been supplied.
 *
 * @author damien bourdette
 */
public class LdapAndMongoAuthenticationProvider implements AuthenticationProvider, LdapAuthoritiesPopulator {
    @Inject
    private Users users;

    @Inject
    private SpringConfig springConfig;

    private LdapAuthenticationProvider ldapAuthenticationProvider;

    @PostConstruct
    public void initLdap() throws Exception {
        String providerUrl = springConfig.getSecurityLdapProviderUrl();
        String userDn = springConfig.getSecurityLdapUserDn();
        String password = springConfig.getSecurityLdapPassword();

        if (StringUtils.isEmpty(providerUrl)) {
            return;
        }

        DefaultSpringSecurityContextSource defaultSpringSecurityContextSource = new DefaultSpringSecurityContextSource(providerUrl);
        defaultSpringSecurityContextSource.setUserDn(userDn);
        defaultSpringSecurityContextSource.setPassword(password);
        defaultSpringSecurityContextSource.afterPropertiesSet();

        FilterBasedLdapUserSearch ldapUserSearch = new FilterBasedLdapUserSearch(
                springConfig.getSecurityLdapSearchBase(),
                springConfig.getSecurityLdapSearchFilter(),
                defaultSpringSecurityContextSource);

        BindAuthenticator authenticator = new BindAuthenticator(defaultSpringSecurityContextSource);
        authenticator.setUserSearch(ldapUserSearch);
        authenticator.afterPropertiesSet();

        ldapAuthenticationProvider = new LdapAuthenticationProvider(authenticator, this);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (ldapAuthenticationProvider == null) {
            return null;
        }

        String username = authentication.getName();

        User user = users.findUserByUsername(username);

        if (user == null) {
            throw new BadCredentialsException("Wrong username and / or password");
        }

        return ldapAuthenticationProvider.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
        return users.findUserByUsername(username).getAuthorities();
    }
}
