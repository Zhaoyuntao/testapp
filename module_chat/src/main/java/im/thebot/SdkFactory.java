package im.thebot;

import androidx.annotation.NonNull;

import im.thebot.api.chat.AudioSdk;
import im.thebot.api.chat.FileSdk;
import im.thebot.api.chat.MessageSdk;

/**
 * created by zhaoyuntao
 * on 05/08/2021
 * description:
 */
public class SdkFactory {
    private static final MessageSdk chatSdk = new MessageSdk();
    private static final AudioSdk audioSdk = new AudioSdk();
    private static final FileSdk fileSdk = new FileSdk();

    @NonNull
    public static MessageSdk getChatSdk() {
        return chatSdk;
    }

    public static AudioSdk getAudioSdk() {
        return audioSdk;
    }

    public static FileSdk getFileSdk() {
        return fileSdk;
    }
}
