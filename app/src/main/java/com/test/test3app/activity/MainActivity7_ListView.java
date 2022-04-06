package com.test.test3app.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.test.test3app.R;
import com.test.test3app.fastrecordviewnew.UiUtils;
import com.test.test3app.recyclerview.CountryCodeAdapter;
import com.test.test3app.recyclerview.CountryCodeBean;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.T;

import java.util.ArrayList;
import java.util.List;

public class MainActivity7_ListView extends AppCompatActivity {
    List<CountryCodeBean> mList;
    CountryCodeAdapter adapter;
    ListView listview;
    BaseAdapter adapter2;
    List<String> list2;
    int index = 0;
    TextView modeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity7_recycler_view);

        //country code
        listview = findViewById(R.id.country_code_list);
        listview.setStackFromBottom(true);
        list2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list2.add(String.valueOf(index++));
        }
        adapter2 = new BaseAdapter() {
            @Override
            public int getCount() {
                return list2.size();
            }

            @Override
            public Object getItem(int position) {
                return list2.get(position);//list2.get(list2.size()-1-position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
//                position=getCount()-1-position;
                TextView textView = new TextView(MainActivity7_ListView.this);
                textView.setTextSize(UiUtils.dipToPx(50));
                textView.setGravity(Gravity.CENTER);
                textView.setText(getItem(position).toString());
                S.s("position:"+position+" content:"+getItem(position).toString());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list2.remove(position);
                        notifyDataSetChanged();
                    }
                });
                return textView;
            }
        };
        modeView = findViewById(R.id.mode);

        listview.setAdapter(adapter2);
        listview.setSelection(0);
        T.t(this, "mode:" + listview.getTranscriptMode());
        initModeView();
        findViewById(R.id.add_at_tail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list2.add(String.valueOf(index++));
                adapter2.notifyDataSetChanged();
                S.s("add at " + (list2.size() - 1) + ":" + index);
            }
        });
        findViewById(R.id.add_at_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list2.add(0, String.valueOf(index++));
                adapter2.notifyDataSetChanged();
                S.s("add at 0:" + index);
                listview.setSelection(1);
            }
        });
        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list2.remove(list2.size() - 1);
                adapter2.notifyDataSetChanged();
                S.s("remove :" + index);
            }
        });
        findViewById(R.id.disable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
                initModeView();
                S.s("disable");
            }
        });
        findViewById(R.id.always_scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                initModeView();
                S.s("always_scroll");
            }
        });
        findViewById(R.id.normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
                initModeView();
                S.s("normal");
            }
        });
    }

    private void initModeView() {
        if (listview.getTranscriptMode() == AbsListView.TRANSCRIPT_MODE_NORMAL) {
            modeView.setText("normal");
        } else if (listview.getTranscriptMode() == AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL) {
            modeView.setText("always");
        } else if (listview.getTranscriptMode() == AbsListView.TRANSCRIPT_MODE_DISABLED) {
            modeView.setText("disable");
        } else {
            modeView.setText("?");
        }
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
