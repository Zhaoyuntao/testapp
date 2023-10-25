package com.test.test3app.interpolator;


import android.util.Log;

import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 2019-10-23
 * description:
 * this thread run at frequency you set.
 */
public abstract class ZThread extends Thread {
    private float frame;
    private float frame_real;
    private boolean flag = true;
    private boolean isStart;
    private boolean pause;
    private long timeStart;

    private Sleeper sleeper = new Sleeper();

    public ZThread(float frame) {
        this.frame = frame;
        setPriority(10);
    }

    public ZThread() {
        this(1000f);
    }

    @Override
    public synchronized void start() {
        if (isStart) {
            return;
        }
        if (frame <= 0) {
            Log.e("ZThread", "frame must be bigger than 0");
            return;
        }
        isStart = true;
        timeStart = System.currentTimeMillis();
        super.start();
    }

    protected void init() {
    }

    @Override
    public void run() {
        init();
        while (flag) {
            if (pause) {
                synchronized (sleeper) {
                    try {
                        sleeper.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
            double interval = (1000d / frame);
            //计算当前开始时间
            long time_start = System.currentTimeMillis();
            //计算本次循环的最快结束时间
            long time_end = (long) (time_start + interval);
            todo();
            long time_now = System.currentTimeMillis();
            long rest = time_end - time_now;
            if (rest > 0) {
                try {
                    Thread.sleep(rest);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
            //计算频率
            long time_end2 = System.currentTimeMillis();
            long during = time_end2 - time_start;
            frame_real = (long) (10000d / during) / 10f;
            Log.i("ZThread", "frame of zthread:" + frame_real);
//            S.s("time_start:" + time_start + "=======time_end:" + time_end + "==============time_end2:" );//+ time_end2 + "==========================during:" + during + "=============rest:" + rest + "============" + frame_real);
        }
    }

    /**
     * frame of real
     *
     * @return
     */
    public float getFrame_real() {
        return frame_real;
    }

    protected abstract void todo();

    public void close() {
        Log.i("ZThread", "zthread close");
        flag = false;
        pause = false;
        interrupt();
    }

    public boolean isClose() {
        return !flag || isInterrupted();
    }

    public void pauseThread() {
        pause = true;
    }

    public void resumeThread() {
        pause = false;
        synchronized (sleeper) {
            sleeper.notifyAll();
        }
    }

    public boolean isPause() {
        return pause;
    }

    public void setFrame(float frame) {
        this.frame = frame;
    }

    public long getTimeStart() {
        return timeStart;
    }
}

