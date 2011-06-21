package com.trigonic.utils.test.junit;

import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class TestClassRunnerForParameters extends BlockJUnit4ClassRunner {
    private final int index;
    private final Object[] parameters;
    private final LabelMaker labelMaker;

    TestClassRunnerForParameters(Class<?> type, List<Object> parameter, int index, LabelMaker labelMaker)
        throws InitializationError {
        super(type);
        this.index = index;
        this.parameters = parameter.toArray();
        this.labelMaker = labelMaker;

        if (labelMaker == null) {
            throw new NullPointerException("LabelMaker required");
        }
    }

    @Override
    public Object createTest() throws Exception {
        return getTestClass().getOnlyConstructor().newInstance(parameters);
    }

    @Override
    protected String getName() {
        return labelMaker.getLabel(index, parameters);
    }

    @Override
    protected String testName(final FrameworkMethod method) {
        return labelMaker.getTestName(index, parameters, method.getName());
    }

    @Override
    protected void validateConstructor(List<Throwable> errors) {
        validateOnlyOneConstructor(errors);
    }

    @Override
    protected Statement classBlock(RunNotifier notifier) {
        return childrenInvoker(notifier);
    }
}