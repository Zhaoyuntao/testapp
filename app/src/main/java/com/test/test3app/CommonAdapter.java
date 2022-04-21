package com.test.test3app;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.test.test3app.wallpaper.DiffHelper;
import com.test.test3app.wallpaper.ListItemSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
public class CommonAdapter extends ListAdapter<CommonBean, CommonHolder> {
    private OnItemClickListener onItemClickListener;
    private ListItemSelector<CommonBean> selector = new ListItemSelector<>();

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

        initData(count);
    }

    public void initData(int count) {
        List<CommonBean> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            CommonBean commonBean = new CommonBean();
            commonBean.id = String.valueOf(i);
            list.add(commonBean);
        }
        submitList(list);
    }

    @Override
    public void submitList(List<CommonBean> list) {
        this.selector.setData(list);
        super.submitList(list);
    }

    private int index;

    @NonNull
    @Override
    public CommonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        S.s("onCreateViewHolder:" + index++);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new CommonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommonHolder holder, int position2) {
        int position = holder.getBindingAdapterPosition();
//        S.s("onBindViewHolder:" + position);
        CommonBean commonBean = getCurrentList().get(position);
        holder.itemView.setTag("position:" + position + " " + commonBean.id);
        holder.textView.setText(commonBean.id);
        holder.textView.setBackgroundColor((selector.isSelected(commonBean)) ? Color.parseColor("#dd880000") : Color.parseColor("#80000000"));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, commonBean, position);
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

    public void selectItem(CommonBean commonBean, int position) {
        boolean selected = selector.changeSelect(commonBean);
        notifyItemChanged(position, DiffHelper.getPayload("", selected));
    }

    public interface OnItemClickListener {
        void onItemClick(View view, CommonBean commonBean, int position);
    }
}
