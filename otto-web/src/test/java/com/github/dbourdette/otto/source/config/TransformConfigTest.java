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

package com.github.dbourdette.otto.source.config;

import org.junit.Assert;
import org.junit.Test;

import com.github.dbourdette.otto.source.Event;
import com.github.dbourdette.otto.source.config.transform.LowerCaseOperation;
import com.github.dbourdette.otto.source.config.transform.NoAccentOperation;

public class TransformConfigTest {
    private TransformConfig config = new TransformConfig();

    private Event event = Event.fromKeyValues("key1=toto,key2=titi");

    @Test
    public void noop() {
        config.applyOn(event);

        Assert.assertEquals("toto", event.get("key1"));
        Assert.assertEquals("titi", event.get("key2"));
    }

    @Test
    public void applyOn() {
        DummyOperation dummyOp = new DummyOperation();

        config.forParam("key1").add(dummyOp);

        config.applyOn(event);

        Assert.assertEquals(1, dummyOp.callCount);
    }

    @Test
    public void parseOperations() {
        Assert.assertEquals(0, config.parseOperations("").size());
        Assert.assertEquals(0, config.parseOperations(null).size());

        Assert.assertEquals(1, config.parseOperations("lower").size());
        Assert.assertEquals(LowerCaseOperation.class, config.parseOperations("lower").get(0).getClass());

        Assert.assertEquals(2, config.parseOperations("lower,noaccent").size());
        Assert.assertEquals(NoAccentOperation.class, config.parseOperations("lower,noaccent").get(1).getClass());

        Assert.assertEquals(1, config.parseOperations("lower,what").size());
        Assert.assertEquals(LowerCaseOperation.class, config.parseOperations("lower,what").get(0).getClass());

        Assert.assertEquals(2, config.parseOperations(" lower , noaccent").size());
        Assert.assertEquals(LowerCaseOperation.class, config.parseOperations(" lower , noaccent").get(0).getClass());
        Assert.assertEquals(NoAccentOperation.class, config.parseOperations(" lower , noaccent").get(1).getClass());
    }
}
