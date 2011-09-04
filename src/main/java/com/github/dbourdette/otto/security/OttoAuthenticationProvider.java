package com.github.dbourdette.otto.security;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.github.dbourdette.otto.service.user.User;
import com.github.dbourdette.otto.service.user.Users;

/**
 * @author damien bourdette
 */
public class OttoAuthenticationProvider implements AuthenticationProvider {
    @Inject
    private Users users;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("No credentials provided");
        }

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = users.findUserByUsernameAndPassword(username, password);

        if (user == null) {
            throw new BadCredentialsException("Wrong username and / or password");
        }

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("user"));

        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
