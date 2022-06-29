package com.sdk.chat.file.audio;

import androidx.annotation.IntDef;

//file and picture
@IntDef({
        AudioStatusCode.STATUS_AUDIO_NOT_PLAYING,
        AudioStatusCode.STATUS_AUDIO_START,
        AudioStatusCode.STATUS_AUDIO_PLAYING,
        AudioStatusCode.STATUS_AUDIO_PAUSED
})
public @interface AudioStatusCode {
    int STATUS_AUDIO_NOT_PLAYING = 0;
    int STATUS_AUDIO_START = 1;
    int STATUS_AUDIO_PLAYING = 2;
    int STATUS_AUDIO_PAUSED = 3;
}
