package com.test.test3app.stickerreply;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;
import im.turbo.utils.log.S;

import java.util.List;

/**
 * created by zhaoyuntao
 * on 28/12/2020
 * description:
 */
class StickerReplyAdapter extends RecyclerView.Adapter<StickerReplyViewHolder> {
    private BlueRecyclerViewItemCache<StickerRepliedBean> se;
    private final String myUid = "abcd";

    public StickerReplyAdapter() {
        se = new BlueRecyclerViewItemCache<>();
    }

    public void initData(List<StickerRepliedBean> list) {
        this.se.clear();
        this.se.setData(list);
        notifyDataSetChanged();
    }

    public void addSticker(StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean) {
        StickerRepliedBean stickerRepliedBean = se.get(stickerRepliedParticipantItemBean.getSticker());
        if (stickerRepliedBean == null) {
            S.s("add sticker:"+stickerRepliedParticipantItemBean);
            stickerRepliedBean = new StickerRepliedBean();
            stickerRepliedBean.setTime(stickerRepliedParticipantItemBean.getTime());
            stickerRepliedBean.setSticker(stickerRepliedParticipantItemBean.getSticker());
            stickerRepliedBean.addStickerRepliedParticipantItemBeans(stickerRepliedParticipantItemBean);
            se.addData(stickerRepliedBean);
            notifyItemInserted(se.size() - 1);
        } else {
            S.s("remove sticker:"+stickerRepliedParticipantItemBean);
            if (!stickerRepliedBean.contains(stickerRepliedParticipantItemBean)) {
                stickerRepliedBean.addStickerRepliedParticipantItemBeans(stickerRepliedParticipantItemBean);
                notifyItemChanged(se.getPosition(stickerRepliedBean));
            }
        }
    }

    public void removeSticker(StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean) {
        StickerRepliedBean stickerRepliedBean = se.get(stickerRepliedParticipantItemBean.getSticker());
        if (stickerRepliedBean == null) {
            return;
        }
        int position = se.getPosition(stickerRepliedBean);
        stickerRepliedBean.removeStickerRepliedParticipantItemBeans(stickerRepliedParticipantItemBean);
        if (stickerRepliedBean.size() > 0) {
            StickerReplyAdapterDiffHelper.getIntBundle(StickerReplyAdapterDiffHelper.DIFF_COUNT, stickerRepliedBean.size());
            notifyItemChanged(position);
        } else {
            se.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public StickerReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickerReplyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_sticker_reply, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StickerReplyViewHolder holder, int position) {
        final StickerRepliedBean stickerRepliedBean = se.get(position);
        if (stickerRepliedBean == null) {
            return;
        }
        holder.setSticker(stickerRepliedBean.getSticker());
        holder.setStickerCount(stickerRepliedBean.size());
        holder.setOnClickItemListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean added = updateSticker(stickerRepliedBean);
            }
        });
        holder.setStickerSelected(includeMySticker(stickerRepliedBean));
    }

    @Override
    public void onBindViewHolder(@NonNull StickerReplyViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() <= 0) {
            onBindViewHolder(holder, position);
            return;
        }
        final StickerRepliedBean stickerRepliedBean = se.get(position);
        if (stickerRepliedBean == null) {
            return;
        }
        for (Object object : payloads) {
            Bundle bundle = (Bundle) object;
            if (bundle.containsKey(StickerReplyAdapterDiffHelper.DIFF_COUNT)) {
                holder.setStickerCount(stickerRepliedBean.size());
                holder.setStickerSelected(includeMySticker(stickerRepliedBean));
            }
        }
    }

    //TODO remote operation.
    public boolean updateSticker(StickerRepliedBean stickerRepliedBean) {
        StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean = new StickerRepliedParticipantItemBean();
        stickerRepliedParticipantItemBean.setTime(System.currentTimeMillis());
        stickerRepliedParticipantItemBean.setSticker(stickerRepliedBean.getSticker());
        stickerRepliedParticipantItemBean.setUid("abcd");
        stickerRepliedParticipantItemBean.setName("abcd");

        if (stickerRepliedBean.contains(stickerRepliedParticipantItemBean)) {
            S.s("remove:" + stickerRepliedParticipantItemBean + "    size:" + stickerRepliedBean.size());
            removeSticker(stickerRepliedParticipantItemBean);
            return false;
        } else {
            S.s("add:" + stickerRepliedParticipantItemBean + "    size:" + stickerRepliedBean.size());
            addSticker(stickerRepliedParticipantItemBean);
            return true;
        }
    }

    private boolean includeMySticker(StickerRepliedBean stickerRepliedBean) {
        for (StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean : stickerRepliedBean.getStickerRepliedParticipantItemBeans()) {
            if (TextUtils.equals(myUid, stickerRepliedParticipantItemBean.getUid())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return se.size();
    }

}
