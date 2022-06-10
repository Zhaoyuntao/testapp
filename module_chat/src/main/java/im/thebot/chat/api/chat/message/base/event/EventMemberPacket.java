package im.thebot.chat.api.chat.message.base.event;

/**
 * created by zhaoyuntao
 * on 11/04/2022
 * description:
 */
public class EventMemberPacket {
    private String uid;
    private String name;

    public EventMemberPacket(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }


    public String getName() {
        return name;
    }
}
