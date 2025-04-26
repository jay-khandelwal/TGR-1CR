package Concurrency.Synchronization.Basics;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
    
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);

        Runnable r1 = () -> {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " latch count down");
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread task1 = new Thread(r1, "Task 1");
        Thread task2 = new Thread(r1, "Task 2");
        Thread task3 = new Thread(r1, "Task 3");
        
        task1.start();
        task2.start();
        task3.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All tasks completed!");
    }

}
