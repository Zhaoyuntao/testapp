package com.test.test3app.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.T;

import base.ui.BaseActivity;
import im.thebot.api.chat.constant.FileStatusCode;
import im.turbo.basetools.state.StateFetchListener;
import im.turbo.baseui.progress.TProgressView;
import im.turbo.baseui.progress.ViewMode;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;


public class Activity_999_loading extends BaseActivity {
    private SafeRunnable safeRunnable;
    private int mode = FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING;
    private boolean touchChangeProgress;
    private int progressNow;
    private TProgressView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_999_loading);

        loadingView = findViewById(R.id.loading);
        safeRunnable = new SafeRunnable(this) {
            @Override
            protected void runSafely() {
                if (touchChangeProgress) {
                    return;
                }
                if (mode == FileStatusCode.STATUS_FILE_DOWNLOADING) {
                    progressNow += 10;
                    if (progressNow > 100) {
                        progressNow = 0;
                    }
                    loadingView.setProgress(progressNow, 100);
                    ThreadPool.runUiDelayed(500, this);
                }
            }
        };

        setViewMode();

        findViewById(R.id.mode_init).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setViewMode();
            }
        });
        findViewById(R.id.mode_waiting).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING);
            }
        });
        findViewById(R.id.mode_downloading).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOADING);
            }
        });
        findViewById(R.id.mode_success).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED);
            }
        });
        findViewById(R.id.mode_failed).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED);
            }
        });
        findViewById(R.id.mode_canceled).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.setCurrentMode(FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED);
            }
        });

        TProgressView loadingView2 = findViewById(R.id.loading2);
        loadingView2.setViewMode(new ViewMode("normal")
                .rotate(true)
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
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mode == FileStatusCode.STATUS_FILE_DOWNLOADING) {
                        touchChangeProgress = true;
                        int width = v.getWidth();
                        loadingView.setProgress(event.getX(), width);
                    }
                }
                return true;
            }
        });

    }

    private void setViewMode() {
        loadingView.setViewMode(
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING)
                        .showProgress(true)
                        .rotate(true)
                        .drawable(R.drawable.svg_transfer_cancel)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                mode = FileStatusCode.STATUS_FILE_DOWNLOAD_WAITING;
                                touchChangeProgress = false;
                                ThreadPool.runUi(safeRunnable);
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOADING)
                        .showProgress(true)
                        .rotate(false)
                        .drawable(R.drawable.svg_transfer_cancel)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                mode = FileStatusCode.STATUS_FILE_DOWNLOADING;
                                ThreadPool.runUi(safeRunnable);
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED)
                        .showProgress(false)
                        .drawable(R.drawable.svg_transfer_success)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                mode = FileStatusCode.STATUS_FILE_DOWNLOAD_COMPLETED;
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED)
                        .showProgress(false)
                        .drawable(R.drawable.svg_transfer_upload)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                mode = FileStatusCode.STATUS_FILE_DOWNLOAD_CANCELED;
                            }
                        }),
                new ViewMode(FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED)
                        .rotate(true)
                        .showProgress(false)
                        .drawable(R.drawable.svg_transfer_failed)
                        .stateListener(new StateFetchListener() {
                            @Override
                            public void onFetched(@NonNull String stateTag) {
                                mode = FileStatusCode.STATUS_FILE_DOWNLOAD_FAILED;
                            }
                        })
        );
        loadingView.setCurrentMode(mode);
    }
}
