package com.github.dbourdette.otto.report.filler;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class LowerCaseFillerTest {
    private LowerCaseOperation filler = new LowerCaseOperation();

    @Test
    public void lower() {
        List<String> columns = filler.handle("LoWeR");

        Assert.assertEquals(1, columns.size());
        Assert.assertEquals("lower", columns.get(0));
    }
}
