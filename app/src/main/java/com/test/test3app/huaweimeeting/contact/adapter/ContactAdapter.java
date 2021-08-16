package com.test.test3app.huaweimeeting.contact.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.test.test3app.R;
import com.test.test3app.expandrecyclerview.ExpandableRecyclerAdapter;
import com.test.test3app.huaweimeeting.contact.entry.ContactBaseEntry;

import org.jetbrains.annotations.NotNull;

/**
 * created by zhaoyuntao
 * on 2020-03-16
 * description:
 */
public class ContactAdapter extends ExpandableRecyclerAdapter<ContactHolder, ContactBaseEntry> {

    private OnItemClickListener onItemClickListener;

    @Override
    public ContactHolder onCreateExpandableViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == ContactBaseEntry.TYPE_TITLE) {
            return new ContactHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_huawei_conference_title_contact_list, parent, false));
        } else {
            return new ContactHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_huawei_conference_item_contact_list, parent, false));
        }
    }

    @Override
    protected void onBindExpandableViewHolder(ContactHolder holder, final int position, final ContactBaseEntry contactEntry) {
        holder.setText(contactEntry.getName());
        if (onItemClickListener == null) {
            return;
        }
        holder.setOnCallClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onCallClick(contactEntry);
            }
        });
        holder.setOnVideoClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onVideoClick(contactEntry);
            }
        });
        holder.setOnCheckChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onItemClickListener.onCheckChange(contactEntry, isChecked);
            }
        });
    }

    @Override
    public int getExpandableItemViewType(ContactBaseEntry contactEntry) {
        return contactEntry.getType();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onVideoClick(ContactBaseEntry ContactBaseEntry);

        void onCallClick(ContactBaseEntry ContactBaseEntry);

        void onCheckChange(ContactBaseEntry ContactBaseEntry, boolean isCheck);
    }
}
