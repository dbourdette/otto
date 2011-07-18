package org.otto.web.exception;

/**
 * @author damien bourdette
 */
public class TypeNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TypeNotFound() {
		super();
	}

	public TypeNotFound(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TypeNotFound(String arg0) {
		super(arg0);
	}

	public TypeNotFound(Throwable arg0) {
		super(arg0);
	}

}
