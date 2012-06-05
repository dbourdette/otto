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

import net.jradius.client.RadiusClient;
import net.jradius.client.auth.PAPAuthenticator;
import net.jradius.dictionary.Attr_UserName;
import net.jradius.dictionary.Attr_UserPassword;
import net.jradius.packet.AccessAccept;
import net.jradius.packet.AccessRequest;
import net.jradius.packet.RadiusPacket;
import net.jradius.packet.attribute.AttributeFactory;

if (!username || !password) {
    return false;
}

def host = 'webradius.rtl.fr'
def secret = 'testing123'
def port = 1812

AttributeFactory.loadAttributeDictionary("net.jradius.dictionary.AttributeDictionaryImpl");

RadiusClient radiusClient = null;

try {
    InetAddress address = InetAddress.getByName(host);

    radiusClient = new RadiusClient(address, secret);
    radiusClient.setAuthPort(port);

    AccessRequest request = new AccessRequest(radiusClient);
    request.addAttribute(new Attr_UserName(username));
    request.addAttribute(new Attr_UserPassword(password));

    int retries = 2;

    RadiusPacket reply = radiusClient.authenticate(request, new PAPAuthenticator(), retries);

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