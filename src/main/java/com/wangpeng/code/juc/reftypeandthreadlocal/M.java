package com.wangpeng.code.juc.reftypeandthreadlocal;

public class M {

    @Override
    protected void finalize() {
        System.out.println("finalize");
    }

}
