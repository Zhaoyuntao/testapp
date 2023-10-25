package im.thebot.chat.api.chat.message;


import androidx.annotation.NonNull;

import im.thebot.api.chat.constant.MessageTypeCode;
import im.thebot.chat.api.chat.message.text.BaseTextMessageForUI;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class TextMessageForUI extends BaseTextMessageForUI {

    public TextMessageForUI() {
        super(MessageTypeCode.kChatMsgType_WrapText);
    }

//    //todo debug code.
//    @Override
//    public void setContent(String content) {
//        content = "1234567" +
//                " zhaoyuntao@qq.com " +
//                "abhttp://www.zhaoyuntao.com," +
//                "http://www.zhaoyuntao.com," +
//                "cdefg @\u2068All\u2068 abcdefg " +
//                "cdefg @\u2068zhaoyuntao\u2068 abcdefg " +
//                "@\u2068971501635523\u2068 abcdefg @\u2068971501635523\u2068 abcdefg @\u2068971501635523\u2068 abcdefg @\u2068971501635523\u2068 abcdefg @\u2068971501635523\u2068 abcdefg";
//        super.setContent(content);
//    }


    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
