package com.github.dbourdette.otto.web.service;

import java.util.Map;

/**
 * Provides a facade for posting and retrieving events.
 * It is used by controllers when receiving requests from remote client.
 *
 * @author damien bourdette
 */
public interface RemoteEventsFacade {
    public void post(String sourceName, Map<String, String> params);
}
