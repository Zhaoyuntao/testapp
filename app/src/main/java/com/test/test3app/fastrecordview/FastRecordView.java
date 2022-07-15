package com.test.test3app.fastrecordview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.test3app.fastrecordviewnew.TouchEvent;
import im.turbo.basetools.utils.BitmapUtils;

/**
 * created by zhaoyuntao
 * on 2019-10-23
 * description:
 * main audio view include two child view : AudioRecordView and InputActionView
 */
public class FastRecordView extends FrameLayout implements TouchEvent {
    private AudioRecordViewOld2 audioRecordView;
    private InputActionView inputActionView;

    public FastRecordView(@NonNull Context context) {
        super(context);
        init();
    }

    public FastRecordView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FastRecordView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        audioRecordView = new AudioRecordViewOld2(getContext());
        inputActionView = new InputActionView(getContext());
        //always on bottom
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.BOTTOM;
        audioRecordView.setLayoutParams(layoutParams);

        LayoutParams actionLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, BitmapUtils.dip2px(getContext(),48));
        actionLp.gravity = Gravity.BOTTOM;
        inputActionView.setLayoutParams(actionLp);

        audioRecordView.setUserOperations(inputActionView);
        inputActionView.setUserOperations(audioRecordView);

        addView(inputActionView);
        addView(audioRecordView);
    }

    public void setAutoUpdateDuration(boolean autoUpdateDuration) {
        inputActionView.setAutoUpdateTime(autoUpdateDuration);
    }

    public void restartTimer() {
        inputActionView.restartTimer();
    }
    public void startTimer() {
        inputActionView.startTimer();
    }

    public void stopRecord() {
        audioRecordView.stopRecord(true, false);
    }

    public void showSendAndDeleteBar() {
        audioRecordView.showSendAndDeleteBar();
    }

    public void updateTime(long duration) {
        inputActionView.updateTime(duration);
    }

    public void setCallBack(CallBack callBack) {
        audioRecordView.setCallBack(callBack);
    }

    @Override
    public void whenActionDown() {
        audioRecordView.whenActionDown();
    }

    @Override
    public void whenActionUp() {
        audioRecordView.whenActionUp();
    }

    @Override
    public void whenLongClickDown(float x, float y) {
        audioRecordView.whenLongClickDown(x, y);
    }

    @Override
    public void whenLongClickUp(MotionEvent event) {
        audioRecordView.whenLongClickUp(event);
    }

    @Override
    public void whenPressAndMove(MotionEvent event) {
        audioRecordView.whenPressAndMove(event);
    }

    public interface CallBack {
        void whenStartRecord();

        void whenStopRecord(boolean needSend);

        void whenCancelRecord();

        void whenAbandonedVoice();

        void whenSendClick();

        void whenActionDown();

        void whenActionUp();
    }

    public boolean isRecording() {
        return audioRecordView.isRecording();
    }

}
