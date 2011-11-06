package gr.ifouk.performance.blockingqueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Generic producer that adds random boolean values to a @see BlockingQueue.
 * 
 * @author ifouk 
 */
public class BlockingQueueProducer implements Runnable {
	/**
	 * The queue to add items to.
	 */
	private final BlockingQueue<Boolean> queue;
	/**
	 * Number of items to add to the queue.
	 */
	private final long loops;
	/**
	 * A latch used as a gate. The producer will not start adding items to the queue unless this
	 * count down latch has reached zero. The benefit of this approach is that we can have better accuracy
	 * when counting time duration of adding all the items. By tracking start time when the latch
	 * reaches zero, we avoid counting thread creation, thread starting time etc.  
	 */
	private final CountDownLatch startGate;
	
	/**
	 * Used to generate random values to be added to the queue.
	 */
	private final Random random = new Random(System.currentTimeMillis());
	
	/**
	 * 
	 * @param queue the queue to add items to.
	 * @param loops the number of items to add to the queue.
	 * @param startGate the latch to wait for before actually starting to add items to the queue.
	 */
	public BlockingQueueProducer(BlockingQueue<Boolean> queue, long loops, CountDownLatch startGate) {
		super();
		this.queue = queue;
		this.loops = loops;
		this.startGate = startGate;
	}

	@Override
	public void run() {
		try {
			//Wait for latch
			startGate.await();
			
			//Add items to the queue
			for(long l = 0; l < loops; l++) {
				boolean next = random.nextBoolean();
				queue.put(Boolean.valueOf(next));
			}
		} catch(InterruptedException ie) {
			//This catch is not inside the for loop because we want to stop the thread when it is
			//interrupted.
			Thread.currentThread().interrupt();
		}

	}

}
