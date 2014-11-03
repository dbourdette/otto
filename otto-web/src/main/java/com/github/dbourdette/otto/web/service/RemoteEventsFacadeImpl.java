package com.github.dbourdette.otto.web.service;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.dbourdette.otto.service.config.Config;
import com.github.dbourdette.otto.source.Event;
import com.github.dbourdette.otto.source.Source;

/**
 * Provides a facade for posting and retrieving events.
 * It is used by controllers when receiving requests from remote client.
 *
 */
@Service
public class RemoteEventsFacadeImpl implements RemoteEventsFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteEventsFacadeImpl.class);

    @Inject
    private Config config;

    @Async
    public void post(String sourceName, Map<String, String> params) {
        LOGGER.debug("Received event for source " + sourceName);

        try {
            @SuppressWarnings("unchecked")
            Event event = Event.fromMap(params);

            event.setDateIfNoneDefined(new DateTime());

            LOGGER.debug("Saving event " + event);

            Source.findByName(sourceName).post(event);

            onEvent(sourceName);
        } catch (Exception e) {
            LOGGER.error("Failed to handle event", e);
        }
    }

    public void onEvent(String sourceName) {
        String monitoringSource = config.get(Config.MONITORING_SOURCE);

        if (StringUtils.isNotEmpty(monitoringSource)) {
            Event event = new Event().putString("source", sourceName);

            event.setDate(new DateTime());

            Source.findByName(monitoringSource).post(event);
        }
    }
}
