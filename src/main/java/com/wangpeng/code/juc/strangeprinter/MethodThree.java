package com.wangpeng.code.juc.strangeprinter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Wang.Peng on 2020/7/6.
 * 交替打印数字
 * 使用ReentrantLock+Condition实现
 *
 * @author Wang.Peng.
 */
public class MethodThree {

    private int max;

    // AtomicInteger保证可见性，也可以用volatile
    private AtomicInteger status = new AtomicInteger(1);

    private ReentrantLock lock = new ReentrantLock();

    private Condition odd = lock.newCondition();

    private Condition even = lock.newCondition();

    public MethodThree(int max) {
        this.max = max;
    }

    public static void main(String[] args) {
        MethodThree strangePrinter = new MethodThree(50);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(strangePrinter.new MyPrinter("偶数Printer", 0));
        executorService.submit(strangePrinter.new MyPrinter("奇数Printer", 1));
        executorService.shutdown();
    }

    class MyPrinter implements Runnable {

        //  线程名
        private String name;
        //  打印的类型，0：代表打印奇数，1：代表打印偶数
        private int type;

        public MyPrinter(String name, int type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public void run() {
            if (type == 1) {
                while (status.get() <= max) { // 打印奇数
                    lock.lock();
                    try {
                        if (status.get() % 2 == 0) {
                            odd.await();
                        }
                        if (status.get() <= max) {
                            // 打印奇数
                            System.out.println(name + " - " + status.getAndIncrement());
                        }
                        even.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            } else {
                while (status.get() <= max) { // 打印偶数
                    lock.lock();
                    try {
                        if (status.get() % 2 != 0) {
                            even.await();
                        }
                        if (status.get() <= max) {
                            System.out.println(name + " - " + status.getAndIncrement()); // 打印偶数
                        }
                        odd.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }

}
