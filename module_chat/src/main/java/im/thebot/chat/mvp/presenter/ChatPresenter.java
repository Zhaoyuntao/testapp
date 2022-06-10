package im.thebot.chat.mvp.presenter;

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
        long time = System.currentTimeMillis();

        TextMessageForUI message0 = new TextMessageForUI();
        message0.setSessionId(ContactUtil.uidGroup);
        message0.setSenderUid(ContactUtil.uidMe);
        message0.setTimeSend(time++);
        message0.setContent("HelloHello");

        TextMessageForUI message1 = new TextMessageForUI();
        message1.setSessionId(ContactUtil.uidGroup);
        message1.setSenderUid(ContactUtil.uidMe);
        message1.setTimeSend(time++);
        message1.setContent("Hello\nHello\nHello\nHello");

        TextMessageForUI message2 = new TextMessageForUI();
        message2.setSessionId(ContactUtil.uidGroup);
        message2.setSenderUid(ContactUtil.uidOther);
        message2.setMessageReply(message0);
        message2.setTimeSend(time++);
        message2.setContent("Hello\nHello\nHello\nHello");

        TextMessageForUI message3 = new TextMessageForUI();
        message3.setSessionId(ContactUtil.uidGroup);
        message3.setSenderUid(ContactUtil.uidOther2);
        message3.setMessageReply(message1);
        message3.setTimeSend(time++);
        message3.setContent("123455");

//        selector.addData(message0);
//        selector.addData(message1);
//        selector.addData(message2);
        selector.addData(message3);

        ValueSafeTransfer.iterate(selector.getData(), new ValueSafeTransfer.ElementIterator<MessageBeanForUI>() {
            @Override
            public MessageBeanForUI element(int position, MessageBeanForUI messageBeanForUI) {
                messageBeanForUI.setCanBeReplied(true);
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
