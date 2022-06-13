package im.thebot.chat.mvp.presenter;

import im.thebot.chat.api.chat.message.AudioMessageForUI;
import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.api.chat.message.TextMessageForUI;
import im.thebot.user.ContactUtil;
import im.turbo.basetools.selector.ListItemSelector;
import im.turbo.basetools.util.ValueSafeTransfer;

/**
 * created by zhaoyuntao
 * on 09/06/2022
 * description:
 */
public class ChatPresenter {

    ListItemSelector<MessageBeanForUI> selector = new ListItemSelector<>();

    public ChatPresenter() {

        TextMessageForUI message0 = new TextMessageForUI();
        message0.setSenderUid(ContactUtil.uidMe);
        message0.setContent("HelloHello");

        TextMessageForUI message1 = new TextMessageForUI();
        message1.setSenderUid(ContactUtil.uidMe);
        message1.setContent("Hello\nHello\nHello\nHello");

        TextMessageForUI message2 = new TextMessageForUI();
        message2.setSenderUid(ContactUtil.uidOther);
        message2.setMessageReply(message0);
        message2.setContent("Hello\nHello\nHello\nHello");

        TextMessageForUI message3 = new TextMessageForUI();
        message3.setSenderUid(ContactUtil.uidOther2);
        message3.setMessageReply(message1);
        message3.setContent("123455");

        AudioMessageForUI audioMessage = new AudioMessageForUI();
        audioMessage.setSenderUid(ContactUtil.uidOther2);
        audioMessage.setAudioDuration(3000);
        audioMessage.setAudioPlayedTime(1);

        TextMessageForUI message4 = new TextMessageForUI();
        message4.setSenderUid(ContactUtil.uidOther2);
        message4.setMessageReply(message1);
        message4.setContent("123455");
        TextMessageForUI message5 = new TextMessageForUI();
        message5.setSenderUid(ContactUtil.uidOther2);
        message5.setMessageReply(message1);
        message5.setContent("123455");
        TextMessageForUI message6 = new TextMessageForUI();
        message6.setSenderUid(ContactUtil.uidOther2);
        message6.setMessageReply(message1);
        message6.setContent("123455");
        TextMessageForUI message7 = new TextMessageForUI();
        message7.setSenderUid(ContactUtil.uidOther2);
        message7.setMessageReply(message1);
        message7.setContent("123455");

        selector.addData(message0);
        selector.addData(message1);
        selector.addData(message2);
        selector.addData(message7);
        selector.addData(audioMessage);
        selector.addData(message4);
        selector.addData(message5);
        selector.addData(message6);
        selector.addData(message7);

        ValueSafeTransfer.iterate(selector.getData(), new ValueSafeTransfer.ElementIterator<MessageBeanForUI>() {
            long time = System.currentTimeMillis();

            @Override
            public MessageBeanForUI element(int position, MessageBeanForUI message) {
                audioMessage.setSessionId(ContactUtil.uidGroup);
                message.setCanBeReplied(true);
                message.setTimeSend(time++);
                message.setUUID(String.valueOf(message.getTimeSend()));
                return null;
            }
        });
    }

    public void setText(String text, int position) {
        if (position < selector.size()) {
            MessageBeanForUI message0 = selector.get(position);
            message0.setContent(text);
        }
    }

    public CharSequence getUUIDForNewMessageLine() {
        return null;
    }

    public int getUnreadCountForNewMessageLine() {
        return 0;
    }

    public boolean isSelecting() {
        return selector.getSelectSize() > 0;
    }

    public boolean isMessageSelected(int position) {
        return selector.isSelected(position);
    }

    public boolean isMessageSelected(String uuid) {
        return selector.isSelected(uuid);
    }

    public MessageBeanForUI getMessage(int position) {
        return selector.get(position);
    }

    public int messageSize() {
        return selector.size();
    }
}
