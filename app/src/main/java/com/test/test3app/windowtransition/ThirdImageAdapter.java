package com.test.test3app.windowtransition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.test.test3app.R;
import com.test.test3app.wallpaper.DiffHelper;

import java.util.List;

import im.turbo.utils.log.S;

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
                return false;
            }

            @Nullable
            @Override
            public Object getChangePayload(@NonNull ImageBean oldItem, @NonNull ImageBean newItem) {
                return DiffHelper.getPayload("hello", "hello");
            }
        });
    }

    @NonNull
    @Override
    public ThirdImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ThirdImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_third_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ThirdImageHolder holder, int position2) {
//        S.s("onBindViewHolder no payload ["+ holder.getBindingAdapterPosition()+"]");
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
        if (payloads.size() > 0) {
//            S.s("onBindViewHolder has payload [" + holder.getBindingAdapterPosition() + "]:[" + getCurrentList().get(holder.getBindingAdapterPosition()).id + "]");
        } else {
//            S.s("onBindViewHolder no payload [" + holder.getBindingAdapterPosition() + "]:[" + getCurrentList().get(holder.getBindingAdapterPosition()).id + "]");
            onBindViewHolder(holder, position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
