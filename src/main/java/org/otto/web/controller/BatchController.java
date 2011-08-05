package org.otto.web.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.validation.Valid;

import org.codehaus.jackson.JsonParseException;
import org.otto.event.DBSource;
import org.otto.event.Event;
import org.otto.event.Sources;
import org.otto.web.form.BatchForm;
import org.otto.web.form.BatchValuesType;
import org.otto.web.util.FlashScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
@Controller
public class BatchController {
	@Inject
	private Sources sources;

	@Inject
	private FlashScope flashScope;

	@RequestMapping("/sources/{name}/events/batch")
	public String form(@PathVariable String name, Model model) {
		model.addAttribute("navItem", "batch");
		model.addAttribute("form", new BatchForm());

		return "sources/batch_form";
	}

	@RequestMapping(value = "/sources/{name}/events/batch", method = RequestMethod.POST)
	public String postEvent(@PathVariable String name, @Valid @ModelAttribute("form") BatchForm form,
			BindingResult bindingResult) throws JsonParseException, IOException {
		if (bindingResult.hasErrors()) {
			return "sources/batch_form";
		}
		
		DBSource source = sources.getSource(name);

		for (int i = 0; i < form.getCount(); i++) {
			Event event = null;

			if (form.getValuesType() == BatchValuesType.JSON) {
				event = Event.fromJson(form.getValues());
			} else {
				event = Event.fromKeyValues(form.getValues());
			}
			
			event.setDateIfNoneDefined(form.getDateType().instanciateDate());

			source.post(event);
		}

		flashScope.message(form.getCount() + " events inserted for source " + name);

		return "redirect:/sources/{name}/events/batch";
	}
}
