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

def ldapUserDn = ''
def ldapProviderUrl = 'ldap://xxx'
def ldapPassword = ''
def ldapSearchBase = ''
def ldapSearchFilter = ''

// source
DefaultSpringSecurityContextSource source = new DefaultSpringSecurityContextSource(ldapProviderUrl);
source.setUserDn(ldapUserDn);
source.setPassword(ldapPassword);
source.afterPropertiesSet();

// LdapUserSearch
FilterBasedLdapUserSearch ldapUserSearch = new FilterBasedLdapUserSearch(ldapSearchBase, ldapSearchFilter, source);

BindAuthenticator authenticator = new BindAuthenticator(source);
authenticator.setUserSearch(ldapUserSearch);
authenticator.afterPropertiesSet();

ldapAuthenticationProvider = new LdapAuthenticationProvider(authenticator, new LdapAuthoritiesPopulator() {
    @Override
    public Collection getGrantedAuthorities(DirContextOperations userData, String username) {
        return new ArrayList();
    }
});

UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

try {
    return ldapAuthenticationProvider.authenticate(token) != null;
} catch (AuthenticationException e) {
    return false;
}

