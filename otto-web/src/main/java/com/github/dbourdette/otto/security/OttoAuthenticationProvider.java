package com.github.dbourdette.otto.security;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class OttoAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(OttoAuthenticationProvider.class);

    @Inject
    private Users users;

    @Inject
    private Security security;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        User user = users.findUserByUsername(username);

        if (user == null) {
            throw new BadCredentialsException("Wrong username and / or password");
        }

        String password = authentication.getCredentials().toString();

        if (checkPassword(user, password)) {
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Wrong username and / or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private boolean checkPassword(User user, String password) {
        if (StringUtils.isEmpty(user.getPassword())) {
            return checkPluginPassword(user, password);
        } else {
            return checkUserPassword(user, password);
        }
    }

    private boolean checkUserPassword(User user, String password) {
        return !StringUtils.isEmpty(password) && password.equals(user.getPassword());
    }

    private boolean checkPluginPassword(User user, String password) {
        AuthProviderPlugin plugin = null;

        try {
            plugin = security.instanciatePlugin();
        } catch (Exception e) {
            LOGGER.error("Failed to instanciate auth plugin", e);
        }

        return plugin != null && plugin.authenticate(user.getUsername(), password);
    }
}
