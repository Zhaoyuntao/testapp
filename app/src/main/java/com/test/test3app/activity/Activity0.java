package com.test.test3app.activity;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.test.test3app.R;

import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionResult;
import im.turbo.baseui.permission.PermissionUtils;
import im.turbo.baseui.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import base.ui.BaseActivity;
import im.thebot.chat.ui.ChatActivity;
import base.ui.AutoSizeTextView;
import im.turbo.utils.log.S;


public class Activity0 extends BaseActivity {
    public static final boolean logOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
//                S.s(logOpen, "onStateChanged: event:" + event);
            }
        });
        S.s(logOpen, "onCreate");
        List<ZRouter> zRouters = new ArrayList<>();
        zRouters.add(new ZRouter("sms", Activity_3_sms.class));
        zRouters.add(new ZRouter("group tips", Activity_4_grouptips.class));
        zRouters.add(new ZRouter("take photo", Activity_5_takephoto.class));
        zRouters.add(new ZRouter("pay", Activity_6_pay.class));
        zRouters.add(new ZRouter("recyclerView", Activity_7_ListView.class));
        zRouters.add(new ZRouter("openGL", Activity_9_openGL.class));
        zRouters.add(new ZRouter("Qmoji", Activity_91_Qmoji.class));
        zRouters.add(new ZRouter("huawei meeting", Activity_92_huawei_meeting.class));
        zRouters.add(new ZRouter("threadPool", Activity_93_threadPool.class));
        zRouters.add(new ZRouter("faceView", Activity_96_faceview.class));
        zRouters.add(new ZRouter("spannable", Activity_97_textview_spannable.class));
        zRouters.add(new ZRouter("stickerReply", Activity_98_sticker_reply.class));
        zRouters.add(new ZRouter("calculate", Activity_991_calculate.class));
        zRouters.add(new ZRouter("drawer", Activity_992_drawerLayout.class));
        zRouters.add(new ZRouter("gallery view", Activity_994_gallery_pager.class));
        zRouters.add(new ZRouter("popWindow", Activity_995_popwindow.class));
        zRouters.add(new ZRouter("expand", Activity_996_expand.class));
        zRouters.add(new ZRouter("CameraWallpaper", Activity_997_cameraWallpaper.class));
        zRouters.add(new ZRouter("AnimateImage", Activity_9991_animateimageview.class));
        zRouters.add(new ZRouter("wam", Activity_9993_wam.class));
        zRouters.add(new ZRouter("mention", Activity_9995_mention.class));
        zRouters.add(new ZRouter("slide", Activity_993_slide.class));
        zRouters.add(new ZRouter("bubble", Activity_9996_bubble.class));
        zRouters.add(new ZRouter("cell", Activity_990_cell.class));
        zRouters.add(new ZRouter("dark mode", Activity_99_dark_mode.class));
        zRouters.add(new ZRouter("wallpaper", Activity_94_wallpaper.class));
        zRouters.add(new ZRouter("toolbar", Activity_9998_toolbar.class));
        zRouters.add(new ZRouter("viewpager2", Activity_9999_viewpager2.class));
        zRouters.add(new ZRouter("gesture", Activity_99990_gesture.class));
        zRouters.add(new ZRouter("smooth", Activity_8_smoothSwitch.class));
        zRouters.add(new ZRouter("NewPermission", Activity_9994_permission.class));
        zRouters.add(new ZRouter("Event", ChatActivity.class));
        zRouters.add(new ZRouter("window transition", Activity_9992_windowContentTransition.class));
        zRouters.add(new ZRouter("blur", Activity_99991_Blur.class));
        zRouters.add(new ZRouter("keyboard", Activity_99992_keyboard.class));
        zRouters.add(new ZRouter("Loading", Activity_999_loading.class));
        zRouters.add(new ZRouter("imageview", Activity_95_imageview.class));
        zRouters.add(new ZRouter("roundFrame", Activity_99993_roundFrameLayout.class));
        zRouters.add(new ZRouter("url", Activity_99995_webURL.class));
        zRouters.add(new ZRouter("Dir", Activity_9997_dir.class));
        zRouters.add(new ZRouter("Nest", Activity_998_reflection.class));
        zRouters.add(new ZRouter("textview", Activity_95_textview.class));
        zRouters.add(new ZRouter("sql", Activity_99994_Sql.class));
        zRouters.add(new ZRouter("notification", Activity_2_notification.class));

        init(zRouters, savedInstanceState == null);

        PermissionUtils.requestPermission(activity(), new PermissionResult() {
            @Override
            public void onGranted(@NonNull String[] grantedPermissions) {

            }
        }, Permission.READ_PHONE_STATE);
    }

    private void init(List<ZRouter> zRouters, boolean jump) {
        GridLayout gridLayout = findViewById(R.id.grid_main);
        int margin = UiUtils.dipToPx(5);
        for (ZRouter z : zRouters) {
            AutoSizeTextView button = new AutoSizeTextView(activity());
            button.setText(z.getName());
            button.setCornerRadius(UiUtils.dipToPx(5));
            button.setPaddingRelative(UiUtils.dipToPx(10), 0, UiUtils.dipToPx(10), 0);
            button.setPadding(0, 0, 0, 0);
            button.setGravity(Gravity.CENTER);
            button.setBackgroundColor(Color.RED);
            button.setTextColor(Color.WHITE);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = 0;
            layoutParams.height = WRAP_CONTENT;
            layoutParams.setMarginEnd(margin);
            layoutParams.setMarginStart(margin);
            layoutParams.topMargin = margin;
            layoutParams.bottomMargin = margin;
            layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1.0f);
            layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1.0f);
            button.setLayoutParams(layoutParams);
            button.setTextSize(20);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToActivity(z.getActivity());
                }
            });
            gridLayout.addView(button);

        }
        if (jump) {
            ZRouter zRouter = zRouters.get(zRouters.size() - 1);
            goToActivity(zRouter.getActivity());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        S.s(logOpen, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        S.s(logOpen, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        S.s(logOpen, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        S.s(logOpen, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        S.s(logOpen, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        S.s(logOpen, "onDestroy");
    }
}