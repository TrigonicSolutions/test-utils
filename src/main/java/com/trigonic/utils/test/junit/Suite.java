package com.trigonic.utils.test.junit;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

/**
 * Similar to {@link org.junit.runners.Suite} but in addition to the {@link org.junit.runners.Suite.SuiteClasses} annotation, the
 * {@link SuiteBaseClass} annotation can also be supplied.
 */
public class Suite extends org.junit.runners.Suite {
    public Suite(Class<?> forClass, RunnerBuilder builder) throws InitializationError {
        super(builder, forClass, getAnnotatedClasses(forClass));
    }

    public Suite(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
        super(builder, classes);
    }

    protected static Class<?>[] getAnnotatedClasses(Class<?> forClass) throws InitializationError {
        Set<Class<?>> results = new LinkedHashSet<Class<?>>();

        SuiteClasses classesAnnotation = forClass.getAnnotation(SuiteClasses.class);
        if (classesAnnotation != null) {
            results.addAll(Arrays.asList(classesAnnotation.value()));
        }

        SuiteBaseClass baseClassAnnotation = forClass.getAnnotation(SuiteBaseClass.class);
        if (baseClassAnnotation != null) {
            String packageName = baseClassAnnotation.packageName();
            if (packageName.isEmpty()) {
                packageName = baseClassAnnotation.packageName();
            }
            Reflections reflections = new Reflections(packageName, new SubTypesScanner());
            results.addAll(reflections.getSubTypesOf(baseClassAnnotation.value()));
        }

        results = Sets.filter(results, new IsConcreteClass());
        if (results.isEmpty()) {
            throw new InitializationError(String.format(
                    "class '%s' either has no @SuiteClass or @SuiteBaseClass, or no concrete classes matched",
                    forClass.getName()));
        }

        return results.toArray(new Class<?>[results.size()]);
    }

    protected static class IsConcreteClass implements Predicate<Class<?>> {
        public boolean apply(Class<?> input) {
            return !Modifier.isAbstract(input.getModifiers());
        }
    }
}
