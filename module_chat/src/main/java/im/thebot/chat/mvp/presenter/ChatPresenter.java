package im.thebot.chat.mvp.presenter;

import androidx.annotation.NonNull;

import com.sdk.chat.file.audio.AudioFilePacket;
import com.sdk.chat.file.audio.AudioRecordListener;
import com.sdk.chat.file.audio.AudioPlayStatusCode;
import com.sdk.chat.file.audio.MessageMediaHelper;

import im.thebot.SdkFactory;
import im.thebot.chat.api.chat.message.AudioMessageForUI;
import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.api.chat.message.TextMessageForUI;
import im.thebot.chat.ui.ChatActivity;
import im.thebot.user.ContactUtil;
import im.turbo.basetools.selector.ListItemSelector;
import im.turbo.basetools.util.ValueSafeTransfer;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 09/06/2022
 * description:
 */
public class ChatPresenter {

    private ChatActivity chatActivity;

    ListItemSelector<MessageBeanForUI> selector = new ListItemSelector<>();
    private String uuidForAudioRecord;
    private String sessionId = ContactUtil.uidGroup;

    public ChatPresenter(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;

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
        audioMessage.setFileLocalPath("/storage/emulated/0/Music/Browser/PerituneMaterial_Sakuya2.mp3");
        audioMessage.setAudioDuration(MessageMediaHelper.getAudioDuration(audioMessage.getFileLocalPath()));
        audioMessage.setTimeSend(1000000);
//        audioMessage.setAudioPlayedTime(5860);

        AudioMessageForUI audioMessageMe = new AudioMessageForUI();
        audioMessageMe.setSenderUid(ContactUtil.uidMe);
        audioMessageMe.setFileLocalPath("/storage/emulated/0/Music/Browser/PerituneMaterial_Sakuya2.mp3");
        audioMessageMe.setAudioDuration(MessageMediaHelper.getAudioDuration(audioMessageMe.getFileLocalPath()));
        audioMessageMe.setTimeSend(1000001);
//        audioMessage.setAudioPlayedTime(5860);

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
//        selector.addData(message2);
//        selector.addData(message7);
        selector.addData(audioMessage);
        selector.addData(audioMessageMe);
        selector.addData(message4);
        selector.addData(message5);
        selector.addData(message6);
        selector.addData(message7);

        ValueSafeTransfer.iterate(selector.getData(), new ValueSafeTransfer.ElementIterator<MessageBeanForUI>() {
            long time = System.currentTimeMillis();

            @Override
            public MessageBeanForUI element(int position, MessageBeanForUI message) {
                setCommon(message, time);
                return null;
            }
        });
    }

    private void setCommon(MessageBeanForUI message, long time) {
        message.setSessionId(getSessionId());
        message.setCanBeReplied(true);
        if (message.getTimeSend() <= 0) {
            message.setTimeSend(time++);
        }
        message.setUUID(String.valueOf(message.getTimeSend()));
    }

    public int add() {
        TextMessageForUI messageNew = new TextMessageForUI();
        messageNew.setSenderUid(ContactUtil.uidMe);
        messageNew.setContent("HelloHello");
        messageNew.setSessionId(ContactUtil.uidGroup);
        messageNew.setCanBeReplied(true);
        messageNew.setTimeSend(System.currentTimeMillis());
        messageNew.setUUID(String.valueOf(messageNew.getTimeSend()));

        selector.addData(messageNew);
        return selector.size() - 1;
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

    public int sendAudioMessage(AudioFilePacket audioFilePacket) {
        AudioMessageForUI audioMessage = new AudioMessageForUI();
        audioMessage.setFileLocalPath(audioFilePacket.getFilePath());
        audioMessage.setFileSize(audioFilePacket.getFileSize());
        audioMessage.setSenderUid(ContactUtil.uidOther2);
        audioMessage.setAudioDuration(audioFilePacket.getDuration());
        audioMessage.setAudioPlayedTime(1);
        setCommon(audioMessage, System.currentTimeMillis());

        selector.addData(audioMessage);
        return selector.size();
    }

    public void playAudio(AudioMessageForUI message, int calculateCurrentPlayingProgress) {
        AudioFilePacket audioFilePacket = new AudioFilePacket();
        audioFilePacket.setFileSize(message.getFileSize());
        audioFilePacket.setFilePath(message.getFileLocalPath());
        audioFilePacket.setDuration(message.getAudioDuration());
        audioFilePacket.setUuid(message.getUUID());
        SdkFactory.getAudioSdk().startPlay(audioFilePacket, calculateCurrentPlayingProgress);
    }

    public void playAudioDraft(float percent) {
        AudioFilePacket audioFilePacket = getAudioDraft();
        S.s("play draft:" + audioFilePacket);
        if (audioFilePacket != null) {
            audioFilePacket.setUuid(uuidForAudioRecord);
            if (!audioFilePacket.hasError()) {
                SdkFactory.getAudioSdk().startPlay(audioFilePacket, (int) (audioFilePacket.getDuration() * percent));
            }
        }
    }

    public void pausePlayingAudio() {
        SdkFactory.getAudioSdk().pausePlayingAudio();
    }

    public boolean isAudioPlaying() {
        return SdkFactory.getAudioSdk().isAudioPlaying();
    }

    public String getUuidForAudioRecord() {
        return uuidForAudioRecord;
    }

    public void setUuidForAudioRecord(String uuidForAudioRecord) {
        this.uuidForAudioRecord = uuidForAudioRecord;
    }

    private AudioRecordListener audioRecordListener;

    public AudioRecordListener getRecordListener() {
        if (audioRecordListener == null) {
            audioRecordListener = new AudioRecordListener() {
                @Override
                public void onRecordStart(float[] volumes, long audioDuration) {
                    runUI(() -> getView().onStartRecord());
                }

                @Override
                public void onRecording(float volume, long duration) {
                    runUI(() -> getView().onRecording(volume, duration));
                }

                @Override
                public void onRecordEnd(@NonNull AudioFilePacket audioFilePacket) {
                    S.sd("end");
                    runUI(() -> {
                        getView().onRecordEnd(audioFilePacket);
                    });
                }

                @Override
                public void onAudioDropped(@NonNull AudioFilePacket audioFilePacket) {
                    runUI(new Runnable() {
                        @Override
                        public void run() {
                            getView().onAudioDropped(audioFilePacket);
                        }
                    });
                }

                @Override
                public void onRecordPause(@NonNull AudioFilePacket audioFilePacket) {

                }

                @Override
                public void onError(String errorMessage) {
                }
            };
        }
        return audioRecordListener;
    }

    private ChatActivity getView() {
        return chatActivity;
    }

    private void runUI(Runnable runnable) {
        chatActivity.runOnUiThread(runnable);
    }

    public void startRecord() {
//        SdkFactory.getAudioSdk().startRecord(getRecordListener());
    }

    public AudioFilePacket getAudioDraft() {
        return SdkFactory.getAudioSdk().getAudioDraft();
    }

    public void pauseRecord() {
        SdkFactory.getAudioSdk().pauseRecord();
    }

    public void dropAudio() {
        SdkFactory.getAudioSdk().cancelRecord();
    }

    public void finishRecord() {
        SdkFactory.getAudioSdk().stopRecord();
    }

    public void resumeRecord() {
        AudioFilePacket audioFilePacket = getAudioDraft();
        SdkFactory.getAudioSdk().stopPlayingAudio();
        SdkFactory.getAudioSdk().resumeRecord();
        getView().setRecordingInitData(audioFilePacket.getVolumes(), audioFilePacket.getDuration());
    }

    public void stopPlaying(){
        SdkFactory.getAudioSdk().stopPlayingAudio();
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getAudioDraftDuration() {
        AudioFilePacket audioFilePacket = getAudioDraft();
        return audioFilePacket == null ? 0 : audioFilePacket.getDuration();
    }

    public boolean isAudioDraftPlaying() {
        return SdkFactory.getAudioSdk().getAudioStatus(getSessionId(), uuidForAudioRecord).getAudioStatusCode() == AudioPlayStatusCode.STATUS_AUDIO_PLAYING;
    }

    public float[] getAudioDraftVolumes() {
        AudioFilePacket audioFilePacket = getAudioDraft();
        return audioFilePacket == null ? null : audioFilePacket.getVolumes();
    }
}
