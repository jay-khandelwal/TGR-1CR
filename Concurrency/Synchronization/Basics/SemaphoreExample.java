package Concurrency.Synchronization.Basics;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {

    // A semaphore with 2 permits
    private static final Semaphore semaphore = new Semaphore(2);

    public static void main(String[] args) {
        // Start 5 worker threads
        for (int i = 1; i <= 5; i++) {
            Thread worker = new Thread(new Task("Worker-" + i));
            worker.start();
        }
    }

    static class Task implements Runnable {
        private final String name;

        Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                System.out.println(name + " is waiting for a permit...");
                semaphore.acquire();
                System.out.println(name + " acquired a permit ✅");

                // Simulate work
                Thread.sleep(2000);

                System.out.println(name + " is releasing the permit...");
                semaphore.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(name + " was interrupted.");
            }
        }
    }
}