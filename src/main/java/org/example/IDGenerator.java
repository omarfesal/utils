package org.example;

import java.util.concurrent.atomic.AtomicLong;

public class IDGenerator implements Runnable {
    private static final long LIMIT = 10000000000L;
    private static AtomicLong last = new AtomicLong(0);
    private static final Object lock = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new IDGenerator(), "Thread 1");
        Thread thread2 = new Thread(new IDGenerator(), "Thread 2");
        Thread thread3 = new Thread(new IDGenerator(), "Thread 3");

        thread1.start();
        thread2.start();
        thread3.start();
    }

    @Override
    public void run() {
        long id = getID();
        System.out.println(Thread.currentThread().getName() + " generated ID: " + id);
    }

    public static long getID() {
        long id;
        synchronized (IDGenerator.class) {
            id = System.currentTimeMillis() % LIMIT;
            long currentLast = last.get();
            if (id <= currentLast) {
                id = (currentLast + 1) % LIMIT;
                last.set(id);
            }
        }
        return id;
    }
}
