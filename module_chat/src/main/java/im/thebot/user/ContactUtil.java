package im.thebot.user;

/**
 * created by zhaoyuntao
 * on 09/06/2022
 * description:
 */
public class ContactUtil {
    public static String uidMe = "123";
    public static String uidOther = "456";
    public static String uidOther2 = "111111111111111111111111111111";
    public static String uidGroup = "789";

    public static boolean isUser(String uid) {
        return !uidGroup.equals(uid);
    }

    public static boolean isGroup(String uid) {
        return uidGroup.equals(uid);
    }

    public static boolean isSelf(String uid) {
        return uidMe.equals(uid);
    }

    public static boolean isMySelf(String uid) {
        return uidMe.equals(uid);
    }

    public static boolean isMentionAll(String uid) {
        return false;
    }

    public static boolean isOAAccount(String sessionId) {
        return false;
    }

    public static boolean isPayOfficial(String senderUid) {
        return false;
    }
}
