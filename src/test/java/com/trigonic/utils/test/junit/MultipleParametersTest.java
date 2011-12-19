package com.trigonic.utils.test.junit;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class MultipleParametersTest extends MultipleParametersBaseTest {
    @Parameters
    public static Object[] getParameters() {
        return new Object[] { 1, 2, 3 };
    }
    
    @LabelMakerFactory
    public static LabelMaker getLabelMaker() {
        return new DefaultLabelMaker(":") {
            @Override
            public String getLabel(int index, Object[] parameters) {
                StringBuilder builder = new StringBuilder();
                for (Object parameter : parameters) {
                    builder.append(parameter);
                }
                return builder.toString();
            }
        };
    }

    private String string;
    private int number;
    
    public MultipleParametersTest(String string, int number) {
        this.string = string;
        this.number = number;
    }
    
    @Test
    public void test() {
        assertTrue(string.matches("[a-c]"));
        assertTrue(number >= 1 && number <= 3);
    }
}
