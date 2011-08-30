package org.otto.web.service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.otto.source.Event;
import org.otto.source.Sources;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Provides a facade for posting and retrieving events.
 * It is used by controllers when receiving requests from remote client.
 *
 * @author damien bourdette
 */
@Service
public class RemoteEventsFacadeImpl implements RemoteEventsFacade {
    @Inject
    private Sources sources;

    @Async
    public void post(String sourceName, HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Event event = Event.fromMap(request.getParameterMap());

        event.setDateIfNoneDefined(new DateTime());

        sources.getSource(sourceName).post(event);
    }
}
