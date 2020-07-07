package com.wangpeng.code.juc.reftypeandthreadlocal;

import java.io.IOException;

public class T01_NormalReference {

    public static void main(String[] args) throws IOException {
        M m = new M();
        //  强引用，如果m不为空，则说明引用指向有对象存在，则gc不会进行垃圾回收
        //  堆上的内存地址
        m = null;
        System.gc(); //DisableExplicitGC

        System.in.read();
    }

}
