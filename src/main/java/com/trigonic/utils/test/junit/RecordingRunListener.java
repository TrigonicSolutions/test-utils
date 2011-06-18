package com.trigonic.utils.test.junit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class RecordingRunListener extends RunListener {
    @Override
    public void testRunStarted(Description description) throws Exception {
        System.out.println(description);
    }

    @Override
    public void testStarted(Description description) throws Exception {
        System.out.println(description);
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        System.out.println(failure);
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        System.out.println(failure);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        System.out.println(description);
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        System.out.println(description);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        System.out.println(result);
    }
}
