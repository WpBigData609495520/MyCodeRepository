package com.wangpeng.code.juc.composeprinter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Wang.Peng on 2020/7/7.
 * 线程顺序打印A1B2C3....Z26
 * synchronized+notify+wait
 *
 * @author Wang.Peng.
 */
public class MethodOne {

    char[] numbers = "1234567".toCharArray();

    char[] characters = "ABCDEFG".toCharArray();

    final Object object = new Object();

    public static void main(String[] args) {

        MethodOne methodOne = new MethodOne();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(methodOne.new MyPrinter("number", 1));
        executorService.execute(methodOne.new MyPrinter("character", 0));
        executorService.shutdown();

    }

    class MyPrinter implements Runnable {

        /**
         * 线程名
         */
        private String name;

        /**
         * 线程类型
         */
        private int type;

        public MyPrinter(String name, int type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public void run() {
            if (type == 0) {
                synchronized (object) {
                    for (char tmp : characters) {
                        System.out.println(tmp);
                        try {
                            object.notify();
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    object.notify();
                }

            } else {
                synchronized (object) {
                    for (char tmp : numbers) {
                        System.out.print(tmp);
                        try {
                            object.notify();
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    object.notify();
                }
            }
        }

    }

}
