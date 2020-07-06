package com.wangpeng.code.juc.strangeprinter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Wang.Peng on 2020/7/6.
 * 两个线程交替打印数字
 * 通过flag标识实现
 *
 * @author Wang.Peng.
 */
public class MethodFour {

    private int max;
    // AtomicInteger保证可见性，也可以用volatile
    private AtomicInteger status = new AtomicInteger(1);
    private boolean oddFlag = true;

    public MethodFour(int max) {
        this.max = max;
    }

    public static void main(String[] args) {
        MethodFour strangePrinter = new MethodFour(100);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(strangePrinter.new MyPrinter("偶数Printer", 0));
        executorService.submit(strangePrinter.new MyPrinter("奇数Printer", 1));
        executorService.shutdown();
    }

    class MyPrinter implements Runnable {
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
                    if (oddFlag) {
                        System.out.println(name + " - " + status.getAndIncrement()); // 打印奇数
                        oddFlag = !oddFlag;
                    }
                }
            } else {
                while (status.get() <= max) { // 打印偶数
                    if (!oddFlag) {
                        System.out.println(name + " - " + status.getAndIncrement()); // 打印奇数
                        oddFlag = !oddFlag;
                    }
                }
            }
        }
    }
}
