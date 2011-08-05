package org.otto.web.controller;

import org.otto.logs.Logs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
@Controller
public class LogsController {
	@Inject
	private Logs logs;
	
	@RequestMapping({"/logs"})
    public String graph(Model model) {
    	model.addAttribute("logs", logs.top());

        return "logs";
    }
}
