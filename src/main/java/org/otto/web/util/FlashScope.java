package org.otto.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.otto.logs.Logs;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author damien bourdette
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FlashScope {
	
	public static final String FLASH_SCOPE_IN_SESSION = "__FLASH_SCOPE_IN_SESSION";
	
	@Inject
	private HttpServletRequest request;
    
    @Inject
    private Logs logs;
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public void message(String message) {
		put("message", message);
		
		logs.trace(message);
	} 
	
	public void put(String name, Object value) {
		map.put(name, value);
		
		request.getSession().setAttribute(FLASH_SCOPE_IN_SESSION, map);
	}
}
