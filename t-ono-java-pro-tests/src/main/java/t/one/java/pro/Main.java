package t.one.java.pro;

import t.one.java.pro.annotations.*;
import t.one.java.pro.exceptions.BadTestClassError;
import t.one.java.pro.exceptions.TestAssertionError;
import t.one.java.pro.model.TestResult;
import t.one.java.pro.runner.TestRunner;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Running custom test runner...");

        // 1. Запуск тестов
        Map<TestResult, List<t.one.java.pro.model.Test>> results = TestRunner.runTests(ValidTestClass.class);

        // 2. Вывод результатов
        for (Map.Entry<TestResult, List<t.one.java.pro.model.Test>> entry : results.entrySet()) {
            System.out.println(entry.getKey() + ":");
            for (t.one.java.pro.model.Test t : entry.getValue()) {
                System.out.println("  " + t.name() + " -> " + t.result() + (t.exception() != null ? " (" + t.exception().getMessage() + ")" : ""));
            }
        }
        // 3. Проверка вызова BeforeSuite/AfterSuite
        System.out.println("\nBeforeSuite call count: " + ValidTestClass.beforeSuiteCallCount);
        System.out.println("AfterSuite call count: " + ValidTestClass.afterSuiteCallCount);

        // 4. Проверка вызова BeforeEach/AfterEach
        System.out.println("BeforeEach/AfterEach counts are checked inside the tests themselves.");

        // 5. Проверка ошибок
        System.out.println("\nTesting error cases...");
        try {
            TestRunner.runTests(BadTestClassWithStaticTest.class);
        } catch (BadTestClassError e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        try {
            TestRunner.runTests(BadTestClassWithInstanceBeforeSuite.class);
        } catch (BadTestClassError e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        System.out.println("\nAll tests completed.");
    }

    // Класс с тестами
    public static class ValidTestClass {

        public static int beforeSuiteCallCount = 0;
        public static int afterSuiteCallCount = 0;
        public int beforeEachCallCount = 0;
        public int afterEachCallCount = 0;

        @BeforeSuite
        public static void beforeSuite() {
            beforeSuiteCallCount++;
        }

        @AfterSuite
        public static void afterSuite() {
            afterSuiteCallCount++;
        }

        @BeforeEach
        public void beforeEach() {
            beforeEachCallCount++;
        }

        @AfterEach
        public void afterEach() {
            afterEachCallCount++;
        }

        @Test(priority = 10)
        @Order(1)
        public void testHighPriority() {
            System.out.println("Running testHighPriority");
            assert beforeEachCallCount == 1;
            assert afterEachCallCount == 0;
        }

        @Test(priority = 5)
        @Order(3)
        public void testMediumPriority() {
            System.out.println("Running testMediumPriority");
            assert beforeEachCallCount == 2;
            assert afterEachCallCount == 1;
        }

        @Test(priority = 5)
        @Order(2)
        public void testMediumPriority2() {
            System.out.println("Running testMediumPriority2");
            assert beforeEachCallCount == 2;
            assert afterEachCallCount == 1;
            throw new TestAssertionError("Assertion failed");
        }

        @Test
        public void testDefaultPriority() {
            System.out.println("Running testDefaultPriority");
            throw new RuntimeException("Runtime error");
        }

        @Test
        @Disabled
        public void testDisabled() {
            System.out.println("This should not run");
        }

        @Test
        public void testSuccess() {
            System.out.println("Running testSuccess");
        }
    }

    // Классы для проверки ошибок
    public static class BadTestClassWithStaticTest {
        @Test
        public static void staticTest() { }
    }

    public static class BadTestClassWithInstanceBeforeSuite {
        @BeforeSuite
        public void instanceBeforeSuite() { }
    }
}