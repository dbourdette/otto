package org.otto.web.exception;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.otto.web.util.FlashScope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author damien bourdette
 */
@Component
public class ExceptionResolver extends DefaultHandlerExceptionResolver {

	@Inject
	private FlashScope flashScope;

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		if (ex instanceof SourceNotFound) {
			flashScope.message("Source not found");

			return new ModelAndView(new RedirectView("/index"));
		}

		return super.resolveException(request, response, handler, ex);
	}

}
