package gr.ifouk.performance.disruptor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.ClaimStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;

public class DisruptorTestUtils {

	public static final long testDisruptorOneProducerOneConsumerWithOptions(int ringSize, long loops, WaitStrategy.Option waitStrategy) throws InterruptedException {
		//Create ring buffer
		final RingBuffer<ValueEvent> ringBuffer =
			    new RingBuffer<ValueEvent>(ValueEvent.EVENT_FACTORY, ringSize, 
			                               ClaimStrategy.Option.SINGLE_THREADED,
			                               waitStrategy);
	    final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
	    CountDownLatch startLatch = new CountDownLatch(1);
	    CountDownLatch endLatch = new CountDownLatch(1);

	    //Create producer and consumer
	    DisruptorProducer producer = new DisruptorProducer(loops, ringBuffer, startLatch);
	    DisruptorConsumer consumer = new DisruptorConsumer(ringBuffer, sequenceBarrier, loops, startLatch, endLatch);
	    
	    //Create executor with a thread pool of two threads to run producer and consumer.
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(consumer);
	   
		//Perform garbage collection before starting the test in order to reduce possibility of 
		//interfering with time measurement.
		System.gc();
		long start = System.nanoTime();
		//Allow producer and consumer to start
		startLatch.countDown();
		
		//Start producer
		producer.run();
		
		//Await for consumer to end. Note that the consumer cannot finish unless the producer has finished (added
		//all items to ring buffer).
		endLatch.await();
		
		long end = System.nanoTime();
		
		executor.shutdown();
		
		return (end - start);
	}
	
	
	public static final long testDisruptorManyProducersOneConsumerWithOptions(int ringSize, int producers, long loops, WaitStrategy.Option waitStrategy) throws InterruptedException {
		
		if(producers < 1) {
			//We need at least one producer
			return -1l;
		}
		
		//Check if thread count greater than number of cores/cpu's.
		int processors = Runtime.getRuntime().availableProcessors();
		if(producers + 1 > processors) {
			System.out.println((producers + 1) + " running on " + processors + " processors. Context switching will happen more often!");
		}
		
		//Create ring buffer
		final RingBuffer<ValueEvent> ringBuffer =
			    new RingBuffer<ValueEvent>(ValueEvent.EVENT_FACTORY, ringSize, 
			                               ClaimStrategy.Option.MULTI_THREADED,
			                               waitStrategy);
	    final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
	    CountDownLatch startLatch = new CountDownLatch(1);
	    CountDownLatch endLatch = new CountDownLatch(1);

	    //Create producer and consumer
	    long loopsPerProducer = loops / producers;
	    DisruptorProducer[] producer = new DisruptorProducer[producers];
	    producer[0] = new DisruptorProducer(loopsPerProducer + (loops % producers), ringBuffer, startLatch);
	    for(int i = 1; i < producers; i++) {
	    	producer[i] = new DisruptorProducer(loopsPerProducer, ringBuffer, startLatch);
	    }
	    DisruptorConsumer consumer = new DisruptorConsumer(ringBuffer, sequenceBarrier, loops, startLatch, endLatch);
	    
	    //Create executor with a thread pool of two threads to run producer and consumer.
		ExecutorService executor = Executors.newFixedThreadPool(producers);
	    for(int i = 1; i < producers; i++) {
	    	executor.submit(producer[i]);
	    }
		executor.execute(consumer);
	   
		//Perform garbage collection before starting the test in order to reduce possibility of 
		//interfering with time measurement.
		System.gc();
		long start = System.nanoTime();
		//Allow producer and consumer to start
		startLatch.countDown();
		
		//Run one producer in current thread.
		producer[0].run();
		
		//Await for consumer to end. Note that the consumer cannot finish unless the producer has finished (added
		//all items to ring buffer).
		endLatch.await();
		
		long end = System.nanoTime();
		
		executor.shutdown();
		
		return (end - start);
	}
}
