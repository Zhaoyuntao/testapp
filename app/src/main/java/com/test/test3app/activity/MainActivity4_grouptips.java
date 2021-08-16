package com.test.test3app.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity4_grouptips extends BaseActivity {

    ListView listView;
    ListView listView2;
    Button button;
    Button button2;
    boolean show;
    int height = -1;
    int height2 = -1;
    FrameLayout frameLayout;
    FrameLayout frameLayout2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity4_grouptips);

        listView = findViewById(R.id.listview);
        listView2 = findViewById(R.id.listview2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                listView.setVisibility(View.GONE);
            }
        }).start();

        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }
        listView.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(activity());
                textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setText(list.get(position));
                return textView;
            }
        });
        listView2.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(activity());
                textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setText(list.get(position));
                return textView;
            }
        });

        frameLayout=findViewById(R.id.container);
        frameLayout2=findViewById(R.id.container2);

        final ValueAnimator a = ValueAnimator.ofFloat(0, 1000);
        a.setDuration(400);
        a.setInterpolator(new DecelerateInterpolator());
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                float percent = distance / 1000;
                ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
                ViewGroup.LayoutParams layoutParams2 = frameLayout2.getLayoutParams();
                if (height < 0) {
                    height = layoutParams.height;
                }
                if (height2 < 0) {
                    height2 = layoutParams2.height;
                }

                layoutParams.height = (int) (height * percent);
                layoutParams2.height = (int) (height2 * percent);
                frameLayout.setLayoutParams(layoutParams);
                frameLayout2.setLayoutParams(layoutParams2);
            }
        });
        final ValueAnimator b = ValueAnimator.ofFloat(1000, 0);
        b.setDuration(400);
        b.setInterpolator(new AccelerateInterpolator());
        b.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                float percent = distance / 1000;
                ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
                ViewGroup.LayoutParams layoutParams2 = frameLayout2.getLayoutParams();
                if (height < 0) {
                    height = layoutParams.height;
                }
                if (height2 < 0) {
                    height2 = layoutParams2.height;
                }
                layoutParams.height = (int) (height * percent);
                layoutParams2.height = (int) (height2 * percent);
                frameLayout.setLayoutParams(layoutParams);
                frameLayout2.setLayoutParams(layoutParams2);
            }
        });

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show) {
                    show = false;
                    a.start();
                } else {
                    show = true;
                    b.start();
                }
            }
        });
    }


}
