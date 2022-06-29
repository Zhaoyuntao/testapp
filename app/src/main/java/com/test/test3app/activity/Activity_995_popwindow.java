package com.test.test3app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import base.ui.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.fastrecordviewnew.UiUtils;
import im.turbo.basetools.utils.InputMethodUtils;
import com.zhaoyuntao.androidutils.tools.B;
import com.zhaoyuntao.androidutils.tools.S;

public class Activity_995_popwindow extends BaseActivity {
    private View button;
    private PopupWindow popupWindowUp;
//    private PopupWindow popupWindowDown;
int[] wh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity995_popwindow);
         wh = B.getScreenWH(activity());

        findViewById(R.id.imageview_popwindow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("click imageview");
                Toast.makeText(activity(),"Clicked imageview",Toast.LENGTH_SHORT).show();
            }
        });


        TextView textViewDown = new TextView(activity());
        textViewDown.setText("Hello");
        textViewDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("click textview");
            }
        });
        textViewDown.setTextSize(UiUtils.dipToPx(30));
        textViewDown.setBackgroundColor(Color.RED);
        textViewDown.setTextColor(Color.WHITE);




//        popupWindowDown = new PopupWindow(textViewUp, wh[0], wh[1], true);
//        popupWindowDown.setBackgroundDrawable(null);
//        popupWindowDown.setAnimationStyle(0);
//        popupWindowDown.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
//        popupWindowDown.setOutsideTouchable(false);

        button = findViewById(R.id.popwindow_buttun);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow(v);
            }
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (!isFinishing()&&!isDestroyed()){
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                    }
//
//                    TP.runOnUi(new SafeRunnable(activity()) {
//                        @Override
//                        protected void runSafely() {
//                            S.s("popupWindow.isShowing():"+popupWindow.isShowing());
//                        }
//                    });
//                }
//            }
//        }).start();
    }

    private void showPopWindow(View v) {
        TextView textViewUp = new TextView(activity());
        textViewUp.setText("Hello");
        textViewUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("click textview");
            }
        });
        textViewUp.setTextSize(UiUtils.dipToPx(30));
        textViewUp.setBackgroundColor(Color.RED);
        textViewUp.setTextColor(Color.WHITE);
        popupWindowUp = new PopupWindow(textViewUp, wh[0], UiUtils.dipToPx(240), true);
        popupWindowUp.setBackgroundDrawable(null);
        popupWindowUp.setAnimationStyle(0);
        popupWindowUp.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        popupWindowUp.setOutsideTouchable(false);


        S.s("popupWindow.isShowing():" + popupWindowUp.isShowing());
        if (popupWindowUp.isShowing()) {
            popupWindowUp.dismiss();
        } else {
//            popupWindow.showAsDropDown(v);//, Gravity.BOTTOM,0,0);
            boolean open = InputMethodUtils.checkKeyBoardOpen(findViewById(R.id.root), new InputMethodUtils.OnKeyBoardOpenListener() {
                @Override
                public void getKeyBoardHeight(int heightOfKeyBoard) {
                    popupWindowUp.setHeight(heightOfKeyBoard);
                    popupWindowUp.showAtLocation(v, Gravity.BOTTOM, 0, 0);//(int) (v.getY()+v.getHeight()));//-(int) (v.getY()+v.getHeight()));
//                    popupWindowDown.setHeight(heightOfKeyBoard);
//                    popupWindowDown.showAtLocation(v, Gravity.BOTTOM, 0, 0);//(int) (v.getY()+v.getHeight()));//-(int) (v.getY()+v.getHeight()));
                }
            });
            if (!open) {
                popupWindowUp.setHeight(UiUtils.dipToPx(240));
                popupWindowUp.showAsDropDown(v);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (popupWindowUp.isShowing()) {
            popupWindowUp.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}