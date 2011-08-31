package com.github.dbourdette.otto.web.service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
    public void post(String sourceName, HttpServletRequest request) {
        LOGGER.debug("Received event for source " + sourceName);

        @SuppressWarnings("unchecked")
        Event event = Event.fromMap(request.getParameterMap());

        event.setDateIfNoneDefined(new DateTime());

        LOGGER.debug("Saving event " + event);

        sources.getSource(sourceName).post(event);
    }
}
