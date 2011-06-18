package com.trigonic.utils.test.junit;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
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
        Iterable<?> parameters = getParameters(getTestClass());
        Iterator<?> parameterIter = parameters.iterator();
        assertTrue("No parameters found for " + testClass, parameterIter.hasNext());
        LabelMaker labelMaker = getLabelMaker(getTestClass());
        int index = 0;
        while (parameterIter.hasNext()) {
            runners.add(new TestClassRunnerForParameters(getTestClass().getJavaClass(), parameterIter.next(), index++, labelMaker));
        }
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

    protected Iterable<?> getParameters(TestClass testClass) throws Throwable {
        Object parameters = getParametersMethod(testClass).invokeExplosively(null);
        Iterable<?> result;
        if (parameters instanceof Iterable<?>) {
            result = (Iterable<?>) parameters;
        } else if (parameters.getClass().isArray()) {
            result = Arrays.asList((Object[]) parameters);
        } else {
            result = Arrays.asList(parameters);
        }
        return result;
    }

    protected FrameworkMethod getParametersMethod(TestClass testClass) throws Exception {
        return getPublicStaticAnnotatedMethod(testClass, Parameters.class, true);
    }

    protected LabelMaker getLabelMaker(TestClass testClass) throws Throwable {
        LabelMaker result;
        LabelMakerClass labelMakerClass = testClass.getJavaClass().getAnnotation(LabelMakerClass.class);
        if (labelMakerClass != null) {
            result = labelMakerClass.value().newInstance();
        } else {
            FrameworkMethod method = getLabelMakerFactoryMethod(testClass);
            if (method != null) {
                result = (LabelMaker) method.invokeExplosively(null);
            } else {
                result = new DefaultLabelMaker();
            }
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
