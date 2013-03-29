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

import javax.inject.Inject;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.github.dbourdette.otto.web.exception.ScheduleNotFound;
import com.github.dbourdette.otto.web.exception.SourceAlreadyExists;
import com.github.dbourdette.otto.web.exception.SourceNotFound;
import com.github.dbourdette.otto.web.util.FlashScope;

/**
 * @author damien bourdette
 */
@ControllerAdvice
public class ExceptionHandlers {
    @Inject
    private FlashScope flashScope;

    @ExceptionHandler(SourceNotFound.class)
    public String sourceNotFound() {
        flashScope.message("Source not found");

        return "redirect:/index";
    }

    @ExceptionHandler(ScheduleNotFound.class)
    public String scheduleNotFound() {
        flashScope.message("Schedule not found");

        return "redirect:/index";
    }

    @ExceptionHandler(SourceAlreadyExists.class)
    public String sourceAlreadyExists() {
        flashScope.message("Source already exists");

        return "redirect:/index";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView generic(Exception e) {
        ModelAndView mav = new ModelAndView("error");

        mav.addObject("stacktrace", ExceptionUtils.getFullStackTrace(e));

        return mav;
    }
}
