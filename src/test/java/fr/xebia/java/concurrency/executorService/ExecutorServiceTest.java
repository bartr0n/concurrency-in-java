package fr.xebia.java.concurrency.executorService;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class ExecutorServiceTest {

    @Test
    public void newSingleThreadExecutorFromRunnable() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        long start = System.currentTimeMillis();

        System.out.println(Thread.currentThread().getName() + ": submitting task");

        Future<?> result1 = executor.submit(getRunnable(20l));

        while (!result1.isDone()) {
            System.out.println(Thread.currentThread().getName() + ": waiting for thread termination");
        }

        long totalTime = System.currentTimeMillis() - start;

        System.out.println(Thread.currentThread().getName() + ": total time = " + totalTime + " ms");
    }

    @Test
    public void newSingleThreadExecutorTest() throws Exception {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        long start = System.currentTimeMillis();

        System.out.println(Thread.currentThread().getName() + ": submitting tasks");

        Future<Integer> result1 = executor.submit(getCallable(3, 2000l));
        Future<Integer> result2 = executor.submit(getCallable(5, 5000l));

        System.out.println(Thread.currentThread().getName() + ": waiting for results");

        assertEquals("Wrong result", Integer.valueOf(3), result1.get());
        assertEquals("Wrong result", Integer.valueOf(5), result2.get());

        long totalTime = System.currentTimeMillis() - start;

        System.out.println(Thread.currentThread().getName() + ": total time = " + totalTime + " ms");
    }

    @Test
    public void newCachedThreadPoolTest() throws Exception {

        ExecutorService executor = Executors.newCachedThreadPool();

        long start = System.currentTimeMillis();

        System.out.println(Thread.currentThread().getName() + ": submitting tasks");

        Future<Integer> result1 = executor.submit(getCallable(3, 2000l));
        Future<Integer> result2 = executor.submit(getCallable(5, 5000l));

        System.out.println(Thread.currentThread().getName() + ": asserting");

        assertEquals("Wrong result", Integer.valueOf(3), result1.get());
        assertEquals("Wrong result", Integer.valueOf(5), result2.get());

        long totalTime = System.currentTimeMillis() - start;

        System.out.println(Thread.currentThread().getName() + ": total time = " + totalTime + " ms");
    }

    @Test
    public void shutdownTest() throws Exception {

        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println(Thread.currentThread().getName() + ": submitting tasks");

        Future<Integer> result1 = executor.submit(getCallable(3, 2000l));
        Future<Integer> result2 = executor.submit(getCallable(5, 5000l));

        executor.shutdown();

        try {
            executor.submit(getCallable(6, 1000l));
            fail("Executor has'nt been shut down");
        } catch (RejectedExecutionException ex) {
            System.out.println(Thread.currentThread().getName() + ": Execution rejected");
        }

        assertTrue("Executor hasn't been shut down", executor.isShutdown());

        assertEquals("Wrong result", Integer.valueOf(3), result1.get());
        assertEquals("Wrong result", Integer.valueOf(5), result2.get());
    }

    private Callable<Integer> getCallable(final Integer result, final long timeout) {

        return new Callable<Integer>() {
            @Override
            public Integer call() {
                try {
                    System.out.println(Thread.currentThread().getName() + ": sleeping " + timeout + " ms");
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                return result;
            }
        };
    }


    private Runnable getRunnable(final long timeout) {

        return new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + ": sleeping " + timeout + " ms");
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

}
