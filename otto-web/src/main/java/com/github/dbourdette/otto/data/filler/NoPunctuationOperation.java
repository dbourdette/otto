package com.github.dbourdette.otto.data.filler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NoPunctuationOperation implements Operation {
    private Pattern pattern = Pattern.compile("\\p{Punct}+");

    @Override
    public List<String> handle(String column) {
        List<String> result = new ArrayList<String>();

        result.add(pattern.matcher(column).replaceAll(""));

        return result;
    }
}
