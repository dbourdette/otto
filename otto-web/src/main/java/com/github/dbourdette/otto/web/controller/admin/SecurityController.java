package com.github.dbourdette.otto.web.controller.admin;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.dbourdette.otto.security.Security;
import com.github.dbourdette.otto.security.SecurityConfig;
import com.github.dbourdette.otto.service.logs.Logs;
import com.github.dbourdette.otto.web.form.TestLoginForm;
import com.github.dbourdette.otto.web.util.FlashScope;

import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

/**
 * @author damien bourdette
 */
@Controller
public class SecurityController {
    @Inject
    private Security security;

    @Inject
    private FlashScope flashScope;

    @Inject
    private Logs logs;

    @RequestMapping("/security")
    public String security(Model model) {
        model.addAttribute("navItem", "security");
        model.addAttribute("form", security.getConfig());

        return "admin/security_form";
    }

    @RequestMapping(value = "/security", method = RequestMethod.POST)
    public String post(Model model, @ModelAttribute("form") @Valid SecurityConfig config, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/security_form";
        }

        try {
            security.save(config);

            flashScope.message("Security config has been updated");
        } catch (Exception e) {
            result.reject("security.failedToConfigurePlugin");

            logs.error("Failed to update security config", e);

            return "admin/security_form";
        }

        return "redirect:/security";
    }

    @RequestMapping("/security/test")
    public String test(Model model) {
        model.addAttribute("navItem", "security");
        model.addAttribute("form", new TestLoginForm());

        return "admin/security_test";
    }

    @RequestMapping(value = "/security/test", method = RequestMethod.POST)
    public String post(Model model, @ModelAttribute("form") TestLoginForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/security_test";
        }

        model.addAttribute("navItem", "security");
        model.addAttribute("tried", true);

        try {
            if (security.authenticate(form.getUsername(), form.getPassword())) {
                model.addAttribute("success", true);
            } else {
                model.addAttribute("success", false);
            }
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("exception", e);
            model.addAttribute("stack", StringUtils.replace(getFullStackTrace(e), "\n", "<br/>\n"));
        }

        return "admin/security_test";
    }
}
