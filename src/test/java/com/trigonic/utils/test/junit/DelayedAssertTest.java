package com.trigonic.utils.test.junit;

import static com.trigonic.utils.test.junit.DelayedAssert.assertDifferentAfter;
import static com.trigonic.utils.test.junit.DelayedAssert.assertEqualsAfter;
import static com.trigonic.utils.test.junit.DelayedAssert.assertNotEqualsAfter;
import static com.trigonic.utils.test.junit.DelayedAssert.assertNotNullAfter;
import static com.trigonic.utils.test.junit.DelayedAssert.assertNullAfter;
import static com.trigonic.utils.test.junit.DelayedAssert.assertSameAfter;
import static com.trigonic.utils.test.junit.DelayedAssert.assertTrueAfter;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.Callable;

import org.junit.Test;

public class DelayedAssertTest {
    Integer value = null;
    
    @Test
    public void immediatelyEquals() throws Exception {
        value = 42;
        assertEqualsAfter(42, 1000L, new Callable<Integer>() {
            public Integer call() throws Exception {
                return value;
            }
        });
    }

    @Test
    public void eventuallyEquals() throws Exception {        
        Thread setter = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500L);
                    value = 42;
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        });
        setter.start();
        assertEqualsAfter(42, 1000L, new Callable<Integer>() {
            public Integer call() throws Exception {
                return value;
            }
        });
    }

    @Test
    public void neverEquals() throws Exception {
        Throwable throwable = null;
        try {
            assertEqualsAfter(42, 1000L, new Callable<Integer>() {
                public Integer call() throws Exception {
                    return value;
                }
            });
        } catch (AssertionError e) {
            throwable = e;
        }
        assertNotNull(throwable);
    }
    
    @Test
    public void immediatelyNotEquals() throws Exception {
        value = 42;
        assertNotEqualsAfter(23, 1000L, new Callable<Integer>() {
            public Integer call() throws Exception {
                return value;
            }
        });
    }
    
    @Test
    public void immediatelyTrue() throws Exception {
        final Boolean flag = true;
        assertTrueAfter(1000L, 250L, new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return flag;
            }
        });
    }
    
    @Test
    public void immediatelyFalse() throws Exception {
        final Boolean flag = true;
        assertTrueAfter(1000L, 250L, new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return flag;
            }
        });
    }
    
    @Test
    public void immediatelySame() throws Exception {
        value = 109238091;
        assertSameAfter(value, 1000L, 250L, new Callable<Object>() {
            public Object call() throws Exception {
                return value;
            }
        });
    }
    
    @Test
    public void immediatelyDifferent() throws Exception {
        value = 22093809;
        assertDifferentAfter(109238091, 1000L, 250L, new Callable<Object>() {
            public Object call() throws Exception {
                return value;
            }
        });
    }
    
    @Test
    public void immediatelyNull() throws Exception {
        assertNullAfter(1000L, 250L, new Callable<Object>() {
            public Object call() throws Exception {
                return value;
            }
        });
    }
    
    @Test
    public void immediatelyNotNull() throws Exception {
        value = 42;
        assertNotNullAfter(1000L, 250L, new Callable<Object>() {
            public Object call() throws Exception {
                return value;
            }
        });
    }
}
