package com.test.test3app;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
public class CommonAdapter extends ListAdapter<CommonBean, CommonHolder> {
    public CommonAdapter(int count) {
        super(new DiffUtil.ItemCallback<CommonBean>() {
            @Override
            public boolean areItemsTheSame(@NonNull CommonBean oldItem, @NonNull CommonBean newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull CommonBean oldItem, @NonNull CommonBean newItem) {
                return TextUtils.equals(oldItem.content, newItem.content);
            }
        });

        List<CommonBean> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            CommonBean commonBean = new CommonBean();
            commonBean.id = i;
            commonBean.content = "Hello" + i;
            list.add(commonBean);
        }
        submitList(list);
    }

    @NonNull
    @Override
    public CommonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_view2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommonHolder holder, int position) {
        holder.textView.setText(getCurrentList().get(position).content);
    }
}
