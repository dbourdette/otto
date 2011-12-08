package com.github.dbourdette.otto.report.filler;

import org.junit.Assert;
import org.junit.Test;

import com.mongodb.BasicDBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class NoPunctuationFillerTest {
    private NoPunctuationOperation filler = new NoPunctuationOperation();

    @Test
    public void removeAccents() {
        ChainContext context = new ChainContext(new BasicDBObject());
        context.setColumn("h'o, mé. dd");

        filler.handle(context);

        Assert.assertEquals("ho mé dd", context.getColumn());
    }
}
