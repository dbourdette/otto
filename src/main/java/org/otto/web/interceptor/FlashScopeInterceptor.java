package org.otto.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.otto.web.util.FlashScope;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author damien bourdette
 */
public class FlashScopeInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Object value = request.getSession().getAttribute(FlashScope.FLASH_SCOPE_IN_SESSION);
		
		if (value != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) value;
			
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				request.setAttribute(entry.getKey(), entry.getValue());
			}
		}
		
		request.getSession().removeAttribute(FlashScope.FLASH_SCOPE_IN_SESSION);
		
		return super.preHandle(request, response, handler);
	}

	

}
