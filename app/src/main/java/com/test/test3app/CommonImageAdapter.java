package com.test.test3app;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
public class CommonImageAdapter extends ListAdapter<CommonBean, CommonImageHolder> {
    public CommonImageAdapter() {
        super(new DiffUtil.ItemCallback<CommonBean>() {
            @Override
            public boolean areItemsTheSame(@NonNull CommonBean oldItem, @NonNull CommonBean newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull CommonBean oldItem, @NonNull CommonBean newItem) {
                return TextUtils.equals(oldItem.id, newItem.id);
            }
        });

        List<CommonBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            CommonBean commonBean = new CommonBean();
            commonBean.id = String.valueOf(i);
            commonBean.resId = R.drawable.a3;
            list.add(commonBean);
        }
        submitList(list);
    }

    @NonNull
    @Override
    public CommonImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommonImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_image_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommonImageHolder holder, int position) {
        Glide.with(holder.imageView).load(getCurrentList().get(position).resId).into(holder.imageView);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CommonImageHolder holder) {
        S.s("detached:" + holder.getAdapterPosition() + " " + holder);
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CommonImageHolder holder) {
        S.s("attached:" + holder.getAdapterPosition() + " " + holder);
        super.onViewAttachedToWindow(holder);
    }
}
