package gr.ifouk.tests;

import java.util.Random;

import junit.framework.TestCase;

public class SingleThreadTest  extends TestCase {

	private static final long LOOPS = 500 * 1000 * 1000;
	
	public void testSingleThread() throws Exception {
		Random random = new Random(System.currentTimeMillis());
		long value = 0l;
		long start = System.nanoTime();
		for(long i = 0; i < LOOPS; i++) {
			if(random.nextBoolean())
				value++;
			else
				value--;
		}
		long end = System.nanoTime();
		System.out.println( (end - start) + " nanoseconds for single thread");
	}
}
