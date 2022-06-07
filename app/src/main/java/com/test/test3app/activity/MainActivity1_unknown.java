package com.test.test3app.activity;


import android.os.Bundle;
import android.view.View;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.S;

public class MainActivity1_unknown extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("clicked   View    !!!!!>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        });
        findViewById(R.id.view).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                S.s("long clicked  View     !!!!!>>>>>>>>>>>>>>>>>>>>>>>>");
                return true;
            }
        });
        findViewById(R.id.viewgroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("clicked ViewGroup      !!!!!>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        });
        findViewById(R.id.viewgroup).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                S.s("long clicked  ViewGroup  !!!! >>>>>>>>>>>>>>>>>>>>>>>");
                return true;
            }
        });
    }

}
