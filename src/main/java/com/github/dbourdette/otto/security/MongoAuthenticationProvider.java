package com.github.dbourdette.otto.security;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.github.dbourdette.otto.service.user.User;
import com.github.dbourdette.otto.service.user.Users;

/**
 * Authenticate and load authorities from mongodb.
 * Only try to authenticate user if password is stored in mongodb.
 *
 * @author damien bourdette
 */
public class MongoAuthenticationProvider implements AuthenticationProvider {
    @Inject
    private Users users;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        User user = users.findUserByUsername(username);

        if (user == null) {
            throw new BadCredentialsException("Wrong username and / or password");
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            return null;
        }

        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("Wrong username and / or password");
        }

        String password = authentication.getCredentials().toString();

        if (!user.getPassword().equals(password)) {
            throw new BadCredentialsException("Wrong username and / or password");
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
