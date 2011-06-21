package com.trigonic.utils.test.junit;

public class MultipleParametersBaseTest {
    @Parameters
    public static Object[] getBaseParameters() {
        return new Object[] { "a", "b", "c" };
    }
}
