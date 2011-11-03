package gr.ifouk.performance;

import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

import com.lmax.disruptor.WaitStrategy;

public class SingleThreadTest  extends TestCase {

	private static final long LOOPS = 500 * 1000 * 1000;
	
	public void testDisruptor() throws Exception {
		AtomicLong value = new AtomicLong(0l);
		long start = System.nanoTime();
		for(long i = 0; i < LOOPS; i++) {
			value.incrementAndGet();
		}
		long end = System.nanoTime();
		System.out.println( (end - start) + " nanoseconds for single thread");
	}
}
