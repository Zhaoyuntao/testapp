package com.test.test3app.glide;

import android.text.TextUtils;

/**
 * created by zhaoyuntao
 * on 25/05/2022
 * description:
 */
public class ChatImageModule {
    private final String sessionId;
    private final String uuid;
    private String localPath;

    public ChatImageModule(String sessionId, String uuid) {
        this.sessionId = sessionId;
        this.uuid = uuid;
    }

    public boolean needLoad() {
        boolean needLoad = TextUtils.isEmpty(localPath);
        return needLoad;
    }

    public String getKey() {
        return sessionId + uuid;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getSessionId() {
        return sessionId;
    }
}
