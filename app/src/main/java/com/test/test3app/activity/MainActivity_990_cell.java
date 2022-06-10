package com.test.test3app.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.textview.SlideBubbleAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_990_cell extends BaseActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_990_cell);

        recyclerView = findViewById(R.id.recycler_view_slide);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity()));

        List<String>a=new ArrayList<>();
        a.add("a");
        a.add("aa");
        a.add("aaa");
        a.add("aaaa");
        a.add("aaaaa");
        a.add("aaaaasadasjndjkajkasjkdsajkdnkjasndkjasndkjsa");
        a.add("aaaaaa");
        a.add("aaaaaaa");
        a.add("aaaaaaaa");
        a.add("aaaaaaaaa");
        a.add("aaaaaaaaaa");
        a.add("aaaaaaaaaaa");
        a.add("file");
        recyclerView.setAdapter(new SlideBubbleAdapter(a));

    }
}