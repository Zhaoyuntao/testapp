package im.thebot.chat.api.chat.message;

import im.thebot.api.chat.constant.MessageTypeCode;

/**
 * created by zhaoyuntao
 * on 07/04/2022
 * description:
 */
public class ContactCardMessageForUI extends MessageBeanForUI {
    private String contactCardName;
    private String contactCardEmail;
    private String contactCardPhone;
    private String contactCardAvatarUrl;
    private int contactCardEmailType;
    private int contactCardPhoneType;
    private boolean contactCardIsRegistered;
    private boolean contactCardIsMyFriend;
    private String contactCardUid;

    public ContactCardMessageForUI() {
        super(MessageTypeCode.kChatMsgType_ContactCard);
    }

    public String getContactCardName() {
        return contactCardName;
    }

    public void setContactCardName(String contactCardName) {
        this.contactCardName = contactCardName;
    }

    public String getContactCardEmail() {
        return contactCardEmail;
    }

    public void setContactCardEmail(String contactCardEmail) {
        this.contactCardEmail = contactCardEmail;
    }

    public String getContactCardPhone() {
        return contactCardPhone;
    }

    public void setContactCardPhone(String contactCardPhone) {
        this.contactCardPhone = contactCardPhone;
    }

    public int getContactCardEmailType() {
        return contactCardEmailType;
    }

    public void setContactCardEmailType(int contactCardEmailType) {
        this.contactCardEmailType = contactCardEmailType;
    }

    public int getContactCardPhoneType() {
        return contactCardPhoneType;
    }

    public void setContactCardPhoneType(int contactCardPhoneType) {
        this.contactCardPhoneType = contactCardPhoneType;
    }

    public String getContactCardAvatarUrl() {
        return contactCardAvatarUrl;
    }

    public void setContactCardAvatarUrl(String contactCardAvatarUrl) {
        this.contactCardAvatarUrl = contactCardAvatarUrl;
    }

    public boolean isContactCardIsRegistered() {
        return contactCardIsRegistered;
    }

    public void setContactCardIsRegistered(boolean contactCardIsRegistered) {
        this.contactCardIsRegistered = contactCardIsRegistered;
    }

    public boolean isContactCardIsMyFriend() {
        return contactCardIsMyFriend;
    }

    public void setContactCardIsMyFriend(boolean contactCardIsMyFriend) {
        this.contactCardIsMyFriend = contactCardIsMyFriend;
    }

    public String getContactCardUid() {
        return contactCardUid;
    }

    public void setContactCardUid(String contactCardUid) {
        this.contactCardUid = contactCardUid;
    }
}
