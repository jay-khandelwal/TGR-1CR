package Concurrency.Problems;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteLockProblem {

    static class ReadWriteLock {
        private int reading = 0;
        private int pendingWrite = 0;
        private boolean writting = false;

        ReentrantLock lock = new ReentrantLock();
        Condition readCondition = lock.newCondition();
        Condition writeCondition = lock.newCondition();

        public void readLock() throws InterruptedException {
            lock.lock();
            try {
                while (pendingWrite > 0 || writting) readCondition.await();
                reading++;
            } finally {
                lock.unlock();;
            }
        }

        public void readUnlock() {
            lock.lock();
            try {
                reading--;
                writeCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void writeLock() throws InterruptedException {
            lock.lock();
            try {
                pendingWrite++;
                while (reading > 0 || writting) writeCondition.await();
                pendingWrite--;
                writting = true;
            } finally {
                lock.unlock();
            }
        }

        public void writeUnlock() {
            lock.lock();
            try {
                writting = false;
                writeCondition.signalAll();
                readCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
    
    public static void main(String[] args) {
        ReadWriteLock lock = new ReadWriteLock();

        Runnable read = () -> {
            for (int i=0; i<5; i++) {
                try {
                    lock.readLock();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Inside read lock: " + Thread.currentThread().getName());
                lock.readUnlock();
            }
        };

        Runnable write = () -> {
            for (int i=0; i<5; i++) {
                try {
                    lock.writeLock();
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Inside write lock: " + Thread.currentThread().getName());
                lock.writeUnlock();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService readPool = Executors.newFixedThreadPool(8);
        ExecutorService writePool = Executors.newFixedThreadPool(4);

        for (int i=0; i<8; i++) {
            readPool.execute(read);
        }

        for (int i=0; i<4; i++) {
            writePool.execute(write);
        }

        readPool.shutdown();
        writePool.shutdown();
    }
}
