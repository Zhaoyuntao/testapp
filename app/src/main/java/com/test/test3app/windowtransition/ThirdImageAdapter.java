package com.test.test3app.windowtransition;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.test.test3app.R;

import java.util.List;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
public class ThirdImageAdapter extends ListAdapter<ImageBean, ThirdImageHolder> {
    private OnItemClickListener onItemClickListener;

    public ThirdImageAdapter() {
        super(new DiffUtil.ItemCallback<ImageBean>() {
            @Override
            public boolean areItemsTheSame(@NonNull ImageBean oldItem, @NonNull ImageBean newItem) {
                return oldItem.id.equals(newItem.id);
            }

            @Override
            public boolean areContentsTheSame(@NonNull ImageBean oldItem, @NonNull ImageBean newItem) {
                return TextUtils.equals(oldItem.id, newItem.id);
            }
        });
    }

    @NonNull
    @Override
    public ThirdImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ThirdImageHolder secondImageHolder = new ThirdImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_third_item, parent, false));
        return secondImageHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ThirdImageHolder holder, int position2) {
        int position = holder.getBindingAdapterPosition();
        holder.imageView.setImageResource(getCurrentList().get(position).resId);
        holder.itemView.setTag(getCurrentList().get(position).id);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ThirdImageHolder holder, int position, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}