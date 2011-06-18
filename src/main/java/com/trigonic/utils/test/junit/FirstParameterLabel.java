package com.trigonic.utils.test.junit;

public class FirstParameterLabel extends DefaultLabelMaker {
    public FirstParameterLabel() {
        super(":");
    }

    public FirstParameterLabel(String delimiter) {
        super(delimiter);
    }
    
    @Override
    public String getLabel(int index, Object[] parameters) {
        return parameters[0] == null ? "null" : parameters[0].toString();
    }
}
