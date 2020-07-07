package com.wangpeng.code.juc.reftypeandthreadlocal;

import java.lang.ref.WeakReference;

/**
 * 弱引用遭到gc就会回收
 * 一般用在容器
 * WeakHashMap
 */
public class T03_WeakReference {

    public static void main(String[] args) {

        WeakReference<M> m = new WeakReference<>(new M());

        System.out.println(m.get());
        System.gc();
        System.out.println(m.get());

        ThreadLocal<M> tl = new ThreadLocal<>();
        tl.set(new M());
        //  ThreadLocal用完必须要remove掉，因为Entry继承了WeakReference key是一个弱引用，当key被回收的时候
        //  key为空，value并不会被回收且无法被访问到，以此累计，会导致内存泄漏，可能会发生oom
        tl.remove();

    }

}

