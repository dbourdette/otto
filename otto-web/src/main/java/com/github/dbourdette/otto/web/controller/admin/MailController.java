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

package com.github.dbourdette.otto.web.controller.admin;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.dbourdette.otto.service.mail.Mail;
import com.github.dbourdette.otto.service.mail.MailConfiguration;
import com.github.dbourdette.otto.service.mail.Mailer;
import com.github.dbourdette.otto.web.util.FlashScope;

/**
 * Edition of mail configuration
 *
 * @author damien bourdette
 */
@Controller
public class MailController {
    @Inject
    private Mailer mailer;

    @Inject
    private FlashScope flashScope;

    @RequestMapping("/mail")
    public String users(Model model) {
        model.addAttribute("navItem", "mail");
        model.addAttribute("configuration", mailer.findConfiguration());

        return "admin/mail";
    }

    @RequestMapping("/mail/edit")
    public String edit(Model model) {
        model.addAttribute("navItem", "mail");
        model.addAttribute("form", mailer.findConfiguration());

        return "admin/mail_form";
    }

    @RequestMapping(value = "/mail/edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute("form") MailConfiguration form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/mail_form";
        }

        mailer.saveConfiguration(form);

        flashScope.message("mail config has been modified");

        return "redirect:/mail";
    }

    @RequestMapping("/mail/send")
    public String send(Model model) {
        model.addAttribute("navItem", "mail");
        model.addAttribute("form", new Mail());

        return "admin/mail_send_form";
    }

    @RequestMapping(value = "/mail/send", method = RequestMethod.POST)
    public String send(@ModelAttribute("form") @Valid Mail form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("navItem", "mail");

            return "admin/mail_send_form";
        }

        try {
            mailer.send(form);

            flashScope.message("Mail has been sent to " + form.getTo());
        } catch (Exception e) {
            model.addAttribute("stacktrace", ExceptionUtils.getFullStackTrace(e));

            return "error";
        }

        return "redirect:/mail/send";
    }

    @RequestMapping("/mail/sent")
    public String sent(Model model) {
        model.addAttribute("navItem", "mail");

        return "admin/mail_sent";
    }
}
