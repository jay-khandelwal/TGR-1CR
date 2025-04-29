package Concurrency.Problems.ProducerConsumerProblem.V1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static volatile boolean running = true;
    private static int PRODUCER_COUNT = 300;
    private static int CONSUMER_COUNT = 10;

    static class QueueNode<T> {
        T data;
        QueueNode<T> next;

        public QueueNode(T data) {
            this.data = data;
            this.next = null;
        }
    }

    static class BlockingQueue<T> {
        QueueNode<T> head, tail;
        int capacity;
        int size = 0;

        public BlockingQueue(int capacity) {
            head = null;
            tail = null;
            this.capacity = capacity;
        }

        public synchronized boolean isEmpty() {
            return size == 0;
        }

        public synchronized void offer(T data) {
            QueueNode<T> node = new QueueNode<T>(data);
            while (capacity <= size) {
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }
            if (isEmpty()) {
                head = tail = node;
            } else {
                tail.next = node;
                tail = tail.next;
            }
            size++;
            notifyAll();
        }

        public synchronized T poll() {
            while (isEmpty()) {
                if (!running)
                    return null;
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }
            QueueNode<T> node = head;
            if (head.next == null) {
                head = tail = null;
            } else {
                head = head.next;
            }
            size--;
            notifyAll();
            return node.data;
        }
    }

    public static void main(String[] args) {

        BlockingQueue<String> queue = new BlockingQueue<>(10);

        Runnable producer = () -> {
            for (int i = 0; i < 60; i++) {
                String message = i + " - " + Thread.currentThread().getName();
                System.out.println("producing... " + message);
                queue.offer(message);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consumer = () -> {
            while (running || !queue.isEmpty()) {
                System.out.println("Consumed " + Thread.currentThread().getName() + " message: " + queue.poll());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService producers = Executors.newFixedThreadPool(PRODUCER_COUNT);
        ExecutorService consumers = Executors.newFixedThreadPool(CONSUMER_COUNT);

        for (int i = 0; i < PRODUCER_COUNT; i++) {
            producers.execute(producer);
        }

        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumers.execute(consumer);
        }

        producers.shutdown();

        // Very important: shutdown() is non-blocking — it just stops accepting new tasks.
        // So if we don't have this below `producers.awaitTermination()`,
        // we quickly reach the `running = false` line and our consumer gets exit.
        // because it is possible that at that time the producer won't be able to push even one message,
        // or some consumers may have already consumed messages, leaving the queue empty for others.
        
        try {
            if (!producers.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("Producers still running, forcing shutdown...");
                producers.shutdownNow();
            }
        } catch (InterruptedException e) {
            producers.shutdownNow();
        }

        running = false;

        synchronized (queue) {
            queue.notifyAll();
        }

        consumers.shutdown();
    }

}
