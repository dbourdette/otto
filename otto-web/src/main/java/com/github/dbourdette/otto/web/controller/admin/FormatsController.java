package com.github.dbourdette.otto.web.controller.admin;

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

import com.github.dbourdette.otto.source.reports.ReportFormat;
import com.github.dbourdette.otto.source.reports.ReportFormats;
import com.github.dbourdette.otto.web.editor.ObjectIdEditor;
import com.github.dbourdette.otto.web.util.FlashScope;

@Controller
@RequestMapping("/admin/formats")
public class FormatsController {
    @Inject
    private FlashScope flashScope;

    @ModelAttribute("navItem")
    public String navItem() {
        return "formats";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(ObjectId.class, new ObjectIdEditor());
    }

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("formats", ReportFormats.findForAllSources());

        return "admin/formats/index";
    }

    @RequestMapping("/form")
    public String form(@RequestParam(required = false) String id, Model model) {
        if (id == null) {
            model.addAttribute("form", new ReportFormat());
        } else {
            model.addAttribute("form", ReportFormats.findById(id));
        }

        return "admin/formats/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String form(@ModelAttribute("form") @Valid ReportFormat form, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/formats/form";
        }

        form.setSourceName(ReportFormats.ALL_SOURCES);

        ReportFormats.save(form);

        flashScope.message("Reports format " + form.getName() + " for all sources has been updated");

        return "redirect:/admin/formats";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(@RequestParam(required = true) String id) {
        ReportFormat reportFormat = ReportFormats.findById(id);

        ReportFormats.delete(id);

        flashScope.message("Reports format " + reportFormat.getName() + " has been deleted");

        return "redirect:/admin/formats";
    }
}
