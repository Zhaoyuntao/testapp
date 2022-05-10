package com.test.test3app.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.loading.TProgressView;
import com.test.test3app.loading.ViewMode;
import com.zhaoyuntao.androidutils.tools.T;


public class MainActivity_999_loading extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_999_loading);

        TProgressView loadingView = findViewById(R.id.loading);

        loadingView.addViewMode(
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
                .drawable(R.drawable.zayhu_video_call_hangup_drawable)
                .listener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.t(activity(), "click LoadingView2");
                    }
                }));
        TProgressView loadingView3 = findViewById(R.id.loading3);
        loadingView3.setViewMode(new ViewMode("normal")
                .rotate(false)
                .showProgress(true)
                .listener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.t(activity(), "click LoadingView3");
                    }
                }));
        findViewById(R.id.progress_touch).setOnTouchListener(new View.OnTouchListener() {
            int current = 0;
            int total = 100;
            float x = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int add = 2;
                    if (event.getX() - x < 0) {
                        current = current - add;
                    } else if (event.getX() - x > 0) {
                        current = current + add;
                    }
                    loadingView3.setTotalProgress(current, total);
                    x = event.getX();
                } else {
                    x = 0;
                }
                return true;
            }
        });

    }
}
