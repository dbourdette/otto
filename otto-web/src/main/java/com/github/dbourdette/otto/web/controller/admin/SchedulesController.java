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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.source.schedule.MailSchedule;
import com.github.dbourdette.otto.source.schedule.SourceScheduleExecutor;
import com.github.dbourdette.otto.source.schedule.SourceSchedules;
import com.github.dbourdette.otto.web.editor.ObjectIdEditor;
import com.github.dbourdette.otto.web.util.FlashScope;

@Controller
@RequestMapping("/admin/schedules")
public class SchedulesController {
    @Inject
    private FlashScope flashScope;

    @Inject
    private SourceSchedules sourceSchedules;

    @Inject
    private SourceScheduleExecutor scheduleExecutor;

    @ModelAttribute("navItem")
    public String navItem() {
        return "schedules";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(ObjectId.class, new ObjectIdEditor());
    }

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("schedules", sourceSchedules.findForAllSources());

        return "admin/schedules/index";
    }

    @RequestMapping("/form")
    public String form(@RequestParam(required = false) String id, Model model) {
        if (StringUtils.isEmpty(id)) {
            model.addAttribute("form", new MailSchedule());
        } else {
            model.addAttribute("form", sourceSchedules.findById(id));
        }

        return "admin/schedules/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String form(@ModelAttribute("form") @Valid MailSchedule form, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/schedules/form";
        }

        form.setSourceName(Source.ALL_SOURCES);

        sourceSchedules.save(form);

        flashScope.message("Global mail schedule " + form.getTitle() + " has been updated");

        return "redirect:/admin/schedules";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(@RequestParam(required = true) String id) {
        MailSchedule mailSchedule = sourceSchedules.findById(id);

        sourceSchedules.delete(mailSchedule);

        flashScope.message("Global mail schedule " + mailSchedule.getTitle() + " has been deleted");

        return "redirect:/admin/schedules";
    }

    @RequestMapping("/{id}/send")
    public String send(@PathVariable String id) throws Exception {
        MailSchedule schedule = sourceSchedules.findById(id);

        scheduleExecutor.executeNow(schedule);

        flashScope.message("Global schedule " + schedule.getTitle() + " has been executed (and mail sent)");

        return "redirect:/admin/schedules";
    }
}
