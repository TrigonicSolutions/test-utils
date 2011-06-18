package com.trigonic.utils.test.junit;

public class DefaultLabelMaker implements LabelMaker {
    public String getLabel(int index, Object[] parameters) {
        return String.format("[%s]", index);
    }

    public String getTestName(int index, Object[] parameters, String methodName) {
        return String.format("%s[%s]", methodName, index);
    }
}