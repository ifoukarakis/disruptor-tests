package gr.ifouk.performance.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class BlockingQueueConsumer implements Runnable {
	private final BlockingQueue<Boolean> queue;
	private final AtomicLong value;
	private final CountDownLatch startGate;
	
	public BlockingQueueConsumer(BlockingQueue<Boolean> queue, AtomicLong value, CountDownLatch startGate) {
		super();
		this.queue = queue;
		this.value = value;
		this.startGate = startGate;
	}

	public void run() {
		try {
			startGate.await();
			Boolean item = Boolean.TRUE;
			while(item.booleanValue()) {
				item = queue.take();
				if(item.booleanValue())
					value.incrementAndGet();
			}
			queue.put(item);
		} catch(InterruptedException ie) {
			Thread.currentThread().interrupt();
		}

	}

}
