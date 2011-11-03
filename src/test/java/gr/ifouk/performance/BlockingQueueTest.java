package gr.ifouk.performance;

import gr.ifouk.performance.blockingqueue.BlockingQueueConsumer;
import gr.ifouk.performance.blockingqueue.BlockingQueueProducer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

public class BlockingQueueTest extends TestCase {

	private static final long LOOPS = 500 * 1000 * 1000;
	
	public void testArrayBlockingQueueSize256OneProducerOneConsumer() throws Exception {
		testArrayBlockingQueueOneProducerOneConsumer(256);
	}
	
	public void testArrayBlockingQueueSize128OneProducerOneConsumer() throws Exception {
		testArrayBlockingQueueOneProducerOneConsumer(128);
	}
	
	public void testArrayBlockingQueueSize64OneProducerOneConsumer() throws Exception {
		testArrayBlockingQueueOneProducerOneConsumer(64);
	}
	
	public void testArrayBlockingQueueSize32OneProducerOneConsumer() throws Exception {
		testArrayBlockingQueueOneProducerOneConsumer(32);
	}
	
	//Linked blocking queue

	public void testLinkedBlockingQueueSize256OneProducerOneConsumer() throws Exception {
		testLinkedBlockingQueueOneProducerOneConsumer(256);
	}
	
	public void testLinkedBlockingQueueSize128OneProducerOneConsumer() throws Exception {
		testLinkedBlockingQueueOneProducerOneConsumer(128);
	}

	public void testLinkedBlockingQueueSize64OneProducerOneConsumer() throws Exception {
		testLinkedBlockingQueueOneProducerOneConsumer(64);
	}

	public void testLinkedBlockingQueueSize32OneProducerOneConsumer() throws Exception {
		testLinkedBlockingQueueOneProducerOneConsumer(32);
	}
	
	
	private final void testArrayBlockingQueueOneProducerOneConsumer(int size) throws Exception {
		final long loops = LOOPS;
		CountDownLatch latch = new CountDownLatch(1);
		AtomicLong counter = new AtomicLong();
		ArrayBlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(size*1024);
		BlockingQueueProducer producer = new BlockingQueueProducer(queue, loops, latch);
		BlockingQueueConsumer consumer = new BlockingQueueConsumer(queue, counter, latch);
		Thread p = new Thread(producer);
		p.start();
		Thread c = new Thread(consumer);
		c.start();
		long start = System.nanoTime();
		System.gc();
		latch.countDown();
		p.join();
		c.join();
		long end = System.nanoTime();
		System.out.println( (end -start) + " nanoseconds for ArrayBlockingQueue(" + size + "k)");
		assertEquals("Invalid synchronization policy", loops, counter.get());
	}
	
	private final void testLinkedBlockingQueueOneProducerOneConsumer(int size) throws Exception {
		final long loops = LOOPS;
		CountDownLatch latch = new CountDownLatch(1);
		AtomicLong counter = new AtomicLong();
		LinkedBlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>(size*1024);
		BlockingQueueProducer producer = new BlockingQueueProducer(queue, loops, latch);
		BlockingQueueConsumer consumer = new BlockingQueueConsumer(queue, counter, latch);
		Thread p = new Thread(producer);
		p.start();
		Thread c = new Thread(consumer);
		c.start();
		long start = System.nanoTime();
		System.gc();
		latch.countDown();
		p.join();
		c.join();
		long end = System.nanoTime();
		System.out.println( (end -start) + " nanoseconds for LinkedBlockingQueue(" + size + "k)");
		assertEquals("Invalid synchronization policy", loops, counter.get());	
	}
	
}
