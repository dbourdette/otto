package com.github.dbourdette.otto.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.github.dbourdette.otto.web.util.FlashScope;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class FlashScopeFilter implements Filter {

    private static String[] STATIC_FILES = {".ico", ".txt", ".css"};

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;

        if (isStaticFile(request.getRequestURI())) {
            chain.doFilter(req, resp);

            return;
        }

		Object value = request.getSession().getAttribute(FlashScope.FLASH_SCOPE_IN_SESSION);

		if (value != null) {
			@SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;

			for (Map.Entry<String, Object> entry : map.entrySet()) {
				request.setAttribute(entry.getKey(), entry.getValue());
			}
		}

		request.getSession().removeAttribute(FlashScope.FLASH_SCOPE_IN_SESSION);

		chain.doFilter(req, resp);

    }

    public boolean isStaticFile(String uri) {
        for (String extension : STATIC_FILES) {
            if (uri.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }


    public void init(FilterConfig config) throws ServletException {

    }

    public void destroy() {
    }

}
