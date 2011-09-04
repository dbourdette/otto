package com.github.dbourdette.otto.integration;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author damien bourdette
 */
public class PostEventTest {

    //private String url = "http://otto.dbourdette.cloudbees.net/sources/test/events";

    private String url = "http://localhost:8080/api/sources/test/events";

    private RestTemplate template = new RestTemplate();

    @Test
    public void post() throws IOException, InterruptedException {
        int count = getEventCount();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("name", "toto");
        template.postForLocation(url, params);

        Thread.sleep(1000);

        Assert.assertEquals("There should one more event", count + 1, getEventCount());
    }

    public int getEventCount() throws IOException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept", "application/json");
        HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
        ResponseEntity<String> entity = template.exchange(url, HttpMethod.GET, requestEntity, String.class);

        return new ObjectMapper().readTree(entity.getBody()).get("count").getIntValue();
    }
}
