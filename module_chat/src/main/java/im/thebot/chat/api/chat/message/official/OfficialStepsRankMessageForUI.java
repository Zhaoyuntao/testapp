package im.thebot.chat.api.chat.message.official;


import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.MessageBeanForUI;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class OfficialStepsRankMessageForUI extends MessageBeanForUI {
    private int stepsRankRank;
    private int stepsRankStepsCount;
    private String stepsRankStepsDate;
    private String stepsRankUri;

    public OfficialStepsRankMessageForUI() {
        super(MessageTypeCode.kChatMsgType_StepsRanking);
    }

    public int getStepsRankRank() {
        return stepsRankRank;
    }

    public void setStepsRankRank(int stepsRankRank) {
        this.stepsRankRank = stepsRankRank;
    }

    public int getStepsRankStepsCount() {
        return stepsRankStepsCount;
    }

    public void setStepsRankStepsCount(int stepsRankStepsCount) {
        this.stepsRankStepsCount = stepsRankStepsCount;
    }

    public String getStepsRankStepsDate() {
        return stepsRankStepsDate;
    }

    public void setStepsRankStepsDate(String stepsRankStepsDate) {
        this.stepsRankStepsDate = stepsRankStepsDate;
    }

    public String getStepsRankUri() {
        return stepsRankUri;
    }

    public void setStepsRankUri(String stepsRankUri) {
        this.stepsRankUri = stepsRankUri;
    }
}
