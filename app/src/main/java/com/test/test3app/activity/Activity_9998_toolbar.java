package com.test.test3app.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.test.test3app.R;

import base.ui.BaseActivity;
import im.turbo.baseui.toast.ToastUtil;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.log.S;

public class Activity_9998_toolbar extends BaseActivity {
    private boolean show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_9998_toolbar);
        setTitle("Hello");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(true);
        findViewById(R.id.button_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show = !show;
                updateToolBarMenu();
            }
        });
    }

    @Override
    protected int getCustomToolbarLayoutRes() {
        return R.layout.test_custom_toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        S.s(getClass().getSimpleName() + ":onCreateOptionsMenu");
        MenuItem menuNight = menu.add(10, 11, 0, "Night");
        menuNight.setIcon(R.drawable.svg_chat_audio_record_draft_play);
        menuNight.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ToastUtil.show(item.getTitle());
                changeTheme(true);
                return true;
            }
        });
        MenuItem menuDay = menu.add(10, 11, 1, "Day");
        menuDay.setIcon(R.drawable.svg_chat_audio_record_draft_pause);
        menuDay.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ToastUtil.show(item.getTitle());
                changeTheme(false);
                return true;
            }
        });


        SubMenu menuSub = menu.addSubMenu(10, 11, 2, "More");
        menuSub.clearHeader();
        menuSub.setIcon(R.drawable.svg_chat_close_grey);

        MenuItem menuHide = menuSub.add(10, 11, 3, "Hide");
        menuHide.setIcon(R.drawable.svg_chat_close_grey);
        menuHide.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (getSupportActionBar().isShowing()) {
                    getSupportActionBar().hide();
                    ThreadPool.runUiDelayed(1000, new SafeRunnable(activity()) {
                        @Override
                        protected void runSafely() {
                            getSupportActionBar().show();
                        }
                    });
                }
                return true;
            }
        });

        if (show) {
            MenuItem menuText = menu.add(10, 11, 10, "Text");
            menuText.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        MenuItem menuAudio = menu.add(10, 11, 11, "Audio");
        menuAudio.setIcon(R.drawable.svg_chat_menu_call_audio);
        menuAudio.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menuAudio.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ToastUtil.show(item.getTitle());
                return true;
            }
        });

        MenuItem menuVideo = menu.add(10, 11, 12, "Video");
        menuVideo.setIcon(R.drawable.svg_chat_menu_call_video);
        menuVideo.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menuVideo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ToastUtil.show(item.getTitle());
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
