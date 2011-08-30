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

package com.github.dbourdette.otto.web.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.validation.Valid;

import org.codehaus.jackson.JsonParseException;
import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.Event;
import com.github.dbourdette.otto.source.Sources;
import com.github.dbourdette.otto.web.form.BatchForm;
import com.github.dbourdette.otto.web.form.BatchValuesType;
import com.github.dbourdette.otto.web.util.FlashScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author damien bourdette
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
