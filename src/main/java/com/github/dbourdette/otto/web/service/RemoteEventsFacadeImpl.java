package com.github.dbourdette.otto.web.service;

import java.util.Map;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.dbourdette.otto.source.Event;
import com.github.dbourdette.otto.source.Sources;

/**
 * Provides a facade for posting and retrieving events.
 * It is used by controllers when receiving requests from remote client.
 *
 * @author damien bourdette
 */
@Service
public class RemoteEventsFacadeImpl implements RemoteEventsFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteEventsFacadeImpl.class);

    @Inject
    private Sources sources;

    @Async
    public void post(String sourceName, Map<String, String> params) {
        LOGGER.debug("Received event for source " + sourceName);

        try {
            @SuppressWarnings("unchecked")
            Event event = Event.fromMap(params);

            event.setDateIfNoneDefined(new DateTime());

            LOGGER.debug("Saving event " + event);

            sources.getSource(sourceName).post(event);
        } catch (Exception e) {
            LOGGER.error("Failed to handle event", e);
        }

    }
}
