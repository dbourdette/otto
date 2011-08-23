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

package org.otto.web.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SizeInBytesTest {
    @Test
    public void fromValue() {
        assertEquals(100, SizeInBytes.fromValue("100").getValue());
        assertEquals(6541, SizeInBytes.fromValue("6541").getValue());
        assertEquals(100, SizeInBytes.fromValue("100B").getValue());
        assertEquals(1024, SizeInBytes.fromValue("1K").getValue());
        assertEquals(1024 * 1024, SizeInBytes.fromValue("1M").getValue());
    }

    @Test
    public void getFormattedValue() throws Exception {
        assertEquals("100B", new SizeInBytes(100).getFormattedValue());
        assertEquals("500B", new SizeInBytes(500).getFormattedValue());
        assertEquals("1K", new SizeInBytes(1024).getFormattedValue());
        assertEquals("1K 1B", new SizeInBytes(1025).getFormattedValue());
        assertEquals("1M", new SizeInBytes(1024 * 1024).getFormattedValue());
        assertEquals("1G", new SizeInBytes(1024 * 1024 * 1024).getFormattedValue());
        assertEquals("1T", new SizeInBytes((long) 1024 * 1024 * 1024 * 1024).getFormattedValue());
        assertEquals("1G 10M 1B", new SizeInBytes(1024 * 1024 * 1024 + 10 * 1024 * 1024 + 1).getFormattedValue());
    }
}
