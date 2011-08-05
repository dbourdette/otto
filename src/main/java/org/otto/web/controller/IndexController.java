package org.otto.web.controller;

import org.otto.event.Sources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
@Controller
public class IndexController {

    @Inject
    private Sources sources;

    @RequestMapping({"/", "/index", "/sources"})
    public String index(Model model) {
        model.addAttribute("sources", sources.getNames());
        
        return "index";
    }
}
