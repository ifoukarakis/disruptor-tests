package gr.ifouk.tests.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueComparison {

	public static final long LOOPS = 500 * 1000 * 1000;
	
	private static final void testOneProducerOneConsumer() {
		BlockingQueue<Boolean> queue = null;
		//Compare for different queue sizes, starting from 64 up to 256k
		//One producer, one consumer
		int size = 64;
		long result;
		for(int i = 6; i < 20; i++) {
			try {
				queue = new ArrayBlockingQueue<Boolean>(size);
				result = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
				System.out.println("ArrayBlockingQueue (" + size + "), 1P 1C:\t" + result + " nanoseconds");
			} catch (Exception e) {
				System.out.println("ArrayBlockingQueue for size " + size + ", 1P 1C failed: " + e.getMessage());
			}
			try {
				queue = new LinkedBlockingQueue<Boolean>(size);
				result = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, LOOPS);
				System.out.println("LinkedBlockingQueue (" + size + "), 1P 1C:\t" + result + " nanoseconds");
			} catch (Exception e) {
				System.out.println("LinkedBlockingQueue for size " + size + ", 1P 1C failed: " + e.getMessage());
			}

			size *= 2;
		}
	}
	
	private static final void testThreeProducerOneConsumer() {
		BlockingQueue<Boolean> queue = null;
		//Compare for different queue sizes, starting from 64 up to 256k
		//One producer, one consumer
		int size = 64;
		long result;
		for(int i = 6; i < 18; i++) {
			try {
				queue = new ArrayBlockingQueue<Boolean>(size);
				result = BlockingQueueTestUtils.testBlockingQueueManyProducerOneConsumer(queue, 3, LOOPS);
				System.out.println("ArrayBlockingQueue (" + size + "), 3P 1C:\t" + result + " nanoseconds");
			} catch (Exception e) {
				System.out.println("ArrayBlockingQueue for size " + size + ", 3P 1C failed:\t" + e.getMessage());
			}
			try {
				queue = new LinkedBlockingQueue<Boolean>(size);
				result = BlockingQueueTestUtils.testBlockingQueueManyProducerOneConsumer(queue, 3, LOOPS);
				System.out.println("LinkedBlockingQueue (" + size + "), 3P 1C:\t" + result + " nanoseconds");
			} catch (Exception e) {
				System.out.println("LinkedBlockingQueue for size " + size + ", 3P 1C failed: " + e.getMessage());
			}

			size *= 2;
		}
	}
	
	public static void main(String[] args) {
		testOneProducerOneConsumer();
		testThreeProducerOneConsumer();
	}

}
