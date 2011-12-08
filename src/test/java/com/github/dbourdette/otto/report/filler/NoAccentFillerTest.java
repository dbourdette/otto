package com.github.dbourdette.otto.report.filler;

import org.junit.Assert;
import org.junit.Test;

import com.mongodb.BasicDBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class NoAccentFillerTest {
    private NoAccentOperation filler = new NoAccentOperation();

    @Test
    public void removeAccents() {
        ChainContext context = new ChainContext(new BasicDBObject());
        context.setColumn("eéèàçùâ");

        filler.handle(context);

        Assert.assertEquals("eeeacua", context.getColumn());
    }

}
