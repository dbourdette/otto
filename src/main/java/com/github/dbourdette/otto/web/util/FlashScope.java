/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dbourdette.otto.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.github.dbourdette.otto.service.logs.Logs;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author damien bourdette
 * @version \$Revision$
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
