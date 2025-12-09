package t.one.java.pro.runner;

import t.one.java.pro.model.TestResult;
import t.one.java.pro.annotations.BeforeEach;
import t.one.java.pro.annotations.AfterEach;
import t.one.java.pro.annotations.BeforeSuite;
import t.one.java.pro.annotations.AfterSuite;
import t.one.java.pro.annotations.Disabled;
import t.one.java.pro.annotations.Order;
import t.one.java.pro.exceptions.BadTestClassError;
import t.one.java.pro.exceptions.TestAssertionError;
import t.one.java.pro.model.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TestRunner {
    public static Map<TestResult, List<Test>> runTests(Class<?> clazz) {
        Map<TestResult, List<Test>> result = new EnumMap<>(TestResult.class);
        result.put(TestResult.Success, new ArrayList<>());
        result.put(TestResult.Failed, new ArrayList<>());
        result.put(TestResult.Error, new ArrayList<>());
        result.put(TestResult.Skipped, new ArrayList<>());

        Method[] methods = clazz.getDeclaredMethods();
        List<Method> testMethods = new ArrayList<>();
        List<Method> beforeEachMethods = new ArrayList<>();
        List<Method> afterEachMethods = new ArrayList<>();
        List<Method> beforeSuiteMethods = new ArrayList<>();
        List<Method> afterSuiteMethods = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(t.one.java.pro.annotations.Test.class)) {
                testMethods.add(method);
            }
            if (method.isAnnotationPresent(BeforeEach.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    throw new BadTestClassError("BeforeEach method must not be static: " + method.getName());
                }
                beforeEachMethods.add(method);
            }
            if (method.isAnnotationPresent(AfterEach.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    throw new BadTestClassError("AfterEach method must not be static: " + method.getName());
                }
                afterEachMethods.add(method);
            }
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new BadTestClassError("BeforeSuite method must be static: " + method.getName());
                }
                beforeSuiteMethods.add(method);
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new BadTestClassError("AfterSuite method must be static: " + method.getName());
                }
                afterSuiteMethods.add(method);
            }
        }


        Object instance;
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        } catch (Exception e) {
            throw new BadTestClassError("Cannot instantiate test class: " + e.getMessage());
        }

        for (Method beforeSuite : beforeSuiteMethods) {
            try {
                beforeSuite.invoke(null);
            } catch (Exception e) {
                throw new BadTestClassError("BeforeSuite method failed: " + e.getMessage());
            }
        }

        List<Method> filteredTestMethods = testMethods.stream()
                .filter(m -> !m.isAnnotationPresent(Disabled.class))
                .sorted((a, b) -> {
                    t.one.java.pro.annotations.Test aTest = a.getAnnotation(t.one.java.pro.annotations.Test.class);
                    t.one.java.pro.annotations.Test bTest = b.getAnnotation(t.one.java.pro.annotations.Test.class);
                    int aPriority = aTest.priority();
                    int bPriority = bTest.priority();
                    if (aPriority != bPriority) {
                        return Integer.compare(bPriority, aPriority);
                    }

                    int aOrder = a.isAnnotationPresent(Order.class) ? a.getAnnotation(Order.class).value() : 5;
                    int bOrder = b.isAnnotationPresent(Order.class) ? b.getAnnotation(Order.class).value() : 5;
                    if (aOrder != bOrder) {
                        return Integer.compare(aOrder, bOrder);
                    }

                    return a.getName().compareTo(b.getName());
                })
                .toList();

        List<Method> disabledTestMethods = testMethods.stream()
                .filter(m -> m.isAnnotationPresent(Disabled.class))
                .toList();


        for (Method testMethod : filteredTestMethods) {
            TestResult testResult = null;
            Throwable exception = null;

            try {
                for (Method beforeEach : beforeEachMethods) {
                    beforeEach.invoke(instance);
                }

                testMethod.invoke(instance);

                testResult = TestResult.Success;

            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof TestAssertionError) {
                    testResult = TestResult.Failed;
                } else {
                    testResult = TestResult.Error;
                }
                exception = cause;
            } finally {
                try {
                    for (Method afterEach : afterEachMethods) {
                        afterEach.invoke(instance);
                    }
                } catch (Exception e) {
                    if (testResult == TestResult.Success) {
                        testResult = TestResult.Error;
                        exception = e.getCause();
                    }
                }
            }

            String testName = testMethod.getAnnotation(t.one.java.pro.annotations.Test.class).name();
            if (testName.isEmpty()) {
                testName = testMethod.getName();
            }

            result.get(testResult).add(new Test(testName, testResult, exception));
        }

        for (Method testMethod : disabledTestMethods) {
            String testName = testMethod.getAnnotation(t.one.java.pro.annotations.Test.class).name();
            if (testName.isEmpty()) {
                testName = testMethod.getName();
            }
            result.get(TestResult.Skipped).add(new Test(testName, TestResult.Skipped, null));
        }

        for (Method afterSuite : afterSuiteMethods) {
            try {
                afterSuite.invoke(null);
            } catch (Exception e) {
                throw new BadTestClassError("AfterSuite method failed: " + e.getMessage());
            }
        }

        return result;
    }
}