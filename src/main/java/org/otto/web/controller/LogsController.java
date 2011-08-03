package org.otto.web.controller;

import javax.inject.Inject;

import org.otto.logs.Logs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author damien bourdette
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
