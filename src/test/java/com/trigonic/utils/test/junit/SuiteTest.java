package com.trigonic.utils.test.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.trigonic.utils.test.junit.suitePieces.ATest;
import com.trigonic.utils.test.junit.suitePieces.BTest;
import com.trigonic.utils.test.junit.suitePieces.TestPieceBase;

@RunWith(Suite.class)
@SuiteBaseClass(value= TestPieceBase.class, packageName = "com.trigonic.utils.test.junit")
public class SuiteTest {
    @BeforeClass
    public static void setupSetup() {
        TestPieceBase.resetTestMethods();
    }
    
    @SuppressWarnings("unchecked")
    @AfterClass
    public static void verifySuite() throws Exception {
       Set<Method> testMethodsExecuted = TestPieceBase.getTestMethodsExecuted();
       assertEquals(2, testMethodsExecuted.size());
       for (Class<?> testClass : Arrays.asList(ATest.class, BTest.class)) {
           assertTrue(testMethodsExecuted.contains(testClass.getMethod("test")));
       }
    }
}
