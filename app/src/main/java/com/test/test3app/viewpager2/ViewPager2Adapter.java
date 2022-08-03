package com.test.test3app.viewpager2;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.test.test3app.R;
import com.test.test3app.wallpaper.DiffHelper;

import java.util.List;

import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 03/08/2022
 * description:
 */
public class ViewPager2Adapter extends ListAdapter<ImageModel, ViewPager2Holder> {

    public ViewPager2Adapter() {
        super(new DiffUtil.ItemCallback<ImageModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull ImageModel oldItem, @NonNull ImageModel newItem) {
                return TextUtils.equals(oldItem.id, newItem.id);
            }

            @Override
            public boolean areContentsTheSame(@NonNull ImageModel oldItem, @NonNull ImageModel newItem) {
                return false;
            }

            @Nullable
            @Override
            public Object getChangePayload(@NonNull ImageModel oldItem, @NonNull ImageModel newItem) {
                return DiffHelper.getPayload("Hello", false);
            }
        });
    }

    @NonNull
    @Override
    public ViewPager2Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPager2Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewpager2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPager2Holder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.image_view_view_pager2);
        imageView.setImageDrawable(null);
        ThreadPool.runUiDelayed(200, new SafeRunnable(holder.itemView) {
            @Override
            protected void runSafely() {
                imageView.setImageResource(R.drawable.wallpaper2);
            }
        });

        TextView textView = holder.itemView.findViewById(R.id.text_view_view_pager2);
        textView.setText(getCurrentList().get(position).id);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPager2Holder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() > 0) {
            S.s("[" + position + "][id:" + getCurrentList().get(position).id + "] onBindViewHolder 2");
        } else {
            S.s("[" + position + "][id:" + getCurrentList().get(position).id + "] onBindViewHolder 1");
            onBindViewHolder(holder, position);
        }
    }
}
