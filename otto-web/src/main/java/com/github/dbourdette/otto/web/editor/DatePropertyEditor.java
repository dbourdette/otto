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

package com.github.dbourdette.otto.web.editor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePropertyEditor extends PropertyEditorSupport {
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    public static String format(Date date) {
        return date == null ? "" : new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public void setAsText(String value) {
        try {
            setValue(new SimpleDateFormat(DATE_FORMAT).parse(value));
        } catch (ParseException e) {
            setValue(null);
        }
    }

    public String getAsText() {
        return format((Date) getValue());
    }
}
