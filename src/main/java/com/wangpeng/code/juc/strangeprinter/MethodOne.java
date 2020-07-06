package com.wangpeng.code.juc.strangeprinter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Wang.Peng on 2020/7/6.
 * 两个线程交替打印数字
 * synchronized锁+AtomicInteger
 *
 * @author Wang.Peng.
 */
public class MethodOne {

    private int max;

    // AtomicInteger保证可见性，也可以用volatile
    private AtomicInteger status = new AtomicInteger(1);

    public MethodOne(int max) {
        this.max = max;
    }

    public static void main(String[] args) {
        MethodOne methodOne = new MethodOne(50);
        //  创建一个只有两个线程的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(methodOne.new MyPrinter("Print1", 0));
        executorService.submit(methodOne.new MyPrinter("Print2", 1));
        executorService.shutdown();
    }

    class MyPrinter implements Runnable {

        private String name;
        // 打印的类型，0：代表打印奇数，1：代表打印偶数
        private int type;

        public MyPrinter(String name, int type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public void run() {
            if (type == 1) {
                while (status.get() <= max) {
                    // 加锁，保证下面的操作是一个原子操作
                    synchronized (MethodOne.class) {
                        // 打印偶数
                        if (status.get() <= max && status.get() % 2 == 0) {
                            System.out.println(name + " - " + status.getAndIncrement());
                        }
                    }
                }
            } else {
                while (status.get() <= max) {
                    synchronized (MethodOne.class) { // 加锁
                        // 打印奇数
                        if (status.get() <= max && status.get() % 2 != 0) {
                            System.out.println(name + " - " + status.getAndIncrement());
                        }
                    }
                }
            }
        }
    }

}
