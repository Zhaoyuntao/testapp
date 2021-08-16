package com.test.test3app.huaweimeeting.conference.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.test3app.R;
import com.test.test3app.expandrecyclerview.ExpandableRecyclerAdapter;
import com.test.test3app.huaweimeeting.conference.entry.ConferenceBaseItem;
import com.test.test3app.huaweimeeting.conference.entry.ConferenceTitleEntry;

import org.jetbrains.annotations.NotNull;

/**
 * created by zhaoyuntao
 * on 2020-03-16
 * description:
 */
public class ConferenceAdapter extends ExpandableRecyclerAdapter<ConferenceHolder, ConferenceBaseItem> {

    private OnItemClickListener onItemClickListener;

    public ConferenceAdapter() {
    }


    @Override
    public ConferenceHolder onCreateExpandableViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ConferenceBaseItem.TYPE_ITEM_NOT_START:
            case ConferenceBaseItem.TYPE_ITEM_ONGOING:
            case ConferenceBaseItem.TYPE_ITEM_CLOSED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_huawei_conference_item_conference_list, parent, false);
                break;
            case ConferenceBaseItem.TYPE_ITEM_DIVIDER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_huawei_conference_divider_list, parent, false);
                break;
            case ConferenceBaseItem.TYPE_TITLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_huawei_conference_title_conference_list, parent, false);
                break;
            default:
                view = new View(parent.getContext());
                break;
        }
        return new ConferenceHolder(view);
    }

    @Override
    public int getExpandableItemViewType(ConferenceBaseItem conferenceEntry) {
        return conferenceEntry.getType();
    }

    @Override
    protected void onBindExpandableViewHolder(ConferenceHolder holder, final int position, final ConferenceBaseItem conferenceBaseItem) {
        //todo set data in ui
        int type = conferenceBaseItem.getType();
        switch (type) {
            case ConferenceBaseItem.TYPE_ITEM_ONGOING:
                holder.setTextView1("15:30 2020-03-16");
                holder.setOnButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(conferenceBaseItem, position);
                        }
                    }
                });
                break;
            case ConferenceBaseItem.TYPE_ITEM_CLOSED:
                holder.setTextView1("16:30 2020-03-16");
                holder.hideButton();
                break;
            case ConferenceBaseItem.TYPE_ITEM_NOT_START:
                holder.setTextView1("18:30 2020-03-16");
                holder.hideButton();
                break;
            case ConferenceBaseItem.TYPE_TITLE:
                holder.setTextView1("18:30 2020-03-16");
                holder.setTitle(((ConferenceTitleEntry) conferenceBaseItem).getTitle());
                break;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ConferenceBaseItem conferenceBaseItem, int position);
    }

}
