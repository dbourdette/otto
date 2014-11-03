package com.github.dbourdette.otto.data.filler;

import org.junit.Test;

public class TokenizeOperationTest {

    private TokenizeOperation operation = new TokenizeOperation();

    @Test
    public void tokenize() {
        org.junit.Assert.assertEquals("test", operation.handle("this is a test").get(3));
    }

    @Test
    public void nullInput() {
        org.junit.Assert.assertEquals(1, operation.handle(null).size());
        org.junit.Assert.assertEquals("", operation.handle(null).get(0));
    }
}
