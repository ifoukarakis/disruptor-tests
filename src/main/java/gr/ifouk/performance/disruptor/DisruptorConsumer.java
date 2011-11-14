package gr.ifouk.performance.disruptor;

import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.Sequencer;

public class DisruptorConsumer implements EventHandler<ValueEvent>, Runnable {
	private final CountDownLatch startGate, endGate;
	private final RingBuffer<ValueEvent> ringBuffer;
	private final SequenceBarrier sequenceBarrier;
	private final Sequence sequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
	
	private long value;
	
	private final long loops;
	
	public DisruptorConsumer(RingBuffer<ValueEvent> ringBuffer, SequenceBarrier sequenceBarrier, long loops, 
			CountDownLatch startGate, CountDownLatch endGate) {
		super();
		this.startGate = startGate;
		this.endGate = endGate;
		this.ringBuffer = ringBuffer;
		this.sequenceBarrier = sequenceBarrier;
		this.value = 0l;
		this.loops = loops;
		ringBuffer.setGatingSequences(this.sequence);
	}

	public void onEvent(ValueEvent event, long sequence, boolean endOfBatch) {
		if(event.getIncrease())
			value++;
		else
			value--;
	}

	@Override
	public void run() {
		try {
			//Number of items read
			long count = 0l;
			
			sequenceBarrier.clearAlert();
			startGate.await();

			ValueEvent event = null;
			long nextSequence = sequence.get() + 1l;
			while (count < this.loops) {
				try {
					final long availableSequence = sequenceBarrier.waitFor(nextSequence);
					while (nextSequence <= availableSequence) {
						event = ringBuffer.get(nextSequence);
						onEvent(event, nextSequence, nextSequence == availableSequence);

						nextSequence++;
						count++;
					}

					sequence.set(nextSequence - 1l);
				} catch (AlertException e) {
					System.out.println("Disruptor Consumer ended unexpectedly!");
					break;
				}
			}
			endGate.countDown();
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

}
