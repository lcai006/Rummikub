package project;

import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("Rummikub Test Suite")
@SelectClasses({ModelTest.class, GameTest.class})
public class TestSuite {
}