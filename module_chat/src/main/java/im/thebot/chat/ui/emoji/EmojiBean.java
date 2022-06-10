package im.thebot.chat.ui.emoji;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * created by zhaoyuntao
 * on 01/09/2020
 * description:
 */
public class EmojiBean {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static EmojiBean fromCodePoint(int code) {
        EmojiBean emojiBean = new EmojiBean();
        emojiBean.setCode(newString(code));
        return emojiBean;
    }

    public static EmojiBean fromChars(int[] codePoints) {
        EmojiBean emojiBean = new EmojiBean();
        emojiBean.setCode(new String(codePoints, 0, codePoints.length));
        return emojiBean;
    }

    public static EmojiBean fromChar(char ch) {
        EmojiBean emojiBean = new EmojiBean();
        emojiBean.setCode(Character.toString(ch));
        return emojiBean;
    }

    public static String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }


    @NonNull
    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmojiBean)) return false;
        EmojiBean that = (EmojiBean) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
