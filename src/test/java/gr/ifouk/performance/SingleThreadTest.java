package gr.ifouk.performance;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

import com.lmax.disruptor.WaitStrategy;

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
