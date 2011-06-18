package com.trigonic.utils.test.junit;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * Similar to {@link org.junit.runners.Parameterized} but allows for readable labels and easier parameter specification.  A public
 * static method that returns a {@link LabelMaker} can be annotated with {@link LabelMakerFactory} and something other than the
 * {@link DefaultLabelMaker default labels} will be used.
 */
public class Parameterized extends Suite {
    protected final ArrayList<Runner> runners = new ArrayList<Runner>();

    /**
     * This is only called reflexively, do not call directly.
     */
    public Parameterized(Class<?> testClass) throws Throwable {
        super(testClass, Collections.<Runner>emptyList());
        List<?> parametersList = getParametersList(getTestClass());
        assertTrue("No parameters found for " + testClass, parametersList.size() > 0);
        LabelMaker labelMaker = getLabelMaker(getTestClass());
        for (int i = 0; i < parametersList.size(); ++i) {
            runners.add(new TestClassRunnerForParameters(getTestClass().getJavaClass(), parametersList, i, labelMaker));
        }
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

    protected List<?> getParametersList(TestClass testClass) throws Throwable {
        Iterable<?> iterable = (Iterable<?>) getParametersMethod(testClass).invokeExplosively(null);
        if (iterable instanceof Collection<?>) {
            return new ArrayList<Object>((Collection<?>) iterable);
        }

        List<Object> arrayList = new ArrayList<Object>();
        for (Object each : iterable) {
            arrayList.add(each);
        }
        return arrayList;
    }

    protected FrameworkMethod getParametersMethod(TestClass testClass) throws Exception {
        return getPublicStaticAnnotatedMethod(testClass, Parameters.class, true);
    }

    protected LabelMaker getLabelMaker(TestClass testClass) throws Throwable {
        LabelMaker result;
        FrameworkMethod method = getLabelMakerFactoryMethod(testClass);
        if (method != null) {
            result = (LabelMaker) method.invokeExplosively(null);
        } else {
            result = new DefaultLabelMaker();
        }
        return result;
    }

    protected FrameworkMethod getLabelMakerFactoryMethod(TestClass testClass) throws Exception {
        return getPublicStaticAnnotatedMethod(testClass, LabelMakerFactory.class, false);
    }

    protected FrameworkMethod getPublicStaticAnnotatedMethod(TestClass testClass, Class<? extends Annotation> annotationType,
        boolean required) throws Exception {
        FrameworkMethod result = null;

        List<FrameworkMethod> methods = testClass.getAnnotatedMethods(annotationType);
        for (FrameworkMethod method : methods) {
            int modifiers = method.getMethod().getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
                result = method;
                break;
            }
        }

        if ((result == null) && required) {
            throw new IllegalArgumentException("No public static parameters @" + annotationType.getSimpleName() + " method on class " +
                testClass.getName());
        }

        return result;
    }
}
