package com.github.dbourdette.otto.security;

import java.util.Properties;

/**
 * @author damien bourdette
 */
public interface AuthProviderPlugin {
    public void configure(Properties properties) throws Exception;

    public boolean authenticate(String user, String password);
}
