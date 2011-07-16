package org.otto.web.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import org.otto.web.form.TypeForm;
import org.otto.web.util.MongoDbHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Inject
    private DB mongoDb;

    @RequestMapping({"/", "/index", "/types"})
    public String index(Model model) {
        List<String> types = new ArrayList<String>();

        for (String name : mongoDb.getCollectionNames()) {
            if (name.startsWith(MongoDbHelper.EVENTS_PREFIX)) {
            	types.add(name.substring(MongoDbHelper.EVENTS_PREFIX.length()));
            }
        }

        model.addAttribute("types", types);

        return "index";
    }
}
