package gr.ifouk.performance.disruptor;

import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.RingBuffer;

public class DisruptorProducer implements Runnable {
	private final long loops;
	private final CountDownLatch startGate;
	private final RingBuffer<ValueEvent> ringBuffer;

	public DisruptorProducer(long loops, CountDownLatch startGate,
			RingBuffer<ValueEvent> ringBuffer) {
		super();
		this.loops = loops;
		this.startGate = startGate;
		this.ringBuffer = ringBuffer;
	}

	@Override
	public void run() {
		try {
			startGate.await();
			for (long i = 1; i < loops; i++) {
				long sequence = ringBuffer.next();
				ringBuffer.get(sequence).setIncrease(true);
				ringBuffer.publish(sequence);
			}
			long sequence = ringBuffer.next();
			ringBuffer.get(sequence).setIncrease(false);
			ringBuffer.publish(sequence);

		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

}
