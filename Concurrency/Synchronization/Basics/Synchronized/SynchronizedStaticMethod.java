package Concurrency.Synchronization.Basics.Synchronized;

public class SynchronizedStaticMethod {
    private static int counter = 0;  // Shared resource

    // Synchronized static method to increment the counter
    public synchronized static void incrementCounter() {
        counter++;
        System.out.println("Counter: " + counter);
    }

    public static void main(String[] args) throws InterruptedException {
        // Create two threads to demonstrate concurrency
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                incrementCounter();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                incrementCounter();
            }
        });

        // Start both threads
        t1.start();
        t2.start();

        // Wait for both threads to finish
        t1.join();
        t2.join();

        System.out.println("Final Counter Value: " + counter);
    }
}
