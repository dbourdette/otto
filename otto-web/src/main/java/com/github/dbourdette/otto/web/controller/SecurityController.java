package com.github.dbourdette.otto.web.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.dbourdette.otto.security.Security;
import com.github.dbourdette.otto.security.SecurityConfig;
import com.github.dbourdette.otto.web.util.FlashScope;

/**
 * @author damien bourdette
 */
@Controller
public class SecurityController {
    @Inject
    private Security security;

    @Inject
    private FlashScope flashScope;

    @RequestMapping("/security")
    public String security(Model model) {
        model.addAttribute("navItem", "security");
        model.addAttribute("form", security.fromDb());

        return "admin/security_form";
    }

    @RequestMapping(value = "/security", method = RequestMethod.POST)
    public String post(Model model, @ModelAttribute("form") @Valid SecurityConfig config, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/security_form";
        }

        security.save(config);

        flashScope.message("Security config has been updated");

        return "redirect:/security";
    }
}
