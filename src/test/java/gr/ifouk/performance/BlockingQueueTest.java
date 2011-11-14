package gr.ifouk.performance;

import gr.ifouk.performance.blockingqueue.BlockingQueueConsumer;
import gr.ifouk.performance.blockingqueue.BlockingQueueProducer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import junit.framework.TestCase;

public class BlockingQueueTest extends TestCase {

	private static final long LOOPS = 500 * 1000 * 1000;
	
	public void testArrayBlockingQueueSize256OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(256 * 1024);
		long value = testBlockingQueueOneProducerOneConsumer(queue);
		System.out.println("ArrayBlockingQueue (256k): " + value + " nanoseconds");
	}
	
	public void testArrayBlockingQueueSize128OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(128 * 1024);
		long value = testBlockingQueueOneProducerOneConsumer(queue);
		System.out.println("ArrayBlockingQueue (128k): " + value + " nanoseconds");
	}
	
	public void testArrayBlockingQueueSize64OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(64 * 1024);
		long value = testBlockingQueueOneProducerOneConsumer(queue);
		System.out.println("ArrayBlockingQueue (64k): " + value + " nanoseconds");
	}
	
	public void testArrayBlockingQueueSize32OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(32 * 1024);
		long value = testBlockingQueueOneProducerOneConsumer(queue);
		System.out.println("ArrayBlockingQueue (32k): " + value + " nanoseconds");
	}
	
	//Linked blocking queue

	public void testLinkedBlockingQueueSize256OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>(256 * 1024);
		long value = testBlockingQueueOneProducerOneConsumer(queue);
		System.out.println("LinkedBlockingQueue (256k): " + value + " nanoseconds");
	}
	
	public void testLinkedBlockingQueueSize128OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>(128 * 1024);
		long value = testBlockingQueueOneProducerOneConsumer(queue);
		System.out.println("LinkedBlockingQueue (128k): " + value + " nanoseconds");
	}

	public void testLinkedBlockingQueueSize64OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>(64 * 1024);
		long value = testBlockingQueueOneProducerOneConsumer(queue);
		System.out.println("LinkedBlockingQueue (64k): " + value + " nanoseconds");
	}

	public void testLinkedBlockingQueueSize32OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>(32 * 1024);
		long value = testBlockingQueueOneProducerOneConsumer(queue);
		System.out.println("LinkedBlockingQueue (32k): " + value + " nanoseconds");
	}
	
	
	private final long testBlockingQueueOneProducerOneConsumer(BlockingQueue<Boolean> queue) throws Exception {
		final long loops = LOOPS;
		
		//Create start and end latch
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(1);
		
		//Create producer and consumer
		BlockingQueueProducer producer = new BlockingQueueProducer(queue, loops, startLatch);
		BlockingQueueConsumer consumer = new BlockingQueueConsumer(queue, loops, startLatch, endLatch);
		
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
		return (end - start);
	}
	
	
}
