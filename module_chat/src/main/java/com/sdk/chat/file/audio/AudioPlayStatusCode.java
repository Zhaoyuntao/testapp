package com.sdk.chat.file.audio;

import androidx.annotation.IntDef;

//file and picture
@IntDef({
        AudioPlayStatusCode.STATUS_AUDIO_NOT_PLAYING,
        AudioPlayStatusCode.STATUS_AUDIO_START,
        AudioPlayStatusCode.STATUS_AUDIO_PLAYING,
        AudioPlayStatusCode.STATUS_AUDIO_PAUSED
})
public @interface AudioPlayStatusCode {
    int STATUS_AUDIO_NOT_PLAYING = 0;
    int STATUS_AUDIO_START = 1;
    int STATUS_AUDIO_PLAYING = 2;
    int STATUS_AUDIO_PAUSED = 3;
}
