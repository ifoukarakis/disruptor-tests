package gr.ifouk.performance;

import gr.ifouk.performance.disruptor.DisruptorConsumer;
import gr.ifouk.performance.disruptor.DisruptorProducer;
import gr.ifouk.performance.disruptor.ValueEvent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
		//Create ring buffer
		final RingBuffer<ValueEvent> ringBuffer =
			    new RingBuffer<ValueEvent>(ValueEvent.EVENT_FACTORY, ringSize, 
			                               ClaimStrategy.Option.SINGLE_THREADED,
			                               waitStrategy);
	    final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
	    CountDownLatch startLatch = new CountDownLatch(1);
	    CountDownLatch endLatch = new CountDownLatch(1);

	    //Create producer and consumer
	    DisruptorProducer producer = new DisruptorProducer(LOOPS, startLatch, ringBuffer);
	    DisruptorConsumer consumer = new DisruptorConsumer(startLatch, endLatch, ringBuffer, sequenceBarrier, LOOPS);
	    
	    //Create executor with a thread pool of two threads to run producer and consumer.
		Executor executor = Executors.newFixedThreadPool(2);
		executor.execute(producer);
		executor.execute(consumer);
	   
		//Perform garbage collection before starting the test in order to reduce possibility of 
		//interfering with time measurement.
		System.gc();
		long start = System.nanoTime();
		//Allow producer and consumer to start
		startLatch.countDown();
		
		//Await for consumer to end. Note that the consumer cannot finish unless the producer has finished.
		endLatch.await();
		
		long end = System.nanoTime();
		System.out.println((end - start) + " nanoseconds for Disruptor (" + ringSize + ", "  + waitStrategy.name() +")");	}
}
