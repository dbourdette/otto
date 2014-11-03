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

package com.github.dbourdette.otto.data.filler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class TokenizeOperation implements Operation {
    private String separator = " ";

    private String[] stopWords;

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setStopWords(String... stopWords) {
        this.stopWords = stopWords;
    }

    @Override
    public List<String> handle(String column) {
        String[] tokens = StringUtils.split(column, separator);

        List<String> result = new ArrayList<String>();

        if (StringUtils.isEmpty(column)) {
            result.add("");

            return result;
        }

        for (String token : tokens) {
            if (!ArrayUtils.contains(stopWords, token)) {
                result.add(token);
            }
        }

        return result;
    }
}
