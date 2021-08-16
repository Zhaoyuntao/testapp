package com.test.test3app.expandrecyclerview;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 2020-03-16
 * description:
 */
public abstract class ExpandableRecyclerAdapter<A extends ExpandableViewHolder, B> extends RecyclerView.Adapter<A> {

    private List<ExpandableEntry<B>> expandableEntries = new ArrayList<>();

    final public void addData(List<ExpandableEntry> expandableEntries) {
        this.expandableEntries.clear();
        for (ExpandableEntry expandableEntry : expandableEntries) {
            this.expandableEntries.addAll(expandableEntry.getAll());
        }
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    final public A onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        final A expandableViewHolder = onCreateExpandableViewHolder(parent, viewType);
        expandableViewHolder.setExpandListener(new ExpandableViewHolder.ExpandListener() {
            @Override
            public void expand(int position) {
                ExpandableRecyclerAdapter.this.expand(position);
            }

            @Override
            public void shrink(int position) {
                ExpandableRecyclerAdapter.this.shrink(position);
            }

            @Override
            public boolean isExpand(int position) {
                ExpandableEntry expandableEntry = expandableEntries.get(position);
                return expandableEntry != null && expandableEntry.isParent() && expandableEntry.isExpand();
            }

            @Override
            public boolean isExpandable(int position) {
                ExpandableEntry expandableEntry = expandableEntries.get(position);
                return expandableEntry != null && expandableEntry.isParent() && expandableEntry.isExpandable();
            }
        });
        return expandableViewHolder;
    }

    /**
     * this is your costume holder ,this holder must extends {@link ExpandableViewHolder}
     *
     * @return
     */
    public abstract A onCreateExpandableViewHolder(@NotNull ViewGroup parent, int viewType);

    @Override
    final public void onBindViewHolder(@NotNull A holder, int position) {
        ExpandableEntry<B> expandableEntry = expandableEntries.get(position);
        if (expandableEntry == null) {
            return;
        }
        holder.initExpandState(position);
        B b = expandableEntry.getParamEntry();
        if (b != null) {
            onBindExpandableViewHolder(holder, position, b);
        }
    }

    @Override
    final public int getItemViewType(int position) {
        ExpandableEntry<B> expandableEntry = expandableEntries.get(position);
        if (expandableEntry != null) {
            B b = expandableEntry.getParamEntry();
            if (b != null) {
                return getExpandableItemViewType(b);
            }
        }
        return -1;
    }

    public abstract int getExpandableItemViewType(B b);

    /**
     * you need to overwrite this method
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindExpandableViewHolder(A holder, int position, B b);

    @Override
    final public int getItemCount() {
        return expandableEntries.size();
    }

    /**
     * expand the layout,add all child of this click item to the expandableEntries
     *
     * @param position the position of the layout
     */
    private void expand(int position) {
        int subViewPosition = position + 1;
        ExpandableEntry expandableEntry = expandableEntries.get(position);
        if (!expandableEntry.isExpandable()) {
            return;
        }
        for (int i = 0; i < expandableEntry.size(); i++) {
            expandableEntries.add(subViewPosition + i, expandableEntry.getChildAt(i));
            notifyItemInserted(subViewPosition + i);
        }
        expandableEntry.setExpand(true);
    }

    /**
     * shrink the layout,and remove all child of this click item from expandableEntries
     *
     * @param position the position of the layout
     */
    private void shrink(int position) {
        int subViewPosition = position + 1;
        ExpandableEntry expandableEntry = expandableEntries.get(position);
        if (!expandableEntry.isExpandable()) {
            return;
        }
        for (int i = expandableEntry.size() - 1; i >= 0; i--) {
            expandableEntries.remove(subViewPosition + i);
            notifyItemRemoved(subViewPosition + i);
        }
        expandableEntry.setExpand(false);
    }


}
