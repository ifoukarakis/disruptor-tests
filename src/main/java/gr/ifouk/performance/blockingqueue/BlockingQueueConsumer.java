package gr.ifouk.performance.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class BlockingQueueConsumer implements Runnable {
	private final BlockingQueue<Boolean> queue;
	private long value;
	private final long loops;
	private final CountDownLatch startGate;
	private final CountDownLatch endGate;

	public BlockingQueueConsumer(BlockingQueue<Boolean> queue, long loops,
			CountDownLatch startGate, CountDownLatch endGate) {
		super();
		this.queue = queue;
		this.loops = loops;
		this.startGate = startGate;
		this.endGate = endGate;
	}

	public void run() {
		try {
			startGate.await();
			Boolean item = Boolean.TRUE;
			for(int i = 0; i < loops; i++) {
				item = queue.take();
				if(item.booleanValue())
					value++;
				else
					value--;
			}
			endGate.countDown();
		} catch(InterruptedException ie) {
			Thread.currentThread().interrupt();
		}

	}

	public long getValue() {
		return value;
	}
}
