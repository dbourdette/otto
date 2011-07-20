package org.otto.event;

/**
 * @author damien bourdette
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
}
