package com.wangpeng.code.juc.strangeprinter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Wang.Peng on 2020/7/6.
 * 两个线程交替打印数字
 * 使用Object的wait和notify实现
 *
 * @author Wang.Peng.
 */
public class MethodTwo {

    // 奇数条件锁
    Object odd = new Object();

    // 偶数条件锁
    Object even = new Object();

    // AtomicInteger保证可见性，也可以用volatile
    private AtomicInteger status = new AtomicInteger(1);

    /**
     * 最大值
     */
    private int max;

    public MethodTwo(int max) {
        this.max = max;
    }

    public static void main(String[] args) {
        MethodTwo methodTwo = new MethodTwo(50);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(methodTwo.new MyPrinter("偶数Printer", 0));
        executorService.submit(methodTwo.new MyPrinter("奇数Printer", 1));
        executorService.shutdown();
    }

    class MyPrinter implements Runnable {
        //  线程名字
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
                    if (status.get() % 2 == 0) { // 如果当前为偶数，则等待
                        synchronized (odd) {
                            try {
                                odd.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.out.println(name + " - " + status.getAndIncrement()); // 打印奇数
                        synchronized (even) { // 通知偶数打印线程
                            even.notify();
                        }
                    }
                }
            } else {
                // 打印偶数
                while (status.get() <= max) {
                    // 如果当前为奇数，则等待
                    if (status.get() % 2 != 0) {
                        synchronized (even) {
                            try {
                                even.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // 打印偶数
                        System.out.println(name + " - " + status.getAndIncrement());
                        // 通知奇数打印线程
                        synchronized (odd) {
                            odd.notify();
                        }
                    }
                }
            }
        }
    }
}
