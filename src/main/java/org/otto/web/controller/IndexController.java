package org.otto.web.controller;

import javax.inject.Inject;

import org.otto.event.Sources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
