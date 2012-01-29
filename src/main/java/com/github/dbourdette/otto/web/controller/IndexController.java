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
import java.io.Writer;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.Sources;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class IndexController {

    @Inject
    private Sources sources;

    @RequestMapping({"/", "/index", "/sources"})
    public String index(Model model) {
        model.addAttribute("groups", sources.getSourceGroups());
        
        return "index";
    }

    @RequestMapping("/state")
    public void state(HttpServletResponse response) throws IOException {
        Writer writer = response.getWriter();
        
        for (DBSource source : sources.getSources()) {
            writer.write(source.toString());
        }
    }
}
