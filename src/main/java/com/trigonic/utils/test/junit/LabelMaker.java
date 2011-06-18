package com.trigonic.utils.test.junit;

public interface LabelMaker {
    String getLabel(int index, Object[] parameters);

    String getTestName(int index, Object[] parameters, String methodName);
}