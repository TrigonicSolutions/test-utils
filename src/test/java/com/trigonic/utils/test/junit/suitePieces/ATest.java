package com.trigonic.utils.test.junit.suitePieces;

import org.junit.Test;

public class ATest extends TestPieceBase {
    @Test
    public void test() throws Exception {
        registerExecuted(getClass().getMethod("test"));
    }
}
