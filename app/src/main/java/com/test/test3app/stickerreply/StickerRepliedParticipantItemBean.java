package com.test.test3app.stickerreply;

import java.util.Objects;

/**
 * created by zhaoyuntao
 * on 23/12/2020
 * description:
 */
public class StickerRepliedParticipantItemBean {
    private String name;
    private String uid;
    private String sticker;
    private long time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public String getSticker() {
        return this.sticker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StickerRepliedParticipantItemBean that = (StickerRepliedParticipantItemBean) o;
        return Objects.equals(sticker, that.sticker) &&
                Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sticker, uid);
    }

    @Override
    public String toString() {
        return "StickerRepliedParticipantItemBean{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", sticker='" + sticker + '\'' +
                ", time=" + time +
                '}';
    }
}
