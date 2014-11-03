package com.github.dbourdette.otto.data.filler;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class LowerCaseFillerTest {
    private LowerCaseOperation filler = new LowerCaseOperation();

    @Test
    public void lower() {
        List<String> columns = filler.handle("LoWeR");

        Assert.assertEquals(1, columns.size());
        Assert.assertEquals("lower", columns.get(0));
    }
}
