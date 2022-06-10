package com.test.test3app.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.test.test3app.R;
import im.turbo.thread.ThreadPool;
import com.zhaoyuntao.androidutils.tools.S;

public class MainActivity93_threadPool extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity93_thread_pool);

        S.s("thread pool will run after 2 seconds...");
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String a=null;
                a.equals("");
                S.s("thread pool is running");
            }
        };
        ThreadPool.runDbDelayed(1000,runnable);
//        S.s("remove from poll");
//        ThreadPool.removeFromPool(runnable);
//        scheduledFuture.cancel(true);
    }
}
