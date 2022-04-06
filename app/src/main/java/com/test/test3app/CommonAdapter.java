package com.test.test3app;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
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
    private OnItemClickListener onItemClickListener;
    private SelectionTracker<String> selectionTracker;

    public CommonAdapter(int count) {
        super(new DiffUtil.ItemCallback<CommonBean>() {
            @Override
            public boolean areItemsTheSame(@NonNull CommonBean oldItem, @NonNull CommonBean newItem) {
                return oldItem.id.equals(newItem.id);
            }

            @Override
            public boolean areContentsTheSame(@NonNull CommonBean oldItem, @NonNull CommonBean newItem) {
                return TextUtils.equals(oldItem.id, newItem.id);
            }
        });

        List<CommonBean> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            CommonBean commonBean = new CommonBean();
            commonBean.id = String.valueOf(i);
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
        CommonBean commonBean = getCurrentList().get(position);
        holder.itemView.setTag("position:" + position + " " + commonBean.id);
        holder.textView.setText(commonBean.id);
        holder.textView.setBackgroundColor((selectionTracker != null && selectionTracker.isSelected(commonBean.id)) ? Color.RED : Color.GREEN);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, commonBean);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull CommonHolder holder, int position, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setSelectionTracker(SelectionTracker<String> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    private CommonBean tmp;

    public void increase() {
        if (tmp != null) {
            List<CommonBean> newList = new ArrayList<>(getCurrentList());
            newList.add(tmp);
            submitList(newList);
            tmp = null;
        }
    }

    public void reduce() {
        if (tmp == null) {
            List<CommonBean> newList = new ArrayList<>(getCurrentList());
            tmp = newList.remove(newList.size() - 1);
            submitList(newList);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, CommonBean commonBean);
    }
}
