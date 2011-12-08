package com.github.dbourdette.otto.report.filler;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class NoAccentOperation implements Operation {
    private Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    @Override
    public void handle(ChainContext context) {
        String string = Normalizer.normalize(context.getColumn(), Normalizer.Form.NFD);

        context.setColumn(pattern.matcher(string).replaceAll(""));
    }
}
