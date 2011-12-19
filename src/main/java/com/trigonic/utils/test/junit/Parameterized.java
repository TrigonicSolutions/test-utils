package com.trigonic.utils.test.junit;

import static org.junit.Assert.assertFalse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

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
        List<List<Object>> parameters = getParameters(getTestClass());
        assertFalse("No parameters found for " + testClass, parameters.size() == 0);
        LabelMaker labelMaker = getLabelMaker(getTestClass());
        for (int index = 0; index < parameters.size(); ++index) {
            runners.add(new TestClassRunnerForParameters(getTestClass().getJavaClass(), parameters.get(index), index, labelMaker));
        }
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

    protected List<List<Object>> getParameters(TestClass testClass) throws Throwable {
        List<List<Object>> result = null;
        for (FrameworkMethod method : getParametersMethods(testClass)) {
            List<List<Object>> parameters = getParameters(method);
            if (result == null) {
                result = parameters;
            } else {
                result = new ListCartesianProduct<Object>(parameters, result);
            }
        }
        return result;
    }
    
    protected List<List<Object>> getParameters(FrameworkMethod method) throws Throwable {
        Object parameters = method.invokeExplosively(null);
        List<List<Object>> parametersList;
        if (parameters instanceof Iterable<?>) {
            parametersList = toParameterList((Iterable<?>) parameters);
        } else if (parameters.getClass().isArray()) {
            parametersList = toParameterList(Arrays.asList((Object[]) parameters));
        } else {
            parametersList = toParameterList(Arrays.asList(parameters));
        }
        return parametersList;
    }
    
    protected List<List<Object>> toParameterList(Iterable<?> parameters) {
        List<List<Object>> result = new ArrayList<List<Object>>();
        for (Object value : parameters) {
            List<Object> row;
            if (value instanceof Iterable<?>) {
                row = Lists.newArrayList((Iterable<?>) value);
            } else if (value.getClass().isArray()) {
                row = Arrays.asList((Object[]) value);
            } else {
                row = Arrays.asList(value);
            }
            result.add(row);
        }
        return result;
    }

    protected Collection<FrameworkMethod> getParametersMethods(TestClass testClass) throws Exception {
        return Collections2.filter(testClass.getAnnotatedMethods(Parameters.class), new PublicStaticFrameworkMethod());
    }
    
    public static class PublicStaticFrameworkMethod implements Predicate<FrameworkMethod> {
        public boolean apply(FrameworkMethod method) {
            int modifiers = method.getMethod().getModifiers();
            return (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers));
        }
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
