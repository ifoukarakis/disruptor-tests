package gr.ifouk.tests;

import gr.ifouk.tests.disruptor.DisruptorTestUtils;
import junit.framework.TestCase;

import com.lmax.disruptor.WaitStrategy;


public class DisruptorTest extends TestCase {

	private static final long LOOPS = 500 * 1000 * 1000;
	
	public void testDisruptor256Yield() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(256, LOOPS, WaitStrategy.Option.YIELDING);
		System.out.println("Disruptor (256, YIELDING):\t" + value + " nanoseconds");

	}

	public void testDisruptor1024Yield() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(1024, LOOPS, WaitStrategy.Option.YIELDING);
		System.out.println("Disruptor (1024, YIELDING):\t" + value + " nanoseconds");
	}
	
	public void testDisruptor16x1024Yield() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(16 * 1024, LOOPS, WaitStrategy.Option.YIELDING);
		System.out.println("Disruptor (16 * 1024, YIELDING):\t" + value + " nanoseconds");
	}

	public void testDisruptor256Blocking() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(256, LOOPS, WaitStrategy.Option.BLOCKING);
		System.out.println("Disruptor (256, BLOCKING):\t" + value + " nanoseconds");
	}

	public void testDisruptor1024Blocking() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(1024, LOOPS, WaitStrategy.Option.BLOCKING);
		System.out.println("Disruptor (1024, BLOCKING):\t" + value + " nanoseconds");
	}
	
	public void testDisruptor16x1024Blocking() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(16 * 1024, LOOPS, WaitStrategy.Option.BLOCKING);
		System.out.println("Disruptor (16 * 1024, BLOCKING):\t" + value + " nanoseconds");

	}

	public void testDisruptor256BusySpin() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(256, LOOPS, WaitStrategy.Option.BUSY_SPIN);
		System.out.println("Disruptor (256, BUSY SPIN):\t" + value + " nanoseconds");
	}

	public void testDisruptor1024BusySpin() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(1024, LOOPS, WaitStrategy.Option.BUSY_SPIN);
		System.out.println("Disruptor (1024, BUSY SPIN):\t" + value + " nanoseconds");
	}
	
	public void testDisruptor16x1024BusySpin() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(16 * 1024, LOOPS, WaitStrategy.Option.BUSY_SPIN);
		System.out.println("Disruptor (16 * 1024, BUSY SPIN):\t" + value + " nanoseconds");
	}
	

	public void testDisruptor256Sleep() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(256, LOOPS, WaitStrategy.Option.SLEEPING);
		System.out.println("Disruptor (256, SLEEPING):\t" + value + " nanoseconds");
	}

	public void testDisruptor1024Sleep() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(1024, LOOPS, WaitStrategy.Option.SLEEPING);
		System.out.println("Disruptor (1024, SLEEPING):\t" + value + " nanoseconds");

	}
	
	public void testDisruptor16x1024Sleep() throws Exception {
		long value = DisruptorTestUtils.testDisruptorOneProducerOneConsumerWithOptions(16 * 1024, LOOPS, WaitStrategy.Option.SLEEPING);
		System.out.println("Disruptor (16 * 1024, SLEEPING):\t" + value + " nanoseconds");

	}
}
