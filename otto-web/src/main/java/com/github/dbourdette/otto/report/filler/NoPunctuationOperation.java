package com.github.dbourdette.otto.report.filler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class NoPunctuationOperation implements Operation {
    private Pattern pattern = Pattern.compile("\\p{Punct}+");

    @Override
    public List<String> handle(String column) {
        List<String> result = new ArrayList<String>();

        result.add(pattern.matcher(column).replaceAll(""));

        return result;
    }
}
