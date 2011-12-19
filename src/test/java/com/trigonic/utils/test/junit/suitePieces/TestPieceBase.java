package com.trigonic.utils.test.junit.suitePieces;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public abstract class TestPieceBase {
    private static Set<Method> testMethodsExecuted;
    
    static {
        resetTestMethods();
    }
    
    public static void resetTestMethods() {
        testMethodsExecuted = new HashSet<Method>();
    }
    
    public static Set<Method> getTestMethodsExecuted() {
        return testMethodsExecuted;
    }
    
    protected static void registerExecuted(Method testMethod) {
        testMethodsExecuted.add(testMethod);
    }
}
