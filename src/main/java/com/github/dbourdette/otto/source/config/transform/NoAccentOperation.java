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

package com.github.dbourdette.otto.source.config.transform;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * @author damien bourdette
 */
public class NoAccentOperation implements TransformOperation {
    private Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    @Override
    public String getShortName() {
        return "noaccent";
    }

    @Override
    public Object apply(Object value) {
        if (value instanceof String) {
            String string = Normalizer.normalize((String) value, Normalizer.Form.NFD);

            return pattern.matcher(string).replaceAll("");
        }

        return value;
    }

    @Override
    public String toString() {
        return "NoAccentOperation{" +
                "pattern=" + pattern +
                '}';
    }
}
