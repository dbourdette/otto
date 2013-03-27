package com.github.dbourdette.otto.source;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.fest.assertions.Assertions.assertThat;

public class RawEventsQueryTest {

    @Test
    public void getQueryParams() throws UnsupportedEncodingException {
        RawEventsQuery query = new RawEventsQuery();
        query.setQuery("{\"name\":\"to<to\"}");
        query.setSort("{\"date\":-1}");

        assertThat(query.getQueryParams()).isEqualTo(("query={&quot;name&quot;:&quot;to&lt;to&quot;}&amp;sort={&quot;date&quot;:-1}"));
    }
}
