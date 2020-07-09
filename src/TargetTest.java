import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TargetTest {
    public static void start(Class targetClass) {
        Class target = targetClass.getClass();
        afterStart(target);
    }

    public static void start(String targetName) {
        try {
            Class target = Class.forName(targetName);
            afterStart(target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void afterStart(Class target) {
        Method[] methods = target.getDeclaredMethods();
        Method metBeforeSuite = null;
        Method metAfterSuite  = null;

        for (Method m: methods) {
            if(m.isAnnotationPresent(BeforeSuite.class)) {
                if(metBeforeSuite == null) {
                    metBeforeSuite = m;
                } else {
                    throw new  RuntimeException();
                }
            }
            if(m.isAnnotationPresent(AfterSuite.class)) {
                if(metAfterSuite == null) {
                    metAfterSuite = m;
                } else {
                    throw new  RuntimeException();
                }
            }
        }
        if(metBeforeSuite != null) {
            try {
                metBeforeSuite.invoke(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= 10; i++) {
            for (Method m: methods) {
                if(m.isAnnotationPresent(Test.class)) {
                    if(m.getAnnotation(Test.class).priority()==i) {
                        try {
                            m.invoke(null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if(metAfterSuite != null) {
            try {
                metAfterSuite.invoke(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    @Test(priority = 4)
    public static void test1() {
        System.out.println("Test 1");
    }

    @Test(priority = 1)
    public static void test2() {
        System.out.println("Test 2");
    }

    @Test(priority = 8)
    public static void test3() {
        System.out.println("Test 3");
    }

    @Test(priority = 1)
    public static void test4() {
        System.out.println("Test 4");
    }

    @Test(priority = 10)
    public static void test5() {
        System.out.println("Test 5");
    }

    @AfterSuite
    public static void after() {
        System.out.println("AfterSuite method");
    }

    @BeforeSuite
    public static void before() {
        System.out.println("BeforeSuite method");
    }
}
