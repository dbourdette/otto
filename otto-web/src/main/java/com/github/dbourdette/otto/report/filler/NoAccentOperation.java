package com.github.dbourdette.otto.report.filler;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class NoAccentOperation implements Operation {
    private Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    @Override
    public List<String> handle(String column) {
        String string = Normalizer.normalize(column, Normalizer.Form.NFD);

        List<String> result = new ArrayList<String>();

        result.add(pattern.matcher(string).replaceAll(""));

        return result;
    }
}
