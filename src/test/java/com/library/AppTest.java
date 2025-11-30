package com.library;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {

    // كونستركتور بدون باراميتر
    public AppTest() {
        super("AppTest");
    }

    // الكونستركتور القديم


    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    public void testApp() {
        assertTrue(true);
    }
}
