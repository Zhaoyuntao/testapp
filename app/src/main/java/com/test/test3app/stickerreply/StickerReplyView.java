package com.test.test3app.stickerreply;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import im.turbo.utils.log.S;

import java.util.List;

/**
 * created by zhaoyuntao
 * on 28/12/2020
 * description:
 */
public class StickerReplyView extends AutoFitView {

    private StickerReply2Adapter stickerReplyAdapter;

    public StickerReplyView(@NonNull Context context) {
        super(context);
        init();
    }

    public StickerReplyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StickerReplyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        stickerReplyAdapter = new StickerReply2Adapter();
        setAdapter(stickerReplyAdapter);
    }

    public void initSticker(List<StickerRepliedBean> stickerRepliedBean) {
        stickerReplyAdapter.initData(stickerRepliedBean);
    }

    public void addSticker(StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean) {
        S.s("add sticker:"+stickerRepliedParticipantItemBean.getSticker());
        stickerReplyAdapter.addSticker(stickerRepliedParticipantItemBean);
    }

    public void removeSticker(StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean) {
        S.s("remove sticker:"+stickerRepliedParticipantItemBean.getSticker());
        stickerReplyAdapter.removeSticker(stickerRepliedParticipantItemBean);
    }
}
