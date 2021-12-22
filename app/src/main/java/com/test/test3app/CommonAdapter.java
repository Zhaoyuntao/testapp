package com.test.test3app;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.zhaoyuntao.androidutils.tools.S;

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

    private int index;

    @NonNull
    @Override
    public CommonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        S.s("onCreateViewHolder:" + index++);
        return new CommonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommonHolder holder, int position) {
//        S.s("onBindViewHolder:" + position);
        holder.itemView.setTag("position:"+position+" "+getCurrentList().get(position).content);
        holder.textView.setText(getCurrentList().get(position).content);
    }
}
