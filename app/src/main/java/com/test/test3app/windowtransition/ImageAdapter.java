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
public class ImageAdapter extends ListAdapter<ImageBean, ImageHolder> {
    private OnItemClickListener onItemClickListener;

    public ImageAdapter() {
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
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_window_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position2) {
        int position = holder.getBindingAdapterPosition();
//        View[] views = new View[]{holder.imageView0, holder.imageView1, holder.imageView2, holder.imageView3};
        holder.imageView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, 0);
                }
            }
        });
//        holder.imageView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onItemClick(v, views, 1);
//                }
//            }
//        });
//        holder.imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onItemClick(v, views, 2);
//                }
//            }
//        });
//        holder.imageView3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onItemClick(v, views, 3);
//                }
//            }
//        });
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
