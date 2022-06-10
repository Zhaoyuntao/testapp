package im.thebot.common.ui.chat.mention;

import android.text.TextUtils;

/**
 * created by zhaoyuntao
 * on 03/06/2022
 * description:
 */
public class MentionTextUtil {
    public static final char FSI = '\u2068';
    public static final char SPACE = ' ';
    public static final String MENTION = "@";

    public static String removeMentionSymbol(String mention) {
        if (!TextUtils.isEmpty(mention)) {
            mention = mention.replaceAll("@", "");
            mention = mention.replaceAll("\\u2068", "");
        }
        return mention;
    }

    public static String getMentionNameContent(CharSequence name) {
        return MENTION + name;
    }

    public static String getMentionUidContent(String uid) {
        return MENTION + FSI + uid + FSI;
    }
}
