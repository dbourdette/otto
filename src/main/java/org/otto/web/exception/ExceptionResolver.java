package org.otto.web.exception;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.otto.web.util.FlashScope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author damien bourdette
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

	@Inject
	private FlashScope flashScope;

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex) {
		if (ex instanceof TypeNotFound) {
			flashScope.message("Type not found");

			return new ModelAndView(new RedirectView("/index"));
		}

		return null;
	}

}
