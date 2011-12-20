package com.github.dbourdette.otto.report.filler;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class NoPunctuationFillerTest {
    private NoPunctuationOperation filler = new NoPunctuationOperation();

    @Test
    public void removeAccents() {
        List<String> columns = filler.handle("h'o, mé. dd");

        Assert.assertEquals(1, columns.size());
        Assert.assertEquals("ho mé dd", columns.get(0));
    }
}
