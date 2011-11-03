package gr.ifouk.performance.disruptor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.Sequencer;

public class DisruptorConsumer implements EventHandler<ValueEvent>, Runnable {
	private final AtomicLong value;
	private final CountDownLatch startGate;
	private final RingBuffer<ValueEvent> ringBuffer;
	private final SequenceBarrier sequenceBarrier;
	private final Sequence sequence = new Sequence(
			Sequencer.INITIAL_CURSOR_VALUE);

	private boolean halt = false;
	
	public DisruptorConsumer(AtomicLong value, CountDownLatch startGate,
			RingBuffer<ValueEvent> ringBuffer, SequenceBarrier sequenceBarrier) {
		super();
		this.value = value;
		this.startGate = startGate;
		this.ringBuffer = ringBuffer;
		this.sequenceBarrier = sequenceBarrier;
		ringBuffer.setGatingSequences(this.sequence);
	}

	public void onEvent(ValueEvent event, long sequence, boolean endOfBatch) {
		value.incrementAndGet();
	}

	@Override
	public void run() {
		try {
			sequenceBarrier.clearAlert();
			startGate.await();

			ValueEvent event = null;
			long nextSequence = sequence.get() + 1L;
			boolean value = true;
			while (value) {
				try {
					final long availableSequence = sequenceBarrier.waitFor(nextSequence);
					while (nextSequence <= availableSequence) {
						event = ringBuffer.get(nextSequence);
						value = event.getIncrease();
						onEvent(event, nextSequence, nextSequence == availableSequence);
						
						nextSequence++;
					}

					sequence.set(nextSequence - 1L);
				} catch (AlertException e) {
					//On error stop thread
					break;
				}
			}
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

}
