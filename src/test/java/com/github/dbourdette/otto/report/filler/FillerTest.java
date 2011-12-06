package com.github.dbourdette.otto.report.filler;

import org.joda.time.DateMidnight;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.otto.report.Report;
import com.github.dbourdette.otto.report.ReportPeriod;
import com.mongodb.BasicDBObject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class FillerTest {
    private Report report;

    @Before
    public void init() {
        report = new Report();
        report.createRows(ReportPeriod.TODAY);
    }

    @Test
    public void nofiller() {
        chain().write(event());

        Assert.assertEquals(1, report.getValue(ColumnValue.DEFAULT_COLUMN, 0).intValue());
    }

    @Test
    public void sum() {
        SumFiller sum = new SumFiller();
        sum.setColumn("value");

        chain(sum).write(event());

        Assert.assertEquals(10, report.getValue(ColumnValue.DEFAULT_COLUMN, 0).intValue());
    }

    @Test
    public void split() {
        SplitFiller split = new SplitFiller();
        split.setColumns("text", "value");

        chain(split).write(event());

        Assert.assertEquals(1, report.getValue("this is a dummy content - 10", 0).intValue());
    }

    @Test
    public void tokenize() {
        TokenizeFiller tokenize = new TokenizeFiller();
        tokenize.setColumn("text");
        tokenize.setStopWords("this", "is", "a");

        chain(tokenize).write(event());

        Assert.assertEquals(1, report.getValue("dummy", 0).intValue());
        Assert.assertEquals(1, report.getValue("content", 0).intValue());
    }

    @Test
    public void multipleFillers() {
        TokenizeFiller tokenize = new TokenizeFiller();
        tokenize.setColumn("text");
        tokenize.setStopWords("this", "is", "a");

        SumFiller sum = new SumFiller();
        sum.setColumn("value");

        chain(tokenize, sum).write(event());

        Assert.assertEquals(10, report.getValue("dummy", 0).intValue());
        Assert.assertEquals(10, report.getValue("content", 0).intValue());
    }

    private BasicDBObject event() {
        BasicDBObject event = new BasicDBObject();

        event.put("value", 10);
        event.put("text", "this is a dummy content");
        event.put("date", new DateMidnight().toDate());

        return event;
    }

    private FillerChain chain(Filler... fillers) {
        FillerChain chain = FillerChain.forGraph(report);

        for (Filler filler : fillers) {
            chain.add(filler);
        }

        return chain;
    }
}
