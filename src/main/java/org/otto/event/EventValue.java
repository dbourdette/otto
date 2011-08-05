package org.otto.event;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
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
}
