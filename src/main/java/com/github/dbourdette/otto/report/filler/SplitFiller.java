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

package com.github.dbourdette.otto.report.filler;

import org.apache.commons.lang.StringUtils;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SplitFiller implements Filler {
    private String[] columns;

    public void setColumns(String... columns) {
        this.columns = columns;
    }

    public void setColumns(String columns) {
        this.columns = StringUtils.split(columns, ",");
    }

    @Override
    public void handle(FillerContext context) {
        if (columns == null) {
            return;
        }

        StringBuilder result = new StringBuilder();

        for (String column : columns) {
            Object value = context.get(column);

            if (value != null) {
                if (result.length() != 0) {
                    result.append(" - ");
                }

                result.append(value.toString());
            }
        }

        String newColumn = result.toString();

        if (StringUtils.isEmpty(newColumn)) {
            return;
        }

        context.addContext(newColumn);
    }
}
