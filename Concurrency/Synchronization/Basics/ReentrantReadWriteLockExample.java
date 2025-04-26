package Concurrency.Synchronization.Basics;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockExample {

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    
    public void read() {
        readLock.lock();
        try {
            System.out.println("Reading data...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Read operation interrupted");
        } finally {
            readLock.unlock();
            System.out.println("Read lock released");
        }
    }

    public void write() {
        writeLock.lock();
        try {
            System.out.println("Writing data...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Write operation interrupted");
        } finally {
            writeLock.unlock();
            System.out.println("Write lock released");
        }
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockExample example = new ReentrantReadWriteLockExample();

        Thread readThread1 = new Thread(example::read);
        Thread readThread2 = new Thread(example::read);
        Thread writeThread = new Thread(example::write);

        readThread1.start();
        readThread2.start();
        writeThread.start();

        try {
            readThread1.join();
            readThread2.join();
            writeThread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }
        System.out.println("All operations completed");
    }
    
}
