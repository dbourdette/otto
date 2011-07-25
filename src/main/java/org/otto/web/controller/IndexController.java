package org.otto.web.controller;

import javax.inject.Inject;

import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Inject
    private MongoDbHelper mongoDbHelper;

    @RequestMapping({"/", "/index", "/sources"})
    public String index(Model model) {
        model.addAttribute("sources", mongoDbHelper.getSources());
        
        return "index";
    }
}
