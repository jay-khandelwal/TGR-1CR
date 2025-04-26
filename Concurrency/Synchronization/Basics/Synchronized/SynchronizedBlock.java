package Concurrency.Synchronization.Basics.Synchronized;

public class SynchronizedBlock {
    private int counter = 0;  // Shared resource

    // Method to increment the counter using synchronized block
    public void incrementCounter() {
        // Synchronizing only the critical section (the counter increment)
        synchronized (this) {
            counter++;
            System.out.println("Counter: " + counter);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedBlock syncBlock = new SynchronizedBlock();

        // Create two threads to demonstrate concurrency
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                syncBlock.incrementCounter();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                syncBlock.incrementCounter();
            }
        });

        // Start both threads
        t1.start();
        t2.start();

        // Wait for both threads to finish
        t1.join();
        t2.join();

        System.out.println("Final Counter Value: " + syncBlock.counter);
    }
}

