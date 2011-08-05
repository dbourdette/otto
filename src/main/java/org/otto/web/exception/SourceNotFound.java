package org.otto.web.exception;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
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
