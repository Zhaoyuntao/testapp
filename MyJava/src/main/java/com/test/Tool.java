package com.test;

import com.zhaoyuntao.myjava.S;

import java.lang.ref.WeakReference;

/**
 * created by zhaoyuntao
 * on 18/05/2022
 * description:
 */
class Tool {
    public static void process(final Callback callback) {
        final WeakReference<Callback> weakReference = new WeakReference<>(callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Callback callback1 = weakReference.get();
                    if (callback1 != null) {
                        callback1.onSuccess();
                    } else {
                        S.e("=====>");
                    }
                }
            }
        }).start();
    }
}
