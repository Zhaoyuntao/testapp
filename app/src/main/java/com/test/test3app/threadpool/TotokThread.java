package com.test.test3app.threadpool;

/**
 * created by zhaoyuntao
 * on 2019-12-13
 * description:
 */
public class TotokThread extends Thread {

    public TotokThread() {
    }

    public TotokThread(Runnable target) {
        super(target);
    }

    public TotokThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public TotokThread(String name) {
        super(name);
    }

    public TotokThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public TotokThread(Runnable target, String name) {
        super(target, name);
    }

    public TotokThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public TotokThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }
}
