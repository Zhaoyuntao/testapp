package com.test.test3app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.a.wam.base.WAMDrawObject;
import com.test.test3app.a.wam.canvas.CanvasView;
import com.test.test3app.a.wam.test.TestObject;
import com.test.test3app.a.wam.test.WorldObject;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.ArrayList;
import java.util.List;


public class MainActivity_9993_wam extends BaseActivity {
    int fMax = 100;
    int gMax = 100;
    int aMax = 360;
    int pMax = 1000;
    float power = 100;
    float angle = 0;
    float percent = 1;

    CanvasView canvasView;
    View oncePush;
    WorldObject worldObject;
    TestObject testObject;
    private TextView textViewPower;
    private TextView textViewAngle;
    private TextView textViewGravity;
    private TextView textViewFriction;
    private TextView textViewElasticity;
    private TextView textViewSlow;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_9993_wam);

        canvasView = findViewById(R.id.canvas);
        canvasView.setSlowPercent(percent);
        oncePush = findViewById(R.id.once_push);
        textViewPower = findViewById(R.id.progress_touch_power);
        textViewAngle = findViewById(R.id.progress_touch_angle);
        textViewGravity = findViewById(R.id.progress_touch_g);
        textViewFriction = findViewById(R.id.progress_touch_f);
        textViewElasticity = findViewById(R.id.progress_touch_e);
        textViewSlow = findViewById(R.id.progress_touch_slow);

        worldObject = new WorldObject("TestWorld");
        worldObject.g = gMax / 2f;
        worldObject.f = fMax / 2f;
        List<WAMDrawObject> objects = new ArrayList<>();
        testObject = new TestObject("TestObject");
        testObject.setE(0.5f);
        objects.add(testObject);
        worldObject.setObjects(objects);
        canvasView.setWorldObject(worldObject);


        showMessage();

        textViewPower.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float percent = event.getX() / v.getWidth();
                power = pMax * percent;
                showMessage();
                return true;
            }
        });
        textViewAngle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float percent = event.getX() / v.getWidth();
                angle = aMax * percent;
                showMessage();
                return true;
            }
        });
        textViewFriction.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float percent = event.getX() / v.getWidth();
                worldObject.f = fMax * percent;
                showMessage();
                return true;
            }
        });
        textViewGravity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float percent = event.getX() / v.getWidth();
                worldObject.g = gMax * percent;
                showMessage();
                return true;
            }
        });
        textViewElasticity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float percent = event.getX() / v.getWidth();
                testObject.setE(percent);
                showMessage();
                return true;
            }
        });
        textViewSlow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                percent = event.getX() / v.getWidth();
                canvasView.setSlowPercent(percent);
                showMessage();
                return true;
            }
        });

        oncePush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldObject.power = power;
                worldObject.angle = angle;
                worldObject.powerTime = 0.3f;
            }
        });
    }

    private void showMessage() {
        textViewFriction.setText("friction:" + S.formatNumber(worldObject.f, "#.#"));
        textViewGravity.setText("gravity:" + S.formatNumber(worldObject.g, "#.#"));
        textViewPower.setText("power:" + S.formatNumber(power, "#.#"));
        textViewAngle.setText("angle:" + S.formatNumber(angle, "#.#"));
        textViewSlow.setText("slow:" + S.formatNumber(percent, "#.#"));
        textViewElasticity.setText("Elasticity:" + S.formatNumber(testObject.getE(), "#.#"));
    }
}
