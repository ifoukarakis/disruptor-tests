package gr.ifouk.performance.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class BlockingQueueProducer implements Runnable {
	private final BlockingQueue<Boolean> queue;
	private final long loops;
	private final CountDownLatch startGate;
	
	public BlockingQueueProducer(BlockingQueue<Boolean> queue, long loops, CountDownLatch startGate) {
		super();
		this.queue = queue;
		this.loops = loops;
		this.startGate = startGate;
	}

	public void run() {
		try {
			startGate.await();
			for(long l = 0; l < loops; l++) {
				queue.put(Boolean.TRUE);
			}
			queue.put(Boolean.FALSE);
		} catch(InterruptedException ie) {
			Thread.currentThread().interrupt();
		}

	}

}
