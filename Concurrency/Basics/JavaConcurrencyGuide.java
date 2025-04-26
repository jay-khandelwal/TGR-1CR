package Concurrency.Basics;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class JavaConcurrencyGuide {
    
    // 1. SYNCHRONIZED KEYWORD (Basic thread synchronization)
    static class SynchronizedExample {
        private int counter = 0;

        // Synchronized method
        public synchronized void increment() {
            counter++;
            System.out.println(Thread.currentThread().getName() + ": " + counter);
        }

        // Synchronized block
        public void incrementWithBlock() {
            synchronized (this) {
                counter++;
                System.out.println(Thread.currentThread().getName() + ": " + counter);
            }
        }

        public int getCounter() {
            return counter;
        }

        public static void demo() throws InterruptedException {
            SynchronizedExample example = new SynchronizedExample();
            Runnable task = () -> {
                for (int i = 0; i < 1000; i++) {
                    example.increment();
                }
            };
            Thread t1 = new Thread(task, "Thread-1");
            Thread t2 = new Thread(task, "Thread-2");
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println("Final counter (synchronized): " + example.getCounter());
        }
    }

    // 2. VOLATILE KEYWORD (Ensuring visibility across threads)
    static class VolatileExample {
        private volatile boolean running = true;

        public void stop() {
            running = false;
        }

        public void run() {
            while (running) {
                System.out.println(Thread.currentThread().getName() + " is running...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " stopped.");
        }

        public static void demo() throws InterruptedException {
            VolatileExample example = new VolatileExample();
            Thread worker = new Thread(example::run, "Worker-Thread");
            worker.start();
            Thread.sleep(2000);
            example.stop();
            worker.join();
        }
    }

    // 3. THREAD CLASS AND RUNNABLE INTERFACE (Creating threads)
    static class ThreadExample extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void demo() throws InterruptedException {
            Thread t1 = new ThreadExample();
            t1.setName("Custom-Thread");
            Runnable runnable = () -> {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread t2 = new Thread(runnable, "Runnable-Thread");
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }
    }

    // 4. LOCK INTERFACE (ReentrantLock for flexible locking)
    static class LockExample {
        private final ReentrantLock lock = new ReentrantLock();
        private int counter = 0;

        public void increment() {
            lock.lock();
            try {
                counter++;
                System.out.println(Thread.currentThread().getName() + ": " + counter);
            } finally {
                lock.unlock();
            }
        }

        public int getCounter() {
            return counter;
        }

        public static void demo() throws InterruptedException {
            LockExample example = new LockExample();
            Runnable task = () -> {
                for (int i = 0; i < 1000; i++) {
                    example.increment();
                }
            };
            Thread t1 = new Thread(task, "Thread-1");
            Thread t2 = new Thread(task, "Thread-2");
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println("Final counter (ReentrantLock): " + example.getCounter());
        }
    }

    // 5. READWRITELOCK (Separate locks for reading and writing)
    static class ReadWriteLockExample {
        private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final Map<String, String> data = new HashMap<>();

        public void put(String key, String value) {
            rwLock.writeLock().lock();
            try {
                System.out.println(Thread.currentThread().getName() + " writing: " + key);
                data.put(key, value);
                Thread.sleep(100); // Simulate work
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rwLock.writeLock().unlock();
            }
        }

        public String get(String key) {
            rwLock.readLock().lock();
            try {
                System.out.println(Thread.currentThread().getName() + " reading: " + key);
                Thread.sleep(50); // Simulate work
                return data.get(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } finally {
                rwLock.readLock().unlock();
            }
        }

        public static void demo() throws InterruptedException {
            ReadWriteLockExample example = new ReadWriteLockExample();
            Runnable writer = () -> {
                example.put("key" + Thread.currentThread().getName(), "value");
            };
            Runnable reader = () -> {
                example.get("key" + Thread.currentThread().getName());
            };
            Thread writer1 = new Thread(writer, "Writer-1");
            Thread reader1 = new Thread(reader, "Reader-1");
            Thread reader2 = new Thread(reader, "Reader-2");
            writer1.start();
            reader1.start();
            reader2.start();
            writer1.join();
            reader1.join();
            reader2.join();
        }
    }

    // 6. ATOMIC VARIABLES (Lock-free thread-safe operations)
    static class AtomicExample {
        private AtomicInteger counter = new AtomicInteger(0);

        public void increment() {
            int newValue = counter.incrementAndGet();
            System.out.println(Thread.currentThread().getName() + ": " + newValue);
        }

        public int getCounter() {
            return counter.get();
        }

        public static void demo() throws InterruptedException {
            AtomicExample example = new AtomicExample();
            Runnable task = () -> {
                for (int i = 0; i < 1000; i++) {
                    example.increment();
                }
            };
            Thread t1 = new Thread(task, "Thread-1");
            Thread t2 = new Thread(task, "Thread-2");
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println("Final counter (AtomicInteger): " + example.getCounter());
        }
    }

    // 7. EXECUTOR SERVICE (Thread pool management)
    static class ExecutorServiceExample {
        public static void demo() throws InterruptedException {
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Runnable task = () -> {
                for (int i = 0; i < 3; i++) {
                    System.out.println(Thread.currentThread().getName() + ": Task " + i);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            executor.submit(task);
            executor.submit(task);
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("ExecutorService tasks completed.");
        }
    }

    // 8. FORK/JOIN FRAMEWORK (Parallel task splitting)
    static class ForkJoinExample extends RecursiveTask<Integer> {
        private final int[] array;
        private final int start, end;
        private static final int THRESHOLD = 10;

        public ForkJoinExample(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= THRESHOLD) {
                int sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            } else {
                int mid = start + (end - start) / 2;
                ForkJoinExample left = new ForkJoinExample(array, start, mid);
                ForkJoinExample right = new ForkJoinExample(array, mid, end);
                left.fork();
                int rightResult = right.compute();
                int leftResult = left.join();
                return leftResult + rightResult;
            }
        }

        public static void demo() {
            ForkJoinPool pool = ForkJoinPool.commonPool();
            int[] array = new int[100];
            Arrays.fill(array, 1); // Array of 100 ones
            ForkJoinExample task = new ForkJoinExample(array, 0, array.length);
            int sum = pool.invoke(task);
            System.out.println("Fork/Join sum: " + sum);
        }
    }

    // 9. COUNTDOWNLATCH (Synchronize thread completion)
    static class CountDownLatchExample {
        public static void demo() throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(3);
            Runnable task = () -> {
                System.out.println(Thread.currentThread().getName() + " is working...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " finished.");
                latch.countDown();
            };
            ExecutorService executor = Executors.newFixedThreadPool(3);
            executor.submit(task);
            executor.submit(task);
            executor.submit(task);
            latch.await(); // Wait for all tasks to finish
            System.out.println("All CountDownLatch tasks completed.");
            executor.shutdown();
        }
    }

    // 10. CYCLICBARRIER (Synchronize threads at a point)
    static class CyclicBarrierExample {
        public static void demo() throws InterruptedException {
            CyclicBarrier barrier = new CyclicBarrier(3, () -> {
                System.out.println("All threads reached the barrier!");
            });
            Runnable task = () -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is working...");
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " reached barrier.");
                    barrier.await();
                    System.out.println(Thread.currentThread().getName() + " passed barrier.");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            };
            ExecutorService executor = Executors.newFixedThreadPool(3);
            executor.submit(task);
            executor.submit(task);
            executor.submit(task);
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // 11. SEMAPHORE (Control resource access)
    static class SemaphoreExample {
        private final Semaphore semaphore = new Semaphore(2); // Allow 2 threads at a time

        public void accessResource() {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " acquired resource.");
                Thread.sleep(1000); // Simulate work
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + " released resource.");
                semaphore.release();
            }
        }

        public static void demo() throws InterruptedException {
            SemaphoreExample example = new SemaphoreExample();
            Runnable task = example::accessResource;
            ExecutorService executor = Executors.newFixedThreadPool(4);
            for (int i = 0; i < 4; i++) {
                executor.submit(task);
            }
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // 12. COMPLETABLEFUTURE (Asynchronous programming)
    static class CompletableFutureExample {
        public static void demo() throws Exception {
            CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "Hello";
            }).thenApply(s -> s + ", World!")
              .thenAccept(System.out::println)
              .get(); // Wait for completion
            System.out.println("CompletableFuture task completed.");
        }
    }

    // 13. THREADLOCAL (Per-thread variables)
    // static class ThreadLocalExample {
    //     private static final ThreadLocal<Integer> threadId = ThreadLocal.withInitial(() -> Thread.currentThread().getId());

    //     public static void demo() throws InterruptedException {
    //         Runnable task = () -> {
    //             System.out.println(Thread.currentThread().getName() + " has ThreadLocal ID: " + threadId.get());
    //         };
    //         Thread t1 = new Thread(task, "Thread-1");
    //         Thread t2 = new Thread(task, "Thread-2");
    //         t1.start();
    //         t2.start();
    //         t1.join();
    //         t2.join();
    //     }
    // }

    // Main method to run all demos
    public static void main(String[] args) throws Exception {
        System.out.println("=== Synchronized Demo ===");
        SynchronizedExample.demo();
        System.out.println("\n=== Volatile Demo ===");
        VolatileExample.demo();
        System.out.println("\n=== Thread Demo ===");
        ThreadExample.demo();
        System.out.println("\n=== Lock Demo ===");
        LockExample.demo();
        System.out.println("\n=== ReadWriteLock Demo ===");
        ReadWriteLockExample.demo();
        System.out.println("\n=== Atomic Demo ===");
        AtomicExample.demo();
        System.out.println("\n=== ExecutorService Demo ===");
        ExecutorServiceExample.demo();
        System.out.println("\n=== Fork/Join Demo ===");
        ForkJoinExample.demo();
        System.out.println("\n=== CountDownLatch Demo ===");
        CountDownLatchExample.demo();
        System.out.println("\n=== CyclicBarrier Demo ===");
        CyclicBarrierExample.demo();
        System.out.println("\n=== Semaphore Demo ===");
        SemaphoreExample.demo();
        System.out.println("\n=== CompletableFuture Demo ===");
        CompletableFutureExample.demo();
        // System.out.println("\n=== ThreadLocal Demo ===");
        // ThreadLocalExample.demo();
    }
}