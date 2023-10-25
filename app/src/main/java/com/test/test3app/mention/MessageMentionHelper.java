package com.test.test3app.mention;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.test.test3app.BuildConfig;
import im.turbo.utils.log.S;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * created by zhaoyuntao
 * on 11/04/2022
 * description:
 */
public class MessageMentionHelper {
    public static final char FSI = '\u2068';
    public static final char SPACE = ' ';
    public static final String MENTION = "@";

    public static String getMentionNameContent(CharSequence name) {
        return MENTION + FSI + name + FSI;
    }

    public static String getMentionUidContent(String uid) {
        return MENTION + FSI + uid + FSI;
    }
}
