package Concurrency.Synchronization.Basics;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {

    public static void main(String[] args) {
        
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("All parties have arrived at the barrier, let's proceed!");
        });

        Runnable r1 = () -> {
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + " has reached the barrier.");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " has crossed the barrier.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Thread thread1 = new Thread(r1);
        Thread thread2 = new Thread(r1);
        Thread thread3 = new Thread(r1);

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("All threads have finished execution.");
    }
    
}
