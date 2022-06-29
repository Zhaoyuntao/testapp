package com.test.test3app.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import base.ui.BaseActivity;
import com.test.test3app.R;
import im.turbo.baseui.progress.TProgressView;
import im.turbo.baseui.progress.ViewMode;
import com.zhaoyuntao.androidutils.tools.T;


public class Activity_999_loading extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_999_loading);

        TProgressView loadingView = findViewById(R.id.loading);

        loadingView.setViewMode(
                new ViewMode("downloading")
                        .showProgress(true)
                        .rotate(true)
                        .useWaveProgress(true)
                        .drawable(R.drawable.svg_transfer_cancel)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadingView.setCurrentMode("canceled");
                            }
                        }),
                new ViewMode("success")
                        .showProgress(false)
                        .drawable(R.drawable.svg_transfer_success)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                T.t(activity(), "clicked success");
                            }
                        }),
                new ViewMode("canceled")
                        .showProgress(false)
                        .drawable(R.drawable.svg_transfer_upload)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadingView.setCurrentMode("downloading");
                            }
                        }),
                new ViewMode("failed")
                        .rotate(true)
                        .showProgress(false)
                        .drawable(R.drawable.svg_transfer_failed)
                        .listener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadingView.setCurrentMode("downloading");
                            }
                        })
        );
        loadingView.setCurrentMode("downloading");

        findViewById(R.id.mode_canceled).setOnClickListener(new View.OnClickListener() {
            private boolean a;

            @Override
            public void onClick(View v) {
                loadingView.setCurrentMode("canceled");
            }
        });
        findViewById(R.id.mode_downloading).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingView.setCurrentMode("downloading");
            }
        });
        findViewById(R.id.mode_success).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingView.setCurrentMode("success");
            }
        });
        findViewById(R.id.mode_failed).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingView.setCurrentMode("failed");
            }
        });

        TProgressView loadingView2 = findViewById(R.id.loading2);
        loadingView2.setViewMode(new ViewMode("normal")
                .rotate(true)
                .useWaveProgress(true)
                .showProgress(true)
                .drawable(R.drawable.zayhu_video_call_hangup_drawable)
                .listener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.t(activity(), "click LoadingView2");
                    }
                }));
        TProgressView loadingView3 = findViewById(R.id.loading3);
        loadingView3.setViewMode(new ViewMode("normal")
                .showProgress(true)
                .listener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.t(activity(), "click LoadingView3");
                    }
                }));
        findViewById(R.id.progress_touch).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int width = v.getWidth();
                loadingView3.setProgress(event.getX(), width);
                return true;
            }
        });

    }
}
