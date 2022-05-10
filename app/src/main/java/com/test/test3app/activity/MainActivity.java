package com.test.test3app.activity;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.a.ZRouter;
import com.test.test3app.fastrecordviewnew.UiUtils;
import com.test.test3app.textview.AutoSizeTextView;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        S.s("onCreate");
        setContentView(R.layout.activity_main);
        List<ZRouter> zRouters = new ArrayList<>();

        zRouters.add(new ZRouter("bubble", MainActivity1_unknown.class));
        zRouters.add(new ZRouter("record", MainActivity2_record.class));
        zRouters.add(new ZRouter("sms", MainActivity3_sms.class));
        zRouters.add(new ZRouter("group tips", MainActivity4_grouptips.class));
        zRouters.add(new ZRouter("take photo", MainActivity5_takephoto.class));
        zRouters.add(new ZRouter("pay", MainActivity6_pay.class));
        zRouters.add(new ZRouter("recyclerView", MainActivity7_ListView.class));
        zRouters.add(new ZRouter("permission", MainActivity8_permission.class));
        zRouters.add(new ZRouter("openGL", MainActivity9_openGL.class));
        zRouters.add(new ZRouter("Qmoji", MainActivity91_Qmoji.class));
        zRouters.add(new ZRouter("huawei meeting", MainActivity92_huawei_meeting.class));
        zRouters.add(new ZRouter("threadPool", MainActivity93_threadPool.class));
        zRouters.add(new ZRouter("textview", MainActivity_95_textview.class));
        zRouters.add(new ZRouter("faceView", MainActivity_96_faceview.class));
        zRouters.add(new ZRouter("spannable", MainActivity_97_textview_spannable.class));
        zRouters.add(new ZRouter("stickerReply", MainActivity_98_sticker_reply.class));
        zRouters.add(new ZRouter("dark mode", MainActivity_99_dark_mode.class));
        zRouters.add(new ZRouter("ndk", MainActivity_990_cell.class));
        zRouters.add(new ZRouter("calculate", MainActivity_991_calculate.class));
        zRouters.add(new ZRouter("drawer", MainActivity_992_drawerLayout.class));
        zRouters.add(new ZRouter("pull down", MainActivity_993_pulldown.class));
        zRouters.add(new ZRouter("gallery view", MainActivity_994_gallery_pager.class));
        zRouters.add(new ZRouter("popWindow", MainActivity_995_popwindow.class));
        zRouters.add(new ZRouter("expand", MainActivity_996_expand.class));
        zRouters.add(new ZRouter("CameraWallpaper", MainActivity_997_cameraWallpaper.class));
        zRouters.add(new ZRouter("Nest", MainActivity_998_nest.class));
        zRouters.add(new ZRouter("wallpaper", MainActivity_94_wallpaper.class));
        zRouters.add(new ZRouter("Loading", MainActivity_999_loading.class));
        zRouters.add(new ZRouter("AnimateImage", MainActivity_9991_animateimageview.class));

        init(zRouters);
    }

    private void init(List<ZRouter> zRouters) {
        GridLayout gridLayout = findViewById(R.id.grid_main);
        int margin = UiUtils.dipToPx(5);
        for (ZRouter z : zRouters) {
            AutoSizeTextView button = new AutoSizeTextView(activity());
            button.setText(z.getName());
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
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToActivity(z.getActivity());
                }
            });
            gridLayout.addView(button);

        }
        ZRouter zRouter = zRouters.get(zRouters.size() - 1);
        goToActivity(zRouter.getActivity());
    }
}