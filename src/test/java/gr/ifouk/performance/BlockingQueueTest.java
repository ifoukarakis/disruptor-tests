package gr.ifouk.performance;

import gr.ifouk.performance.blockingqueue.BlockingQueueTestUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import junit.framework.TestCase;

public class BlockingQueueTest extends TestCase {

	private static final long LOOPS = 500 * 1000 * 1000;
	
	public void testArrayBlockingQueueSize256KOneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(256 * 1024);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("ArrayBlockingQueue (256k): " + value + " nanoseconds");
	}
	
	public void testArrayBlockingQueueSize128KOneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(128 * 1024);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("ArrayBlockingQueue (128k): " + value + " nanoseconds");
	}
	
	public void testArrayBlockingQueueSize64KOneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(64 * 1024);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("ArrayBlockingQueue (64k): " + value + " nanoseconds");
	}
	
	public void testArrayBlockingQueueSize32KOneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(32 * 1024);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("ArrayBlockingQueue (32k): " + value + " nanoseconds");
	}	
	
	public void testArrayBlockingQueueSize256OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(256);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("ArrayBlockingQueue (256): " + value + " nanoseconds");
	}
	
	public void testArrayBlockingQueueSize128OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(128);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("ArrayBlockingQueue (128): " + value + " nanoseconds");
	}
	
	public void testArrayBlockingQueueSize64OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(64);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("ArrayBlockingQueue (64): " + value + " nanoseconds");
	}
	
	public void testArrayBlockingQueueSize32OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(32);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("ArrayBlockingQueue (32): " + value + " nanoseconds");
	}
	//Linked blocking queue

	public void testLinkedBlockingQueueSize256OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>(256 * 1024);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("LinkedBlockingQueue (256k): " + value + " nanoseconds");
	}
	
	public void testLinkedBlockingQueueSize128OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>(128 * 1024);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("LinkedBlockingQueue (128k): " + value + " nanoseconds");
	}

	public void testLinkedBlockingQueueSize64OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>(64 * 1024);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("LinkedBlockingQueue (64k): " + value + " nanoseconds");
	}

	public void testLinkedBlockingQueueSize32OneProducerOneConsumer() throws Exception {
		BlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>(32 * 1024);
		long value = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
		System.out.println("LinkedBlockingQueue (32k): " + value + " nanoseconds");
	}
	
	

	
	
}
