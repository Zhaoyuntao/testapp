package im.thebot.chat.api.chat.message.session;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * created by zhaoyuntao
 * on 15/04/2022
 * description:
 */
public class UUIDPacket implements Cloneable {
    private final String uuid;
    private final long time;

    public UUIDPacket(String uuid, long time) {
        this.uuid = uuid;
        this.time = time;
    }

    @NonNull
    @Override
    public UUIDPacket clone() {
        try {
            return (UUIDPacket) super.clone();
        } catch (CloneNotSupportedException e) {
            return new UUIDPacket(uuid, time);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UUIDPacket)) return false;
        UUIDPacket packet = (UUIDPacket) o;
        return TextUtils.equals(uuid, packet.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return " (uuid:" + uuid + ",time:" + time + ") ";
    }

    public String getUuid() {
        return uuid;
    }

    public long getTime() {
        return time;
    }
}
