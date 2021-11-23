package com.test.test3app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;
import com.test.test3app.expandrecyclerview.ItemDivider;
import com.test.test3app.recyclerview.CountryCodeAdapter;
import com.test.test3app.recyclerview.CountryCodeBean;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.thread.SafeRunnable;
import com.zhaoyuntao.androidutils.tools.thread.TP;

import java.util.ArrayList;
import java.util.List;

public class MainActivity7_recyclerView extends AppCompatActivity {
    List<CountryCodeBean> mList;
    CountryCodeAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity7_recycler_view);

        //country code
        recyclerView = findViewById(R.id.country_code_list);
        mList = new ArrayList<>();

        Resources resources = getResources();
        if (resources == null) {
            return;
        }
        for (int i = 0; i < 100; i++) {
            CountryCodeBean uae = new CountryCodeBean();
            uae.setCountryCode(971);
            uae.setDrawableId(R.drawable.country_ar);
            uae.setCountryName("UAE:" + i);
            mList.add(uae);
        }


        adapter = new CountryCodeAdapter(mList);
        adapter.setOnItemClickListener(new CountryCodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CountryCodeBean countryCodeBean) {
                TP.runOnUiDelayed(new SafeRunnable(MainActivity7_recyclerView.this) {
                    @Override
                    protected void runSafely() {
                        S.s("start refresh");
                        adapter.notifyItemChanged(0, new Object());
                        adapter.notifyItemChanged(0, new Object());
                    }
                }, 1000);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider(RecyclerView.VERTICAL));

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryCodeBean uae = new CountryCodeBean();
                uae.setCountryCode(971);
                uae.setDrawableId(R.drawable.country_ar);
                uae.setCountryName("UAE:" + System.currentTimeMillis());
                adapter.addData(uae);
            }
        });
    }

    @Override
    public void finish() {
        S.s("finish===>");
//        RecyclerView.RecycledViewPool recycledViewPool= recyclerView.getRecycledViewPool();
//        for(int i=0;i<recycledViewPool.co){
//
//        }
        super.finish();
    }
}
