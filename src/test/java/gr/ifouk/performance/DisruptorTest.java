package gr.ifouk.performance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import gr.ifouk.performance.disruptor.DisruptorConsumer;
import gr.ifouk.performance.disruptor.DisruptorProducer;
import gr.ifouk.performance.disruptor.ValueEvent;
import junit.framework.TestCase;

import com.lmax.disruptor.ClaimStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;


public class DisruptorTest extends TestCase {

	private static final long LOOPS = 500 * 1000 * 1000;
	
	public void testDisruptor256Yield() throws Exception {
		testDisruptorWithOptions(256, WaitStrategy.Option.YIELDING);
	}

	public void testDisruptor1024Yield() throws Exception {
		testDisruptorWithOptions(1024, WaitStrategy.Option.YIELDING);
	}
	
	public void testDisruptor16x1024Yield() throws Exception {
		testDisruptorWithOptions(16 * 1024, WaitStrategy.Option.YIELDING);
	}

	public void testDisruptor256Blocking() throws Exception {
		testDisruptorWithOptions(256, WaitStrategy.Option.BLOCKING);
	}

	public void testDisruptor1024Blocking() throws Exception {
		testDisruptorWithOptions(1024, WaitStrategy.Option.BLOCKING);
	}
	
	public void testDisruptor16x1024Blocking() throws Exception {
		testDisruptorWithOptions(16 * 1024, WaitStrategy.Option.BLOCKING);
	}


	public void testDisruptor256BusySpin() throws Exception {
		testDisruptorWithOptions(256, WaitStrategy.Option.BUSY_SPIN);
	}

	public void testDisruptor1024BusySpin() throws Exception {
		testDisruptorWithOptions(1024, WaitStrategy.Option.BUSY_SPIN);
	}
	
	public void testDisruptor16x1024BusySpin() throws Exception {
		testDisruptorWithOptions(16 * 1024, WaitStrategy.Option.BUSY_SPIN);
	}
	

	public void testDisruptor256Sleep() throws Exception {
		testDisruptorWithOptions(256, WaitStrategy.Option.SLEEPING);
	}

	public void testDisruptor1024Sleep() throws Exception {
		testDisruptorWithOptions(1024, WaitStrategy.Option.SLEEPING);
	}
	
	public void testDisruptor16x1024Sleep() throws Exception {
		testDisruptorWithOptions(16 * 1024, WaitStrategy.Option.SLEEPING);
	}


	
	private final void testDisruptorWithOptions(int ringSize, WaitStrategy.Option waitStrategy) throws InterruptedException {
		final RingBuffer<ValueEvent> ringBuffer =
			    new RingBuffer<ValueEvent>(ValueEvent.EVENT_FACTORY, ringSize, 
			                               ClaimStrategy.Option.SINGLE_THREADED,
			                               waitStrategy);
	    final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
	    CountDownLatch startGate = new CountDownLatch(1);
	    DisruptorProducer producer = new DisruptorProducer(LOOPS, startGate, ringBuffer);
	    DisruptorConsumer consumer = new DisruptorConsumer(startGate, ringBuffer, sequenceBarrier);
		Thread p = new Thread(producer);
		p.start();
		Thread c = new Thread(consumer);
		c.start();
		System.gc();
		long start = System.nanoTime();
		startGate.countDown();
		p.join();
		c.join();
		long end = System.nanoTime();
		System.out.println((end - start) + " nanoseconds for Disruptor (" + ringSize + ", "  + waitStrategy.name() +")");	}
}
