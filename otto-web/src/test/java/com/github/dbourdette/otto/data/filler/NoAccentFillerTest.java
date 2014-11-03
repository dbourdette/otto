package com.github.dbourdette.otto.data.filler;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class NoAccentFillerTest {
    private NoAccentOperation filler = new NoAccentOperation();

    @Test
    public void removeAccents() {
        List<String> columns = filler.handle("eéèàçùâ");

        Assert.assertEquals(1, columns.size());
        Assert.assertEquals("eeeacua", columns.get(0));
    }

}
