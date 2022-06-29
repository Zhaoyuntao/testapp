package im.thebot.chat.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.module_chat.R;
import com.sdk.chat.file.audio.AudioFilePacket;

import java.util.UUID;

import base.ui.BaseActivity;
import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.mvp.presenter.ChatPresenter;
import im.thebot.chat.ui.adapter.ChatAdapter;
import im.thebot.chat.ui.view.AudioBucketView;
import im.thebot.chat.ui.view.AudioDraftView;
import im.thebot.chat.ui.view.AudioRecordView;
import im.turbo.baseui.chat.ChatEditText;
import im.turbo.baseui.chat.ChatLayoutManager;
import im.turbo.baseui.chat.ChatRecyclerView;
import im.turbo.baseui.chat.SmoothSwitchFrameLayout;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionResult;
import im.turbo.baseui.permission.PermissionUtils;
import im.turbo.utils.log.S;

public class ChatActivity extends BaseActivity {
    ChatEditText editText1;
    AudioRecordView audioRecordView;
    AudioDraftView audioDraftView;

    AudioBucketView canView1;
    View emojiIconView;
    SmoothSwitchFrameLayout smoothSwitchFrameLayout;
    ChatRecyclerView chatView;
    ViewGroup edittextBarContainer;

    ChatPresenter chatPresenter;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatView = findViewById(R.id.chat_recycler_view);
        chatView.setLayoutManager(new ChatLayoutManager(activity()));
        chatAdapter = new ChatAdapter(getPresenter());
        chatAdapter.setMessageCallback(new ChatAdapter.OnMessageClickListener() {
            @Override
            public void onMessageBindToAdapter(@NonNull MessageBeanForUI messageBean) {

            }

            @Override
            public boolean canClickMessage(@NonNull MessageBeanForUI messageBean) {
                return false;
            }

            @Override
            public void onClickReplyMessage(@NonNull MessageBeanForUI messageBean) {

            }

            @Override
            public boolean onLongClickReplyMessage(@NonNull MessageBeanForUI messageBean) {
                return false;
            }

            @Override
            public void onNameClick(@NonNull MessageBeanForUI messageBean) {

            }

            @Override
            public void onClickForwardButton(@NonNull MessageBeanForUI message) {

            }

            @Override
            public void onSlideMessage(@NonNull MessageBeanForUI messageBean) {

            }

            @Override
            public void onSelectItem(@NonNull MessageBeanForUI messageBean, boolean selected) {

            }
        });
        chatView.setAdapter(chatAdapter);

        editText1 = findViewById(R.id.edit_text_chat);
        audioRecordView = findViewById(R.id.chat_record_view);
        audioDraftView = findViewById(R.id.audio_draft_view);
        canView1 = findViewById(R.id.garbage_can_0);
        emojiIconView = findViewById(R.id.button_chat_input_bar_emoji);
        smoothSwitchFrameLayout = findViewById(R.id.double_switch_view_chat_record);
        edittextBarContainer = findViewById(R.id.input_bar_edittext_container);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                smoothSwitchFrameLayout.switchIndex(TextUtils.isEmpty(s) ? SmoothSwitchFrameLayout.INDEX_DEFAULT : SmoothSwitchFrameLayout.INDEX_SECOND);
                setAudioRecordLockedState(false);
            }
        });

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getPresenter().setText(s.toString(), 0);
                chatAdapter.notifyItemChanged(0);
            }
        });

        audioRecordView.setListener(new AudioRecordView.AudioRecordViewListener() {
            @Override
            public void onFingerSliding(boolean horizontal) {
                setEdittextState(horizontal, true);
            }

            @Override
            public void onFingerSlideCanceled(long durationMills) {
//                ToastUtil.show("Canceled");
                S.e("canceled");
                setEdittextState(false, false);
                getPresenter().dropAudio();
            }

            @Override
            public void onViewClosed() {
                smoothSwitchFrameLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFingerSlideLocked(long durationMills) {
                setAudioRecordLockedState(true);
                audioDraftView.setRecordingInitData(getPresenter().getAudioDraft().getVolumes(), durationMills);
            }

            @Override
            public boolean onFingerPressed() {
                getPresenter().setUuidForAudioRecord(UUID.randomUUID().toString());
                boolean hasPermission = PermissionUtils.hasPermission(activity(), Permission.RECORD_AUDIO);
                if (hasPermission) {
                    S.s("start...");
                    smoothSwitchFrameLayout.setVisibility(View.INVISIBLE);
                    setEdittextState(false, true);
                    getPresenter().startRecord();
                } else {
                    PermissionUtils.requestPermission(activity(), new PermissionResult() {
                        @Override
                        public void onGranted(@NonNull String[] grantedPermissions) {
                            //do nothing
                        }
                    }, Permission.RECORD_AUDIO);
                }
                return hasPermission;
            }

            @Override
            public void onFingerRelease(long durationMills) {
                setEdittextState(false, false);
                getPresenter().finishRecord();
            }
        });

        audioDraftView.setListener(new AudioDraftView.AudioDraftListener() {
            @Override
            public void onClickSend() {
                setAudioRecordLockedState(false);
                getPresenter().stopPlaying();
                getPresenter().finishRecord();
            }

            @Override
            public void onClickDelete() {
                setAudioRecordLockedState(false);
                getPresenter().stopPlaying();
                getPresenter().dropAudio();
            }

            @Override
            public void onPauseRecord() {
                getPresenter().pauseRecord();
            }

            @Override
            public void onResumeRecord() {
                getPresenter().resumeRecord();
            }

            @Override
            public void onPlay(float percent) {
                getPresenter().playAudioDraft(percent);
            }

            @Override
            public void onPausePlay() {
                getPresenter().pausePlayingAudio();
            }

            @Override
            public boolean isPlayingDraft() {
                return getPresenter().isAudioDraftPlaying();
            }

            @Override
            public void pausePlaying() {
                getPresenter().pausePlayingAudio();
            }

            @Override
            public long getAudioDuration() {
                return getPresenter().getAudioDraftDuration();
            }
        });

        setAudioRecordLockedState(false);
    }


    private void runUI(Runnable runnable) {
        runOnUiThread(runnable);
    }

    private void setAudioRecordLockedState(boolean locked) {
        audioDraftView.setVisibility(locked ? View.VISIBLE : View.GONE);
        if (locked) {
            audioDraftView.registerListener(getPresenter().getUuidForAudioRecord());
        }
        audioRecordView.setCanRecord(!locked && smoothSwitchFrameLayout.getIndex() == SmoothSwitchFrameLayout.INDEX_DEFAULT);
        setEdittextState(false, locked);
    }

    private void setEdittextState(boolean horizontal, boolean isRecording) {
        edittextBarContainer.setVisibility(horizontal ? View.INVISIBLE : View.VISIBLE);
        editText1.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                return isRecording ? "" : null;
            }
        }});
    }

    public void onStartRecord() {
        audioDraftView.start();
    }

    public void onRecording(float volume, long duration) {
        audioDraftView.setCurrentVolume(volume);
        audioDraftView.setRecordDuration(duration);
    }

    public void onRecordEnd(AudioFilePacket audioFilePacket) {
        if (audioFilePacket.getDuration() > 1000) {
            S.s("success:[duration:" + audioFilePacket.getDuration() + "][size:" + audioFilePacket.getFileSize() + "]" + audioFilePacket.getFilePath());
            int position = getPresenter().sendAudioMessage(audioFilePacket);
            chatAdapter.notifyItemInserted(position);
            chatView.scrollDynamicToBottom();
        } else {
            S.e("too short:" + audioFilePacket.getDuration());
        }
    }

    private ChatPresenter getPresenter() {
        if (chatPresenter == null) {
            chatPresenter = new ChatPresenter(this);
        }
        return chatPresenter;
    }

    public void setRecordingInitData(float[] volumes, long duration) {
        audioDraftView.setRecordingInitData(volumes, duration);
    }

    public void onAudioDropped(AudioFilePacket audioFilePacket) {
        if (audioFilePacket.getDuration() < 1000) {
            return;
        }
        canView1.setVisibility(View.VISIBLE);
        canView1.start(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                emojiIconView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                emojiIconView.setVisibility(View.VISIBLE);
                canView1.setVisibility(View.INVISIBLE);
            }
        });
    }
}
