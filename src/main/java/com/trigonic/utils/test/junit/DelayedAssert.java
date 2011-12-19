package com.trigonic.utils.test.junit;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;

public class DelayedAssert {
    private static final long DEFAULT_POLL_INTERVAL = 250L;
    
    /* true */
    
    public static void assertTrueAfter(long timeout, Callable<Boolean> callable) throws Exception {
        assertTrueAfter(timeout, DEFAULT_POLL_INTERVAL, callable);
    }
    
    public static void assertTrueAfter(long timeout, long pollInterval, Callable<Boolean> callable) throws Exception {
        long stopAfter = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < stopAfter && !callable.call()) {
            Thread.sleep(pollInterval);
        }
        assertTrue(callable.call());
    }
    
    /* false */
    
    public static void assertFalseAfter(long timeout, Callable<Boolean> callable) throws Exception {
        assertFalseAfter(timeout, DEFAULT_POLL_INTERVAL, callable);
    }
    
    public static void assertFalseAfter(long timeout, long pollInterval, Callable<Boolean> callable) throws Exception {
        assertTrueAfter(timeout, pollInterval, new Not(callable));
    }
    
    /* null */
    
    public static <T> void assertNullAfter(long timeout, Callable<T> callable) throws Exception {
        assertNullAfter(timeout, DEFAULT_POLL_INTERVAL, callable);
    }
    
    public static <T> void assertNullAfter(long timeout, long pollInterval, Callable<T> callable) throws Exception {
        assertEqualsAfter(null, timeout, pollInterval, callable);
    }
    
    /* not null */
    
    public static <T> void assertNotNullAfter(long timeout, Callable<T> callable) throws Exception {
        assertNotNullAfter(timeout, DEFAULT_POLL_INTERVAL, callable);
    }
    
    public static <T> void assertNotNullAfter(long timeout, long pollInterval, Callable<T> callable) throws Exception {
        assertNotEqualsAfter(null, timeout, pollInterval, callable);
    }
    
    /* equals */
    
    public static <T> void assertEqualsAfter(T expected, long timeout, Callable<T> callable) throws Exception {
        assertEqualsAfter(expected, timeout, DEFAULT_POLL_INTERVAL, callable);
    }
        
    public static <T> void assertEqualsAfter(T expected, long timeout, long pollInterval, Callable<T> callable) throws Exception {
        assertTrueAfter(timeout, pollInterval, new CallableEquals<T>(expected, callable));
    }
    
    /* not equals */
    
    public static <T> void assertNotEqualsAfter(T expected, long timeout, Callable<T> callable) throws Exception {
        assertNotEqualsAfter(expected, timeout, DEFAULT_POLL_INTERVAL, callable);
    }
        
    public static <T> void assertNotEqualsAfter(T expected, long timeout, long pollInterval, Callable<T> callable) throws Exception {
        assertFalseAfter(timeout, pollInterval, new CallableEquals<T>(expected, callable));
    }

    /* same */
    
    public static <T> void assertSameAfter(T expected, long timeout, Callable<T> callable) throws Exception {
        assertSameAfter(expected, timeout, DEFAULT_POLL_INTERVAL, callable);
    }
        
    public static <T> void assertSameAfter(T expected, long timeout, long pollInterval, Callable<T> callable) throws Exception {
        assertTrueAfter(timeout, pollInterval, new CallableSame<T>(expected, callable));
    }
    
    /* different */
    
    public static <T> void assertDifferentAfter(T expected, long timeout, Callable<T> callable) throws Exception {
        assertDifferentAfter(expected, timeout, DEFAULT_POLL_INTERVAL, callable);
    }
        
    public static <T> void assertDifferentAfter(T expected, long timeout, long pollInterval, Callable<T> callable) throws Exception {
        assertFalseAfter(timeout, pollInterval, new CallableSame<T>(expected, callable));
    }

    /* useful callables */
    
    protected static class Not implements Callable<Boolean> {
        private Callable<Boolean> callable;
        
        public Not(Callable<Boolean> callable) {
            this.callable = callable;
        }

        public Boolean call() throws Exception {
            return !callable.call();
        }
    }
    
    protected static class CallableSame<T> implements Callable<Boolean> {
        private T expected;
        private Callable<T> callable;
        
        public CallableSame(T expected, Callable<T> callable) {
            this.expected = expected;
            this.callable = callable;
        }

        public Boolean call() throws Exception {
            T actual = callable.call();
            return expected == actual;
        }
    }
    
    protected static class CallableEquals<T> implements Callable<Boolean> {
        private T expected;
        private Callable<T> callable;
        
        public CallableEquals(T expected, Callable<T> callable) {
            this.expected = expected;
            this.callable = callable;
        }

        public Boolean call() throws Exception {
            T actual = callable.call();
            return expected == actual || (expected != null && actual != null && expected.equals(actual));
        }
    }
}
