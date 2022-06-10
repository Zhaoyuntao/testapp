package im.thebot;

import androidx.annotation.NonNull;

import im.thebot.api.chat.MessageSdk;
import im.turbo.basetools.util.ValueSafeTransfer;

/**
 * created by zhaoyuntao
 * on 05/08/2021
 * description:
 */
public class SdkFactory {
    private static final MessageSdk chatSdk = ValueSafeTransfer.from("com.sdk.chat.api.MessageSdkImpl");

    @NonNull
    public static MessageSdk getChatSdk() {
        return chatSdk;
    }
}
