package com.trigonic.utils.test.junit;

public class DefaultLabelMaker implements LabelMaker {
    private String delimiter;
    
    public DefaultLabelMaker() {
        this("");
    }
    
    public DefaultLabelMaker(String delimiter) {
        this.delimiter = delimiter;
    }
    
    public String getLabel(int index, Object[] parameters) {
        return String.format("[%s]", index);
    }

    public String getTestName(int index, Object[] parameters, String methodName) {
        String label = getLabel(index, parameters);
        return methodName + delimiter + label;
    }
}