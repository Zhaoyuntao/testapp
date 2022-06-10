package im.thebot.chat.api.chat.message.official;


import java.util.List;

import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.MessageBeanForUI;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class OfficialPayMessageForUI extends MessageBeanForUI {
    private String officialPayOaId;
    private String officialPayHead;
    private String officialPayTitle;
    private String officialPayOaName;
    private String officialPayBillId;
    private String officialPayShowFields;
    private String officialPayFixContents;
    private String officialPayExt;
    private List<PayData> officialPayShowFieldsData;
    private List<PayData> officialPayFixContentsData;

    public OfficialPayMessageForUI() {
        super(MessageTypeCode.kChatMsgType_PayOfficial);
    }

    public String getOfficialPayOaId() {
        return officialPayOaId;
    }

    public void setOfficialPayOaId(String officialPayOaId) {
        this.officialPayOaId = officialPayOaId;
    }

    public String getOfficialPayHead() {
        return officialPayHead;
    }

    public void setOfficialPayHead(String officialPayHead) {
        this.officialPayHead = officialPayHead;
    }

    public String getOfficialPayTitle() {
        return officialPayTitle;
    }

    public void setOfficialPayTitle(String officialPayTitle) {
        this.officialPayTitle = officialPayTitle;
    }

    public String getOfficialPayOaName() {
        return officialPayOaName;
    }

    public void setOfficialPayOaName(String officialPayOaName) {
        this.officialPayOaName = officialPayOaName;
    }

    public String getOfficialPayBillId() {
        return officialPayBillId;
    }

    public void setOfficialPayBillId(String officialPayBillId) {
        this.officialPayBillId = officialPayBillId;
    }

    public String getOfficialPayShowFields() {
        return officialPayShowFields;
    }

    public void setOfficialPayShowFields(String officialPayShowFields) {
        this.officialPayShowFields = officialPayShowFields;
    }

    public String getOfficialPayFixContents() {
        return officialPayFixContents;
    }

    public void setOfficialPayFixContents(String officialPayFixContents) {
        this.officialPayFixContents = officialPayFixContents;
    }

    public String getOfficialPayExt() {
        return officialPayExt;
    }

    public void setOfficialPayExt(String officialPayExt) {
        this.officialPayExt = officialPayExt;
    }

    public List<PayData> getOfficialPayShowFieldsData() {
        return officialPayShowFieldsData;
    }

    public void setOfficialPayShowFieldsData(List<PayData> officialPayShowFieldsData) {
        this.officialPayShowFieldsData = officialPayShowFieldsData;
    }

    public List<PayData> getOfficialPayFixContentsData() {
        return officialPayFixContentsData;
    }

    public void setOfficialPayFixContentsData(List<PayData> officialPayFixContentsData) {
        this.officialPayFixContentsData = officialPayFixContentsData;
    }
}
