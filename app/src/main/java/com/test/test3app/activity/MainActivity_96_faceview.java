package com.test.test3app.activity;

import android.os.Bundle;
import android.view.View;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.faceview.BlueFaceView;
import com.zhaoyuntao.androidutils.tools.T;

public class MainActivity_96_faceview extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_view);

        BlueFaceView blueFaceView = findViewById(R.id.faceView1);
        BlueFaceView blueFaceView2 = findViewById(R.id.faceView2);

        BlueFaceView blueFaceView3 = findViewById(R.id.faceView3);
        BlueFaceView blueFaceView4 = findViewById(R.id.faceView4);
        BlueFaceView blueFaceView6 = findViewById(R.id.faceView6);
        BlueFaceView blueFaceView8 = findViewById(R.id.faceView8);
        blueFaceView3.setShowStatus(true);
        blueFaceView4.setShowStatus(true);
        blueFaceView6.setShowStatus(true);
        blueFaceView8.setShowStatus(true);

        blueFaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.t(activity(), "click blueFaceView");
            }
        });

        blueFaceView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                T.t(activity(), " long click blueFaceView");
                return true;
            }
        });
    }
}
