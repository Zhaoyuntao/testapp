package com.test.test3app.stickerreply;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 23/12/2020
 * description:
 */
public class StickerRepliedBean implements Selectable<String> {
    private String sticker;
    private long time;
    private List<StickerRepliedParticipantItemBean> stickerRepliedParticipantItemBeans = new ArrayList<>();

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<StickerRepliedParticipantItemBean> getStickerRepliedParticipantItemBeans() {
        return stickerRepliedParticipantItemBeans;
    }

    public void setStickerRepliedParticipantItemBeans(List<StickerRepliedParticipantItemBean> stickerRepliedParticipantItemBeans) {
        this.stickerRepliedParticipantItemBeans = stickerRepliedParticipantItemBeans;
    }

    public void addStickerRepliedParticipantItemBeans(StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean) {
        if (!stickerRepliedParticipantItemBeans.contains(stickerRepliedParticipantItemBean)) {
            stickerRepliedParticipantItemBeans.add(stickerRepliedParticipantItemBean);
        }
    }

    public void removeStickerRepliedParticipantItemBeans(StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean) {
        if (stickerRepliedParticipantItemBeans == null) {
            stickerRepliedParticipantItemBeans = new ArrayList<>();
        }
        stickerRepliedParticipantItemBeans.remove(stickerRepliedParticipantItemBean);
    }

    public int size() {
        return stickerRepliedParticipantItemBeans.size();
    }

    @Override
    public String getUniqueIdentificationId() {
        return sticker;
    }

    public boolean contains(StickerRepliedParticipantItemBean stickerRepliedParticipantItemBean) {
        return stickerRepliedParticipantItemBeans.contains(stickerRepliedParticipantItemBean);
    }
}
