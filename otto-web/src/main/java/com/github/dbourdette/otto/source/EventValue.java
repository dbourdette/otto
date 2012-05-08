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

package com.github.dbourdette.otto.source;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class EventValue {
	private Object value;
	
	private EventValueType type;

	public EventValue(EventValueType type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public EventValueType getType() {
		return type;
	}

    @Override
    public String toString() {
        return "EventValue{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
