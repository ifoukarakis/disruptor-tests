package gr.ifouk.tests.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueComparison {

	public static final long ITERATIONS = 100 * 1000 * 1000;
	public static final long WARMUP_ITERATIONS = 100 * 1000;
	public static final int WARMUP_LOOPS = 10;
	
	private static final void testOneProducerOneConsumer() {
				
		BlockingQueue<Boolean> queue = null;
		//Compare for different queue sizes, starting from 2^6 up to 2^25
		//One producer, one consumer
		int size = 64;
		
		for (int i = 0; i < WARMUP_LOOPS; i++) {
			//Warm up first in order to avoid compilation during actual
			//test execution.
			System.out.println("Warming up # " + i + "...");
			queue = new ArrayBlockingQueue<Boolean>(size);
			try {
				BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(
						queue, WARMUP_ITERATIONS);
			} catch (Exception e) {
				System.out.println("Warm up failed!!!");
			}
		}
		System.out.println("Done!");
		
		long result;
		for(int i = 6; i < 25; i++) {
			try {
				queue = new ArrayBlockingQueue<Boolean>(size);
				result = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, ITERATIONS);
				//System.out.println("ArrayBlockingQueue (" + size + "), 1P 1C:\t" + result + " nanoseconds");
				System.out.println("ArrayBlockingQueue;" + size + ";1P 1C;" + result + "");
			} catch (Exception e) {
				System.out.println("ArrayBlockingQueue for size " + size + ", 1P 1C failed: " + e.getMessage());
			}
			try {
				queue = new LinkedBlockingQueue<Boolean>(size);
				result = BlockingQueueTestUtils.testBlockingQueueOneProducerOneConsumer(queue, ITERATIONS);
				//System.out.println("LinkedBlockingQueue (" + size + "), 1P 1C:\t" + result + " nanoseconds");
				System.out.println("LinkedBlockingQueue;" + size + ";1P 1C;" + result + "");
			} catch (Exception e) {
				System.out.println("LinkedBlockingQueue for size " + size + ", 1P 1C failed: " + e.getMessage());
			}

			size *= 2;
		}
	}
	
	private static final void testThreeProducerOneConsumer() {
		BlockingQueue<Boolean> queue = null;
		//Compare for different queue sizes, starting from 2^6 up to 2^25
		//Three producers, one consumer
		int size = 64;
		
		//Warm up first in order to avoid compilation during actual
		//test execution.
		for (int i = 0; i < WARMUP_LOOPS; i++) {
			//Warm up first in order to avoid compilation during actual
			//test execution.
			System.out.println("Warming up # " + i + "...");
			queue = new ArrayBlockingQueue<Boolean>(size);
			try {
				BlockingQueueTestUtils.testBlockingQueueManyProducerOneConsumer(
						queue, 3, WARMUP_ITERATIONS);
			} catch (Exception e) {
				System.out.println("Warm up failed!!!");
			}
		}
		System.out.println("Done!");
		
		long result;
		for(int i = 6; i < 25; i++) {
			try {
				queue = new ArrayBlockingQueue<Boolean>(size);
				result = BlockingQueueTestUtils.testBlockingQueueManyProducerOneConsumer(queue, 3, ITERATIONS);
				System.out.println("ArrayBlockingQueue;" + size + ";3P 1C;" + result);
			} catch (Exception e) {
				System.out.println("ArrayBlockingQueue for size " + size + ", 3P 1C failed:\t" + e.getMessage());
			}
			try {
				queue = new LinkedBlockingQueue<Boolean>(size);
				result = BlockingQueueTestUtils.testBlockingQueueManyProducerOneConsumer(queue, 3, ITERATIONS);
				System.out.println("LinkedBlockingQueue;" + size + ";3P 1C;" + result);
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
