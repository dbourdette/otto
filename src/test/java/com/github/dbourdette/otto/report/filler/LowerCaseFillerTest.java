package com.github.dbourdette.otto.report.filler;

import org.junit.Assert;
import org.junit.Test;

import com.mongodb.BasicDBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class LowerCaseFillerTest {
    private LowerCaseOperation filler = new LowerCaseOperation();

    @Test
    public void lower() {
        ChainContext context = new ChainContext(new BasicDBObject());
        context.setColumn("LoWeR");

        filler.handle(context);

        Assert.assertEquals("lower", context.getColumn());
    }
}
