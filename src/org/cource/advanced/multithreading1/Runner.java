package org.cource.advanced.multithreading1;


//7. Multi threading p.1:
//    - Volatile, synchronized
//    - Wait, notify
//    - Locks (dead locks)


import org.cource.advanced.multithreading1.beans.Book;
import org.cource.advanced.multithreading1.beans.BookStore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// Plan:
// Theory: object's monitors, synchronized, wait, notify, notifyAll
// Simple example: 1. how-tos with basic multithreading and resource sharing. 2. Book store example (BookStore with List<Book> and sleeps)
// Theory on volatile. No big sense for code example
// Hard example (if we'll have a time): simultaneous push and pop tasks to the single queue
public class Runner {
    public static void main(String[] args) throws InterruptedException {
        basicExampleOfSynchronized();


        waitNotifyExample();

        System.out.println("\n\n\n\n\n");
        showDeadLockExample();


        var bookStore = new BookStore();
        var allBooks = bookStore.getAllBooks();
//        allBooks.add(new Book("", ""));


    }

    private static void showDeadLockExample() {
        var monitorObject1 = new Object();
        var monitorObject2 = new Object();

        var threadList1 = Stream.generate(() -> new Thread(() -> {
            synchronized (monitorObject1) {
                synchronized (monitorObject2) {
                    sleep(10L);
                }
            }
                }))
                .limit(100).toList();

        var threadList2 = Stream.generate(() -> new Thread(() -> {
            synchronized (monitorObject2) {
                synchronized (monitorObject1) {
                    sleep(10L);
                }
            }
                }))
                .limit(100).toList();

        System.out.println("Going to show dead lock example");
        for(var thread : threadList1) {
            thread.start();
        }

        // TODO: uncomment me to make a dead lock
//        for(var thread : threadList2) {
//            thread.start();
//        }

        sleep(150L);
        System.out.println("Have to be some dead lock now");


//        for(var thread : threadList1) {
//            thread.join();
//        }
    }

    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
    }

    private static void waitNotifyExample() throws InterruptedException {
        var monitorObject = new Object();
        var threadList = Stream.generate(() -> new Thread(() -> {
                    synchronized (monitorObject) {
                        try {
                            System.out.println("Thread has gone to wait");
                            monitorObject.wait();
                            System.out.println("Thread has woke up");
                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
                        }
                    }
                }))
                .limit(100).toList();

        for(var thread : threadList) {
            thread.start();
        }

        Thread.sleep(1500L);

        System.out.println("Going to wake up threads");
        synchronized (monitorObject) {
            monitorObject.notifyAll();
        }

        for(var thread : threadList) {
            thread.join();
        }
    }

    private static void basicExampleOfSynchronized() throws InterruptedException {
        var strings = new ArrayList<String>();
        strings.add("str1");
        strings.add("str2");
        strings.add("str3");


        var threadList = Stream.generate(() -> new Thread(() -> doSmthWithList(strings)))
                .limit(10000).toList();

        for(var thread : threadList) {
            thread.start();
        }

        for(var thread : threadList) {
            thread.join();
        }

        System.out.println(strings);
    }

    private static synchronized void doSmthWithList(List<String> list) {
//        synchronized (Runner.class) {
            list.add("string");
            list.remove(0);
//        }
    }
}
