package com.github.dbourdette.otto.report.filler;

import java.util.regex.Pattern;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class NoPunctuationOperation implements Operation {
    private Pattern pattern = Pattern.compile("\\p{Punct}+");

    @Override
    public void handle(ChainContext context) {
        String column = pattern.matcher(context.getColumn()).replaceAll("");

        context.setColumn(column);
    }
}
