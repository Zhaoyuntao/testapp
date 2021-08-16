package com.test.test3app.expandrecyclerview;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * created by zhaoyuntao
 * on 2020-03-17
 * description:
 */
public abstract class ExpandableViewHolder extends RecyclerView.ViewHolder {
    private ExpandListener expandListener;

    public ExpandableViewHolder(final View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ExpandableViewHolder.this.onClick(v)) {
                    return;
                }
                changeExpandState(getLayoutPosition());
            }
        });
    }

    final public void initExpandState(int position) {
        if (!expandListener.isExpandable(position)) {
            return;
        }
        if (expandListener.isExpand(position)) {
            onExpand();
        } else {
            onShrink();
        }
    }

    final public void changeExpandState(int position) {
        if (!expandListener.isExpandable(position)) {
            return;
        }
        if (expandListener.isExpand(position)) {
            onShrink();
            expandListener.shrink(position);
        } else {
            onExpand();
            expandListener.expand(position);
        }
    }

    public boolean onClick(View v) {
        return false;
    }

    protected void onExpand() {
    }

    protected void onShrink() {
    }


    final public void setExpandListener(ExpandListener expandListener) {
        this.expandListener = expandListener;
    }

    public interface ExpandListener {
        void expand(int position);

        void shrink(int position);

        boolean isExpand(int position);

        boolean isExpandable(int position);
    }
}