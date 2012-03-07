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

import com.github.dbourdette.otto.SpringConfig;
import com.github.dbourdette.otto.service.user.User;
import com.github.dbourdette.otto.service.user.Users;
import net.jradius.client.RadiusClient;
import net.jradius.client.auth.PAPAuthenticator;
import net.jradius.dictionary.Attr_UserName;
import net.jradius.dictionary.Attr_UserPassword;
import net.jradius.packet.AccessAccept;
import net.jradius.packet.AccessRequest;
import net.jradius.packet.RadiusPacket;
import net.jradius.packet.attribute.AttributeFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.inject.Inject;
import java.net.InetAddress;

/**
 * Facade for authentication
 *
 * @author damien bourdette
 */
public class RadiusAuthenticationProvider implements AuthenticationProvider {
    private static final int RETRIES = 2;

    @Inject
    private Users users;

    @Inject
    private SpringConfig springConfig;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (StringUtils.isEmpty(springConfig.getSecurityRadiusHost())) {
            return null;
        }
        
        String username = authentication.getName();

        User user = users.findUserByUsername(username);

        if (user == null) {
            throw new BadCredentialsException("Wrong username and / or password");
        }

        String password = authentication.getCredentials().toString();

        if (radius(username, password)) {
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Wrong username and / or password");
        }
    }

    public boolean radius(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return false;
        }

        AttributeFactory.loadAttributeDictionary("net.jradius.dictionary.AttributeDictionaryImpl");

        RadiusClient radiusClient = null;

        try {
            InetAddress address = InetAddress.getByName(springConfig.getSecurityRadiusHost());

            radiusClient = new RadiusClient(address, springConfig.getSecurityRadiusSecret());
            radiusClient.setAuthPort(Integer.valueOf(springConfig.getSecurityRadiusPort()));

            AccessRequest request = new AccessRequest(radiusClient);
            request.addAttribute(new Attr_UserName(username));
            request.addAttribute(new Attr_UserPassword(password));

            RadiusPacket reply = radiusClient.authenticate(request, new PAPAuthenticator(), RETRIES);

            if (reply == null) {
                throw new IllegalStateException("Failed to query properly radius server");
            }

            return reply instanceof AccessAccept;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to query properly radius server", e);
        } finally {
            if (radiusClient != null) {
                radiusClient.close();
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
