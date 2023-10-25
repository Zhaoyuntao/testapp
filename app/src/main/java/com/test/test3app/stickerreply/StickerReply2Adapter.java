package com.test.test3app.stickerreply;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.test.test3app.R;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 30/12/2020
 * description:
 */
class StickerReply2Adapter extends AutoFitAdapter<StickerRepliedBean> {
    public void addSticker(StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean) {
        StickerRepliedBean stickerRepliedBean = getData(stickerRepliedParticipantItemBean.getSticker());
        S.s("add sticker:" + stickerRepliedParticipantItemBean);
        if (stickerRepliedBean == null) {
            S.s("first add");
            stickerRepliedBean = new StickerRepliedBean();
            stickerRepliedBean.setTime(stickerRepliedParticipantItemBean.getTime());
            stickerRepliedBean.setSticker(stickerRepliedParticipantItemBean.getSticker());
            stickerRepliedBean.addStickerRepliedParticipantItemBeans(stickerRepliedParticipantItemBean);
            addData(stickerRepliedBean);
        } else {
            S.s("not first add");
            if (!stickerRepliedBean.contains(stickerRepliedParticipantItemBean)) {
                S.s("not contains");
                stickerRepliedBean.addStickerRepliedParticipantItemBeans(stickerRepliedParticipantItemBean);
                notifyItemChanged(getDataPosition(stickerRepliedBean));
            }
        }
    }

    public void removeSticker(StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean) {
        StickerRepliedBean stickerRepliedBean = getData(stickerRepliedParticipantItemBean.getSticker());
        if (stickerRepliedBean == null) {
            S.e("remove failed,is null");
            return;
        }
        int position = getDataPosition(stickerRepliedBean);
        S.s("position:" + position);
        stickerRepliedBean.removeStickerRepliedParticipantItemBeans(stickerRepliedParticipantItemBean);
        if (stickerRepliedBean.size() > 0) {
            StickerReplyAdapterDiffHelper.getIntBundle(StickerReplyAdapterDiffHelper.DIFF_COUNT, stickerRepliedBean.size());
            notifyItemChanged(position);
        } else {
            removeData(position);
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull ViewGroup parent, int position) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_sticker_reply, parent, false);
    }

    @Override
    public void onBindViewData(@NonNull ViewGroup parent,@NonNull View view, int position, @NonNull final StickerRepliedBean stickerRepliedBean) {
        S.s("onCreateViewData:" + position + " " + stickerRepliedBean);
        TextView stickerCountView = view.findViewById(R.id.sticker_reply_view_count);
        TextView stickerDescView = view.findViewById(R.id.sticker_reply_view_desc);
        stickerDescView.setText(stickerRepliedBean.getSticker());
        stickerCountView.setText(String.valueOf(stickerRepliedBean.size()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSticker(stickerRepliedBean);
            }
        });
    }

    @Override
    public void onChangeViewData(@NonNull ViewGroup parent, @NonNull View child, int position, @NonNull StickerRepliedBean stickerRepliedBean,@NonNull Bundle payload) {
        S.s("onChangeViewData:" + position + " " + stickerRepliedBean);
        TextView stickerCountView = child.findViewById(R.id.sticker_reply_view_count);
        TextView stickerDescView = child.findViewById(R.id.sticker_reply_view_desc);
        if (payload.containsKey(StickerReplyAdapterDiffHelper.DIFF_COUNT)) {
            stickerCountView.setText(String.valueOf(stickerRepliedBean.size()));
        }
        if (includeMySticker(stickerRepliedBean)) {
            child.setBackgroundResource(R.drawable.shape_background_sticker_corner_12dp_selected);
            stickerCountView.setTextColor(ContextCompat.getColor(child.getContext(), R.color.color_main_white_ffffff));
        } else {
            child.setBackgroundResource(R.drawable.shape_background_sticker_corner_12dp);
            stickerCountView.setTextColor(ContextCompat.getColor(child.getContext(), R.color.color_text_4b4b4b));
        }
    }

    private boolean includeMySticker(StickerRepliedBean stickerRepliedBean) {
        for (StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean : stickerRepliedBean.getStickerRepliedParticipantItemBeans()) {
            if (TextUtils.equals("abcd", stickerRepliedParticipantItemBean.getUid())) {
                return true;
            }
        }
        return false;
    }

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
}
