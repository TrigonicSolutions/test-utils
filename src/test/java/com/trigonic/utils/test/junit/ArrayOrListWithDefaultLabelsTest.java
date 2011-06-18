package com.trigonic.utils.test.junit;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class ArrayOrListWithDefaultLabelsTest {
    @SuppressWarnings("unchecked")
    @Parameters
    public static List<?>[] getParameters() {
        return new List[] {
                Arrays.asList("a", 1), Arrays.asList("b", 2), Arrays.asList("c", 3)
        };
    }
    
    private String string;
    private int number;
    
    public ArrayOrListWithDefaultLabelsTest(String string, int number) {
        this.string = string;
        this.number = number;
    }
    
    @Test
    public void test() {
        assertTrue(string.matches("[a-c]"));
        assertTrue(number >= 1 && number <= 3);
    }
}
