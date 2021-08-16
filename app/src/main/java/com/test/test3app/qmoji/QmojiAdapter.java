package com.test.test3app.qmoji;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * created by zhaoyuntao
 * on 2020-02-09
 * description:
 */
public class QmojiAdapter extends RecyclerView.Adapter<BlackViewHolder> {

    private List<QmojiItem> qmojiItems;

    public QmojiAdapter() {
        qmojiItems = new ArrayList<>();
    }

    @NotNull
    @Override
    public BlackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qmoji,parent,false);
        return new BlackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull BlackViewHolder holder, int position) {
        QmojiItem qmojiItem = qmojiItems.get(position);
        if (qmojiItem == null) {
            return;
        }
        holder.imageView.setImageResource(qmojiItem.getDrawableId());
        holder.imageView.setTag(qmojiItem.getIndex());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick((Integer) v.getTag());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return qmojiItems.size();
    }

    public void addQmojiItem(int index, int drawableId) {
        QmojiItem qmojiItem = new QmojiItem();
        qmojiItem.setDrawableId(drawableId);
        qmojiItem.setIndex(index);
        qmojiItems.add(qmojiItem);
        notifyDataSetChanged();
    }

    private QmojiSelectView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(QmojiSelectView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void clearQmojiItems() {
        this.qmojiItems.clear();
        notifyDataSetChanged();
    }
}
