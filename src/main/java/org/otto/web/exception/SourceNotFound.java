package org.otto.web.exception;

/**
 * @author damien bourdette
 */
public class SourceNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SourceNotFound() {
		super();
	}

	public SourceNotFound(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SourceNotFound(String arg0) {
		super(arg0);
	}

	public SourceNotFound(Throwable arg0) {
		super(arg0);
	}

}
