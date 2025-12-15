package com.api.tests;

import org.testng.TestNG;

public class TestRunner {
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[] { PetTests.class });
        testng.run();
    }
}
