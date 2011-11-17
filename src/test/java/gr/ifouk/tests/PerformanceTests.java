package gr.ifouk.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PerformanceTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(PerformanceTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(BlockingQueueTest.class);
		suite.addTestSuite(DisruptorTest.class);
		suite.addTestSuite(SingleThreadTest.class);
		//$JUnit-END$
		return suite;
	}

}
