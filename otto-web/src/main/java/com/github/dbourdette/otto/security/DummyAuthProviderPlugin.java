package com.github.dbourdette.otto.security;

import java.util.Properties;

/**
 * @author damien bourdette
 */
public class DummyAuthProviderPlugin implements AuthProviderPlugin {
    @Override
    public void configure(Properties properties) {

    }

    @Override
    public boolean authenticate(String user, String password) {
        return true;
    }
}
