package com.trigonic.utils.test.junit;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class ArrayWithLabelMakerFactoryTest {
    @Parameters
    public static Object[][] getParameters() {
        return new Object[][] {
                { "a", 1 }, {"b", 2}, {"c", 3}
        };
    }
    
    @LabelMakerFactory
    public static LabelMaker getLabelMaker() {
        return new FirstParameterLabel();
    }
    
    private String string;
    private int number;
    
    public ArrayWithLabelMakerFactoryTest(String string, int number) {
        this.string = string;
        this.number = number;
    }
    
    @Test
    public void test() {
        assertTrue(string.matches("[a-c]"));
        assertTrue(number >= 1 && number <= 3);
    }
}
