package com.github.dbourdette.otto.report.filler;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class NoAccentFillerTest {
    private NoAccentOperation filler = new NoAccentOperation();

    @Test
    public void removeAccents() {
        List<String> columns = filler.handle("eéèàçùâ");

        Assert.assertEquals(1, columns.size());
        Assert.assertEquals("eeeacua", columns.get(0));
    }

}
