package com.trigonic.utils.test.junit;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class ListOfArraysWithCustomLabelMakerTest {
    @Parameters
    public static List<Object[]> getParameters() {
        return Arrays.asList(new Object[] { "a", 1 }, new Object[] { "b", 2 }, new Object[] { "c", 3 });
    }

    @LabelMakerFactory
    public static LabelMaker getLabelMaker() {
        return new LabelMaker() {
            public String getLabel(int index, Object[] parameters) {
                return (String) parameters[0] + parameters[1];
            }

            public String getTestName(int index, Object[] parameters, String methodName) {
                return methodName + "/" + index;
            }
        };
    }

    private String string;
    private int number;

    public ListOfArraysWithCustomLabelMakerTest(String string, int number) {
        this.string = string;
        this.number = number;
    }

    @Test
    public void test() {
        assertTrue(string.matches("[a-c]"));
        assertTrue(number >= 1 && number <= 3);
    }
}
