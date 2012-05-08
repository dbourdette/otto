package com.github.dbourdette.otto.data.filler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class LowerCaseOperation implements Operation {
    @Override
    public List<String> handle(String column) {
        List<String> result = new ArrayList<String>();

        result.add(column.toLowerCase());

        return result;
    }
}
