package com.test.test3app.stickerreply;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 29/12/2020
 * description:
 */
public class TestStickerAdapter extends RecyclerView.Adapter<TestHolder> {
    private StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean;
    private String[] arr = {"🐑", "㊗️", "🐸", "😂", "👍", "🐂"};

    @NonNull
    @Override
    public TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_test_sticker_reply, parent, false);
        return new TestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, int position) {
        StickerReplyView stickerReplyView = holder.itemView.findViewById(R.id.view);
        init(stickerReplyView);
    }

    private void init(StickerReplyView stickerReplyView) {
        List<StickerRepliedBean> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            StickerRepliedBean stickerRepliedBean = new StickerRepliedBean();
            stickerRepliedBean.setSticker(arr[i]);
            List<StickerRepliedParticipantItemBean> list1 = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean = new StickerRepliedParticipantItemBean();
                if (j > 0) {
                    stickerRepliedParticipantItemBean.setUid("abcd" + j);
                } else {
                    stickerRepliedParticipantItemBean.setUid("abcd");
                }
                stickerRepliedParticipantItemBean.setName("abcd");
                stickerRepliedParticipantItemBean.setTime(123445);
                stickerRepliedParticipantItemBean.setSticker(arr[i]);
                list1.add(stickerRepliedParticipantItemBean);
            }
            stickerRepliedBean.setStickerRepliedParticipantItemBeans(list1);
            stickerRepliedBean.setTime(i);
            list.add(stickerRepliedBean);
        }

        stickerReplyView.initSticker(list);
    }

    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, int position, List<Object> payloads) {
        final StickerReplyView stickerReplyView = holder.itemView.findViewById(R.id.view);
        if (payloads.size() == 0) {
            onBindViewHolder(holder, position);
        } else {
            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle.getInt("add") == 1) {
                stickerReplyView.addSticker(stickerRepliedParticipantItemBean);
            } else if (bundle.getInt("add") == 0) {
                stickerReplyView.removeSticker(stickerRepliedParticipantItemBean);
            }
            stickerRepliedParticipantItemBean = null;
        }
    }

    public void debugAdd(StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean) {
        this.stickerRepliedParticipantItemBean = stickerRepliedParticipantItemBean;
        Bundle bundle = new Bundle();
        bundle.putInt("add", 1);
        notifyItemChanged(2, bundle);
    }

    public void reduce() {
        Bundle bundle = new Bundle();
        bundle.putInt("add", 0);
        notifyItemChanged(2, bundle);
    }

    @Override
    public int getItemCount() {
        return 200;
    }
}
