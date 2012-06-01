package com.github.dbourdette.otto.security;

import com.github.dbourdette.otto.service.user.User;
import com.github.dbourdette.otto.service.user.Users;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.inject.Inject;

/**
 * Authenticate and load authorities from mongodb.
 * Only try to authenticate user if password is stored in mongodb.
 *
 * @author damien bourdette
 */
public class OttoAuthenticationProvider implements AuthenticationProvider {

    @Inject
    private Users users;

    @Inject
    private Security security;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = null;

        if (security.isAdminAccount(username, password)) {
            user = security.getAdminUser();
        } else {
            user = users.findUserByUsername(username);

            if (user == null) {
                throw new BadCredentialsException("Wrong username and / or password");
            }

            if (!checkPassword(user, password)) {
                throw new BadCredentialsException("Wrong username and / or password");
            }
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
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
        return Security.plugin != null && Security.plugin.authenticate(user.getUsername(), password);
    }
}
