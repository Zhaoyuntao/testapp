package com.test.test3app;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.zhaoyuntao.androidutils.tools.B;
import com.zhaoyuntao.androidutils.tools.S;

public class MainActivity_inputbar extends AppCompatActivity {

    private LinearLayout linearLayout;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inputbar);
        linearLayout = findViewById(R.id.container);
        editText = findViewById(R.id.edit);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        controlKeyboardLayout(linearLayout, editText);
    }

    private void controlKeyboardLayout(final View root, final View scrollToView) {
        //软键盘弹出来的时候
        final int[] location = new int[2];
        // 获取scrollToView在窗体的坐标
        scrollToView.getLocationInWindow(location);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        final Point point = new Point();
        display.getSize(point);
        int y = point.y;
        S.s("y:" + y);
        // 注册一个回调函数，当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时调用这个回调函数。
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取root在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect);
                        int h_root = root.getRootView().getHeight();
                        int h_bottom = h_root - rect.bottom;
                        int height_inputMethod = B.dip2px(MainActivity_inputbar.this, 150);
                        if (h_bottom > height_inputMethod ) {
                            // 计算root滚动高度，使scrollToView在可见区域的底部
                            int srollHeight = (location[1] + scrollToView
                                    .getHeight()) - rect.bottom;
                            root.scrollTo(0, srollHeight);
                        } else {
                            // 软键盘没有弹出来的时候
                            root.scrollTo(0, 0);
                        }
                    }
                });
    }
}
