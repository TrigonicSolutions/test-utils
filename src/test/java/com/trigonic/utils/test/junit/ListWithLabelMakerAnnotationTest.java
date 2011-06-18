package com.trigonic.utils.test.junit;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
@LabelMakerClass(FirstParameterLabel.class)
public class ListWithLabelMakerAnnotationTest {
    @Parameters
    public static List<String> getParameters() {
        return Arrays.asList("a", "b", "c");
    }
    
    private String string;
    
    public ListWithLabelMakerAnnotationTest(String string) {
        this.string = string;
    }
    
    @Test
    public void test() {
        assertTrue(string.matches("[a-c]"));
    }
}
