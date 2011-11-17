package gr.ifouk.tests.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class BlockingQueueConsumer implements Runnable {
	/**
	 * The queue to read items from.
	 */
	private final BlockingQueue<Boolean> queue;
	
	/**
	 * The value that will change over time. 
	 */
	private long value;
	
	/**
	 * The number of items to read from the queue.
	 */
	private final long loops;
	
	/**
	 * We will wait for this gate before starting.
	 */
	private final CountDownLatch startGate;
	
	/**
	 * We will inform other threads that we are done
	 * by subtracting one from this gate.
	 */
	private final CountDownLatch endGate;

	public BlockingQueueConsumer(BlockingQueue<Boolean> queue, long loops,
			CountDownLatch startGate, CountDownLatch endGate) {
		super();
		this.queue = queue;
		this.loops = loops;
		this.value = 0l;
		this.startGate = startGate;
		this.endGate = endGate;
	}

	
	public void run() {
		try {
			//Wait for latch
			startGate.await();
			
			Boolean item;
			
			//Read items from queue and process them
			for(int i = 0; i < loops; i++) {
				item = queue.take();
				if(item.booleanValue())
					value++;
				else
					value--;
			}
			
			//Inform that we are done.
			endGate.countDown();
		} catch(InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

	public long getValue() {
		return value;
	}
}
