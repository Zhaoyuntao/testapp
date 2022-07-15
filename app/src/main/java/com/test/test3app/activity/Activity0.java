package com.test.test3app.activity;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;

import com.test.test3app.R;
import com.test.test3app.appbase.ZRouter;
import com.test.test3app.fastrecordviewnew.UiUtils;

import java.util.ArrayList;
import java.util.List;

import base.ui.BaseActivity;
import im.thebot.chat.ui.ChatActivity;
import im.thebot.common.ui.chat.AutoSizeTextView;


public class Activity0 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ZRouter> zRouters = new ArrayList<>();
        zRouters.add(new ZRouter("record", Activity_2_record.class));
        zRouters.add(new ZRouter("sms", Activity_3_sms.class));
        zRouters.add(new ZRouter("group tips", Activity_4_grouptips.class));
        zRouters.add(new ZRouter("take photo", Activity_5_takephoto.class));
        zRouters.add(new ZRouter("pay", Activity_6_pay.class));
        zRouters.add(new ZRouter("recyclerView", Activity_7_ListView.class));
        zRouters.add(new ZRouter("openGL", Activity_9_openGL.class));
        zRouters.add(new ZRouter("Qmoji", Activity_91_Qmoji.class));
        zRouters.add(new ZRouter("huawei meeting", Activity_92_huawei_meeting.class));
        zRouters.add(new ZRouter("threadPool", Activity_93_threadPool.class));
        zRouters.add(new ZRouter("textview", Activity_95_textview.class));
        zRouters.add(new ZRouter("faceView", Activity_96_faceview.class));
        zRouters.add(new ZRouter("spannable", Activity_97_textview_spannable.class));
        zRouters.add(new ZRouter("stickerReply", Activity_98_sticker_reply.class));
        zRouters.add(new ZRouter("calculate", Activity_991_calculate.class));
        zRouters.add(new ZRouter("drawer", Activity_992_drawerLayout.class));
        zRouters.add(new ZRouter("gallery view", Activity_994_gallery_pager.class));
        zRouters.add(new ZRouter("popWindow", Activity_995_popwindow.class));
        zRouters.add(new ZRouter("expand", Activity_996_expand.class));
        zRouters.add(new ZRouter("CameraWallpaper", Activity_997_cameraWallpaper.class));
        zRouters.add(new ZRouter("Nest", Activity_998_nest.class));
        zRouters.add(new ZRouter("AnimateImage", Activity_9991_animateimageview.class));
        zRouters.add(new ZRouter("window transition", Activity_9992_windowContentTransition.class));
        zRouters.add(new ZRouter("wam", Activity_9993_wam.class));
        zRouters.add(new ZRouter("Loading", Activity_999_loading.class));
        zRouters.add(new ZRouter("mention", Activity_9995_mention.class));
        zRouters.add(new ZRouter("slide", Activity_993_slide.class));
        zRouters.add(new ZRouter("bubble", Activity_9996_bubble.class));
        zRouters.add(new ZRouter("cell", Activity_990_cell.class));
        zRouters.add(new ZRouter("dark mode", Activity_99_dark_mode.class));
        zRouters.add(new ZRouter("Dir", Activity_9997_dir.class));
        zRouters.add(new ZRouter("NewPermission", Activity_9994_permission.class));
        zRouters.add(new ZRouter("smooth", Activity_8_smoothSwitch.class));
        zRouters.add(new ZRouter("wallpaper", Activity_94_wallpaper.class));
        zRouters.add(new ZRouter("Event", ChatActivity.class));

        init(zRouters, savedInstanceState == null);
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
}