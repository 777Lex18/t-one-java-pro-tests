//
//
//
//import org.junit.jupiter.api.Test; // ← Для JUnit тестов
//import t.one.java.pro.annotations.Test; // ← Ваша аннотация
//import t.one.java.pro.annotations.BeforeSuite;
//import t.one.java.pro.annotations.AfterSuite;
//import t.one.java.pro.annotations.BeforeEach;
//import t.one.java.pro.annotations.AfterEach;
//import t.one.java.pro.annotations.Order;
//import t.one.java.pro.annotations.Disabled;
//import t.one.java.pro.exceptions.TestAssertionError;
//import t.one.java.pro.exceptions.BadTestClassError;
//import t.one.java.pro.model.TestResult;
//import t.one.java.pro.model.Test; // ← Ваш DTO
//
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//public class TestRunnerValidationTest {
//    @Test
//    void testAllAnnotationsAndScenarios() {
//        // Класс с тестами, который содержит все аннотации и сценарии
//        class ValidTestClass {
//
//            public static int beforeSuiteCallCount = 0;
//            public static int afterSuiteCallCount = 0;
//            public int beforeEachCallCount = 0;
//            public int afterEachCallCount = 0;
//
//            @BeforeSuite
//            public static void beforeSuite() {
//                beforeSuiteCallCount++;
//            }
//
//            @AfterSuite
//            public static void afterSuite() {
//                afterSuiteCallCount++;
//            }
//
//            @BeforeEach
//            public void beforeEach() {
//                beforeEachCallCount++;
//            }
//
//            @AfterEach
//            public void afterEach() {
//                afterEachCallCount++;
//            }
//
//            @Test(priority = 10)
//            @Order(1)
//            public void testHighPriority() {
//                assertEquals(1, beforeEachCallCount);
//                assertEquals(0, afterEachCallCount);
//            }
//
//            @Test(priority = 5)
//            @Order(3)
//            public void testMediumPriority() {
//                assertEquals(2, beforeEachCallCount);
//                assertEquals(1, afterEachCallCount);
//            }
//
//            @Test(priority = 5)
//            @Order(2)
//            public void testMediumPriority2() {
//                assertEquals(2, beforeEachCallCount);
//                assertEquals(1, afterEachCallCount);
//                throw new TestAssertionError("Assertion failed");
//            }
//
//            @Test
//            public void testDefaultPriority() {
//                throw new RuntimeException("Runtime error");
//            }
//
//            @Test
//            @Disabled
//            public void testDisabled() {
//                fail("This should not run");
//            }
//
//            @Test
//            public void testSuccess() {
//                // success
//            }
//        }
//
//        Map<TestResult, List<t.one.java.pro.model.Test>> results = TestRunner.runTests(ValidTestClass.class);
//
//        List<t.one.java.pro.model.Test> successTests = results.get(TestResult.Success);
//        List<t.one.java.pro.model.Test> failedTests = results.get(TestResult.Failed);
//        List<t.one.java.pro.model.Test> errorTests = results.get(TestResult.Error);
//        List<t.one.java.pro.model.Test> skippedTests = results.get(TestResult.Skipped);
//
//        assertEquals(2, successTests.size(), "Should have 2 successful tests");
//        assertEquals(1, failedTests.size(), "Should have 1 failed test");
//        assertEquals(1, errorTests.size(), "Should have 1 error test");
//        assertEquals(1, skippedTests.size(), "Should have 1 skipped test");
//
//        assertTrue(successTests.stream().anyMatch(t -> t.name().equals("testHighPriority")));
//        assertTrue(successTests.stream().anyMatch(t -> t.name().equals("testSuccess")));
//
//        assertTrue(failedTests.stream().anyMatch(t -> t.name().equals("testMediumPriority2")));
//        assertTrue(errorTests.stream().anyMatch(t -> t.name().equals("testDefaultPriority")));
//        assertTrue(skippedTests.stream().anyMatch(t -> t.name().equals("testDisabled")));
//
//        assertEquals(1, ValidTestClass.beforeSuiteCallCount, "BeforeSuite should be called once");
//        assertEquals(1, ValidTestClass.afterSuiteCallCount, "AfterSuite should be called once");
//    }
//
//    @Test
//    void testStaticTestAnnotationThrowsError() {
//        class BadTestClass {
//            @Test
//            public static void staticTest() { }
//        }
//
//        assertThrows(BadTestClassError.class, () -> TestRunner.runTests(BadTestClass.class));
//    }
//
//    @Test
//    void testStaticBeforeEachAnnotationThrowsError() {
//        class BadTestClass {
//            @BeforeEach
//            public static void staticBeforeEach() { }
//        }
//
//        assertThrows(BadTestClassError.class, () -> TestRunner.runTests(BadTestClass.class));
//    }
//
//    @Test
//    void testInstanceBeforeSuiteAnnotationThrowsError() {
//        class BadTestClass {
//            @BeforeSuite
//            public void instanceBeforeSuite() { }
//        }
//
//        assertThrows(BadTestClassError.class, () -> TestRunner.runTests(BadTestClass.class));
//    }
//
//    @Test
//    void testNoDefaultConstructorThrowsError() {
//        class BadTestClass {
//            public BadTestClass(String s) { }
//        }
//
//        assertThrows(BadTestClassError.class, () -> TestRunner.runTests(BadTestClass.class));
//    }
//}
