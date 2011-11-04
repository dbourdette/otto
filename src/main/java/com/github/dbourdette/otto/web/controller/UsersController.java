package com.github.dbourdette.otto.web.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.dbourdette.otto.service.user.User;
import com.github.dbourdette.otto.service.user.Users;
import com.github.dbourdette.otto.web.editor.ObjectIdEditor;
import com.github.dbourdette.otto.web.util.FlashScope;

/**
 * @author damien bourdette
 */
@Controller
public class UsersController {
    @Inject
    private Users users;

    @Inject
    private FlashScope flashScope;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(ObjectId.class, new ObjectIdEditor());
    }

    @RequestMapping("/users")
    public String users(Model model) {
        model.addAttribute("navItem", "users");
        model.addAttribute("users", users.findUsers());

        return "admin/users";
    }

    @RequestMapping("/users/form")
    public String form(@RequestParam(required = false) String id, Model model) {
        if (id == null) {
            model.addAttribute("form", new User());
        } else {
            model.addAttribute("form", users.findUser(id));
        }

        return "admin/user_form";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String post(Model model, @ModelAttribute("form") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/user_form";
        }

        users.save(user);

        flashScope.message("user " + user.getUsername() + " has been modified");

        return "redirect:/users";
    }

    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public String delete(@RequestParam String id, Model model) {
        User user = users.findUser(id);

        users.delete(user);

        flashScope.message("user " + user.getUsername() + " has been deleted");

        return "redirect:/users";
    }
}
