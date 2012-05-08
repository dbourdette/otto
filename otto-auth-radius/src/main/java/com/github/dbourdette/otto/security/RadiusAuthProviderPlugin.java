package com.github.dbourdette.otto.security;

import java.net.InetAddress;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import net.jradius.client.RadiusClient;
import net.jradius.client.auth.PAPAuthenticator;
import net.jradius.dictionary.Attr_UserName;
import net.jradius.dictionary.Attr_UserPassword;
import net.jradius.packet.AccessAccept;
import net.jradius.packet.AccessRequest;
import net.jradius.packet.RadiusPacket;
import net.jradius.packet.attribute.AttributeFactory;

/**
 * @author damien bourdette
 */
public class RadiusAuthProviderPlugin implements AuthProviderPlugin {
    private static final int RETRIES = 2;

    private Properties properties;

    public static final String HOST = "host";

    public static final String PORT = "port";

    public static final String SECRET = "secret";

    @Override
    public void configure(Properties properties) throws Exception {
        this.properties = properties;
    }

    @Override
    public boolean authenticate(String user, String password) {
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) {
            return false;
        }

        AttributeFactory.loadAttributeDictionary("net.jradius.dictionary.AttributeDictionaryImpl");

        RadiusClient radiusClient = null;

        try {
            InetAddress address = InetAddress.getByName(properties.getProperty(HOST));

            radiusClient = new RadiusClient(address, properties.getProperty(SECRET));
            radiusClient.setAuthPort(Integer.valueOf(properties.getProperty(PORT)));

            AccessRequest request = new AccessRequest(radiusClient);
            request.addAttribute(new Attr_UserName(user));
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
}
