package gr.ifouk.performance.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockingQueueTestUtils {

	public static final long testBlockingQueueOneProducerOneConsumer(BlockingQueue<Boolean> queue, long loops) throws Exception {
		System.out.println("Starting 1 producer, 1 consumer, queue: " + queue.getClass().getSimpleName());
		//Create start and end latch
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(1);
		
		//Create producer and consumer
		BlockingQueueProducer producer = new BlockingQueueProducer(queue, loops, startLatch);
		BlockingQueueConsumer consumer = new BlockingQueueConsumer(queue, loops, startLatch, endLatch);
		
		//Create executor with a thread pool of two threads to run producer and consumer.
		ExecutorService	executor = Executors.newFixedThreadPool(2);
		executor.submit(producer);
		executor.submit(consumer);
		
		//Perform garbage collection before starting the test in order to reduce possibility of 
		//interfering with time measurement.
		System.gc();
		long start = System.nanoTime();
		//Allow producer and consumer to start
		startLatch.countDown();
		
		//Await for consumer to end. Note that the consumer cannot finish unless the producer has finished.
		endLatch.await();
		
		long end = System.nanoTime();

		//Cleanup executor
		executor.shutdown();

		return (end - start);
	}
	
	/**
	 * Creates many producers and one consumer, communicating through the specified queue.
	 * 
	 * If the number of producers plus the consumer is greater than the number of the CPU's cores,
	 * context switching will happen more often, resulting in slower results.    
	 * 
	 * @param queue the queue that will be used as the buffer
	 * @param producers the number of producers to use.
	 * @param loops the number of items the producers will add to the queue in total.
	 * @return the run time (in nanoseconds) of the specified scenario, or -1 if something went wrong. 
	 * @throws Exception
	 */
	public static final long testBlockingQueueManyProducerOneConsumer(BlockingQueue<Boolean> queue, int producers, long loops) throws Exception {
		if(producers < 1) {
			//We need at least one producer
			return -1l;
		}
		
		System.out.println("Starting " + producers + " producer(s), 1 consumer, queue: " + queue.getClass().getSimpleName());
				
		
		//Check if thread count greater than number of cores/cpu's.
		int processors = Runtime.getRuntime().availableProcessors();
		if(producers + 1 > processors) {
			System.out.println((producers + 1) + " running on " + processors + " processors. Context switching will happen more often!");
		}
		
		//Create start and end latch
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(1);
		
		//Create producers and consumer
		BlockingQueueProducer producer[] = new BlockingQueueProducer[producers];
		long loopsPerProducer = loops / producers;
		for(int i = 0; i < producers - 1; i++) {
			producer[i] = new BlockingQueueProducer(queue, loopsPerProducer, startLatch);
		}
		producer[producers - 1] = new BlockingQueueProducer(queue, loopsPerProducer + (loops % producers), startLatch);
		BlockingQueueConsumer consumer = new BlockingQueueConsumer(queue, loops, startLatch, endLatch);
		
		//Create executor with a thread pool of size equal to the number of producers.
		//Note that we'll use the executor service to run the consumer and all the producers
		//except for one. One producer will be run by the current thread.
		ExecutorService executor = Executors.newFixedThreadPool(producers);
		
		executor.submit(consumer);
		for(int i = 1; i < producers ; i++) {
			executor.submit(producer[i]);
		}
		
		//Perform garbage collection before starting the test in order to reduce possibility of 
		//interfering with time measurement.
		System.gc();
		long start = System.nanoTime();
		//Allow producers and consumer to start
		startLatch.countDown();
		
		//Run one producer in current thread.
		producer[0].run();
		
		//Await for consumer to end. Note that the consumer cannot finish unless the producer has finished.
		endLatch.await();
		long end = System.nanoTime();
		
		//Cleanup executor
		executor.shutdown();
		
		return (end - start);
	}
}
