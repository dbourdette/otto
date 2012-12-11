package com.github.dbourdette.otto.web.controller.admin;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.source.reports.ReportFormat;
import com.github.dbourdette.otto.source.reports.ReportFormats;
import com.github.dbourdette.otto.web.editor.ObjectIdEditor;
import com.github.dbourdette.otto.web.util.FlashScope;

@Controller
@RequestMapping("/admin/formats")
public class FormatsController {
    @Inject
    private FlashScope flashScope;

    @Inject
    private ReportFormats reportFormats;

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
        model.addAttribute("formats", reportFormats.findForAllSources());

        return "admin/formats/index";
    }

    @RequestMapping("/form")
    public String form(@RequestParam(required = false) String id, Model model) {
        if (StringUtils.isEmpty(id)) {
            model.addAttribute("form", new ReportFormat());
        } else {
            model.addAttribute("form", reportFormats.findById(id));
        }

        return "admin/formats/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String form(@ModelAttribute("form") @Valid ReportFormat form, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/formats/form";
        }

        form.setSourceName(Source.ALL_SOURCES);

        reportFormats.save(form);

        flashScope.message("Reports format " + form.getName() + " for all sources has been updated");

        return "redirect:/admin/formats";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(@RequestParam(required = true) String id) {
        ReportFormat reportFormat = reportFormats.findById(id);

        reportFormats.delete(id);

        flashScope.message("Reports format " + reportFormat.getName() + " has been deleted");

        return "redirect:/admin/formats";
    }
}
