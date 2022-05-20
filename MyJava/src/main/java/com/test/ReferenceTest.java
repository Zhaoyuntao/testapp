package com.test;

import com.zhaoyuntao.myjava.S;

/**
 * created by zhaoyuntao
 * on 18/05/2022
 * description:
 */
class ReferenceTest {


    public static void main(String[] args) {
        S.s("start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Activityc activity = new Activityc();
                activity.show();
                System.gc();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        activity.destroy();
                        System.gc();
                    }
                }).start();
            }
        }).start();
        S.s("end");
    }
}
