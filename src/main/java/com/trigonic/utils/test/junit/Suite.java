package com.trigonic.utils.test.junit;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

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
            results.addAll(getSubTypesOf(baseClassAnnotation.value(), packageName));
        }

        results = Sets.filter(results, new IsConcreteClass());
        if (results.isEmpty()) {
            throw new InitializationError(String.format(
                    "class '%s' either has no @SuiteClass or @SuiteBaseClass, or no concrete classes matched",
                    forClass.getName()));
        }

        return results.toArray(new Class<?>[results.size()]);
    }
    
    protected static List<Class<?>> getSubTypesOf(Class<?> baseClass, String packageName) throws InitializationError {
        List<Class<?>> candidates = new ArrayList<Class<?>>();

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        String resourcePath = ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(packageName));
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resourcePath + "/**/*.class";
        
        try {
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        Class<?> candidate = Class.forName(metadataReader.getClassMetadata().getClassName());
                        if (baseClass.isAssignableFrom(candidate)) {
                            candidates.add(candidate);
                        }
                    } catch(Exception e){
                        // skip this resource
                    }
                }
            }
        } catch (IOException e) {
            throw new InitializationError(e);
        }
        
        return candidates;
    }
    
    protected static class IsConcreteClass implements Predicate<Class<?>> {
        public boolean apply(Class<?> input) {
            return !Modifier.isAbstract(input.getModifiers());
        }
    }
}
