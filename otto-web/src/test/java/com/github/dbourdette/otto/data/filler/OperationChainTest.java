package com.github.dbourdette.otto.data.filler;

import org.joda.time.DateMidnight;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.otto.data.SimpleDataTable;
import com.github.dbourdette.otto.data.DataTablePeriod;
import com.mongodb.BasicDBObject;

public class OperationChainTest {
    private SimpleDataTable table;

    @Before
    public void init() {
        table = new SimpleDataTable();
        table.setupRows(DataTablePeriod.TODAY);
    }

    @Test
    public void nofiller() {
        chain().write(event());

        Assert.assertEquals(1, table.getValue(OperationChain.DEFAULT_COLUMN, 0).intValue());
    }

    @Test
    public void valueAttribute() {
        chain().valueAttribute("value").write(event());

        Assert.assertEquals(10, table.getValue(OperationChain.DEFAULT_COLUMN, 0).intValue());
    }

    @Test
    public void labelAttributes() {
        chain().labelAttributes("text,value").write(event());

        Assert.assertEquals(1, table.getValue("this is a Dummy conTent - 10", 0).intValue());
    }

    @Test
    public void tokenize() {
        TokenizeOperation tokenize = new TokenizeOperation();
        tokenize.setStopWords("this", "is", "a");

        chain(tokenize).labelAttributes("text").write(event());

        Assert.assertEquals(1, table.getValue("Dummy", 0).intValue());
        Assert.assertEquals(1, table.getValue("conTent", 0).intValue());
    }

    @Test
    public void multipleFillers() {
        TokenizeOperation tokenize = new TokenizeOperation();
        tokenize.setStopWords("this", "is", "a");

        LowerCaseOperation lowerCase = new LowerCaseOperation();

        chain(tokenize, lowerCase).labelAttributes("text").valueAttribute("value").write(event());

        Assert.assertEquals(10, table.getValue("dummy", 0).intValue());
        Assert.assertEquals(10, table.getValue("content", 0).intValue());
    }

    private BasicDBObject event() {
        BasicDBObject event = new BasicDBObject();

        event.put("value", 10);
        event.put("text", "this is a Dummy conTent");
        event.put("date", new DateMidnight().toDate());

        return event;
    }

    private OperationChain chain(Operation... operations) {
        OperationChain chain = OperationChain.forTable(table);

        for (Operation operation : operations) {
            chain.add(operation);
        }

        return chain;
    }
}
