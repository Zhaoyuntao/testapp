/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.test3app.textview_spannable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.emoji.widget.EmojiTextViewHelper;

import com.test.test3app.R;
import im.turbo.utils.ResourceUtils;
import im.turbo.basetools.utils.RTLUtils;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlueTextView extends AppCompatTextView {

    private boolean rtlSupport;

    private UrlSpanClickListener mUrlSpanClickListener;

    private long mTouchDownloadTime;
    private long CLICK_GAP = ViewConfiguration.getLongPressTimeout();

    private List<Pair<Integer, Integer>> mList;

    private EmojiTextViewHelper mEmojiTextViewHelper;

    public void setHighLight(List<Pair<Integer, Integer>> list) {
        mList = list;
    }

    public interface OnLayoutListener {
        void onLayouted(BlueTextView view, boolean changed, int left, int top, int right, int bottom);
    }

    public interface UrlSpanClickListener {
        void onClick(View view, String url, String content);
    }

    public boolean isRtlSupport() {
        return rtlSupport;
    }

    public void setRtlSupport(boolean rtlSupport) {
        this.rtlSupport = rtlSupport;
    }

    private static class UrlClickableSpan extends ClickableSpan {

        private String mUrl;
        private String mContent;
        UrlSpanClickListener mClickListener;

        UrlClickableSpan(String url, String content, UrlSpanClickListener listener) {
            mUrl = url;
            mContent = content;
            mClickListener = listener;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            int color = Color.argb(255, 0, 94, 222);
            ds.setColor(color);
            ds.linkColor = color;
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }

        @Override
        public void onClick(View widget) {
            if (mClickListener != null) {
                mClickListener.onClick(widget, mUrl, mContent);
            }
        }
    }

    public BlueTextView(Context context) {
        super(context);
        init(null);
    }

    public BlueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BlueTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BlueTextView);
            rtlSupport = a.getBoolean(R.styleable.BlueTextView_rtlSupport, false);
            a.recycle();
        }
        setText(getText());
    }

    private void init() {
        getEmojiTextViewHelper().updateTransformationMethod();
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        super.setFilters(getEmojiTextViewHelper().getFilters(filters));
    }

    @Override
    public void setAllCaps(boolean allCaps) {
        super.setAllCaps(allCaps);
        getEmojiTextViewHelper().setAllCaps(allCaps);
    }

    private EmojiTextViewHelper getEmojiTextViewHelper() {
        if (mEmojiTextViewHelper == null) {
            mEmojiTextViewHelper = new EmojiTextViewHelper(this);
        }
        return mEmojiTextViewHelper;
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text == null || text.length() == 0) {
            super.setText(text, type);
            return;
        }

        final int autoLinkMask = getAutoLinkMask();
//        final boolean linkMaskChanged = autoLinkMask != 0;
//        if (linkMaskChanged) {
            setAutoLinkMask(0);
//        }

        //搜索高亮显示
        SpannableStringBuilder spannableString = new SpannableStringBuilder(text);
        if (mList != null && !mList.isEmpty()) {
            int color = ResourceUtils.getApplicationContext().getResources().getColor(R.color.yc_color_FFFA00);
            spannableString.setSpan(new ForegroundColorSpan(Color.RED),
                    0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            for (int i = 0; i < mList.size(); i++) {
                int indexStart = mList.get(i).first;
                int indexEnd = mList.get(i).second;
                spannableString.setSpan(new BackgroundColorSpan(color), indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        super.setText(spannableString, type);
        attachSpanClick(type);

//        if (linkMaskChanged) {
//            setAutoLinkMask(autoLinkMask);
//        }
        setAutoLinkMask(Linkify.PHONE_NUMBERS);
        rtlSupport(text);
        setMovementMethod(LinkMovementMethod.getInstance());
        setHighlightColor(Color.TRANSPARENT);

    }

    /**
     * 左右转换支持
     *
     * @param text 输入的文本
     */
    private void rtlSupport(CharSequence text) {
        if (rtlSupport) {
            setTextDirection(RTLUtils.getStringDirection(getContext(), text) == RTLUtils.RTL ? TEXT_DIRECTION_RTL : TEXT_DIRECTION_LTR);
        }
    }

    private void attachSpanClick(BufferType type) {
        if (mUrlSpanClickListener != null) {
            CharSequence content = getText();
            SpannableStringBuilder sp = new SpannableStringBuilder(content);
            URLSpan[] urls = sp.getSpans(0, sp.length(), URLSpan.class);
            for (URLSpan url : urls) {
                CharSequence cs = "";
                if (sp.getSpanStart(url) < sp.getSpanEnd(url)) {
                    cs = sp.subSequence(sp.getSpanStart(url), sp.getSpanEnd(url));
                    UrlClickableSpan myURLSpan = new UrlClickableSpan(url.getURL(), cs.toString(), mUrlSpanClickListener);
                    sp.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            String contentStr = content.toString();
            List<String> list = matchURL(contentStr);
            if (list != null) {
                for (String temp : list) {
                    int index = contentStr.indexOf(temp);
                    UrlClickableSpan urlSpan = new UrlClickableSpan(temp, temp, mUrlSpanClickListener);
                    sp.setSpan(urlSpan, index, index + temp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                }
            }
            super.setText(sp, type);
        }
    }


    public List<String> matchURL(String content) {
        List<String> termList = new ArrayList<String>();
        S.s("content:" + content);

        Matcher matcher = WEB_URL.matcher(content);

        while (matcher.find()) {
            termList.add(matcher.group());
        }

        return termList;
    }

    public void setUrlSpanClickListener(UrlSpanClickListener listener) {
        mUrlSpanClickListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP) {
                if (SystemClock.uptimeMillis() - mTouchDownloadTime > CLICK_GAP) {
                    return true;
                }
            } else if (action == MotionEvent.ACTION_DOWN) {
                mTouchDownloadTime = SystemClock.uptimeMillis();
            }
            return super.onTouchEvent(event);
        } catch (Exception e) {
            return false;
        }
    }

    //


    // all domain names
    private static final String[] ext = {
            "top", "com.cn", "com", "net", "org", "edu", "gov", "int", "mil", "cn", "tel", "biz", "cc", "tv", "info",
            "name", "hk", "mobi", "asia", "cd", "travel", "pro", "museum", "coop", "aero", "ad", "ae", "af",
            "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "az", "ba", "bb", "bd",
            "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz",
            "ca", "cc", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cq", "cr", "cu", "cv", "cx",
            "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "es", "et", "ev", "fi",
            "fj", "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gh", "gi", "gl", "gm", "gn", "gp",
            "gr", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "in", "io",
            "iq", "ir", "is", "it", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw",
            "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md",
            "mg", "mh", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mv", "mw", "mx", "my", "mz",
            "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nt", "nu", "nz", "om", "qa", "pa",
            "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "pt", "pw", "py", "re", "ro", "ru", "rw",
            "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "st",
            "su", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk", "tm", "tn", "to", "tp", "tr", "tt",
            "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "va", "vc", "ve", "vg", "vn", "vu", "wf", "ws",
            "ye", "yu", "za", "zm", "zr", "zw"
    };
    static Pattern WEB_URL;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < ext.length; i++) {
            sb.append(ext[i]);
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        // final pattern str
        String pattern = "((https?|s?ftp|irc[6s]?|git|afp|telnet|smb)://)?((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|((www\\.|[a-zA-Z\\.\\-]+\\.)?[a-zA-Z0-9\\-]+\\." + sb.toString() + "(:[0-9]{1,5})?))((/[a-zA-Z0-9\\./,;\\?'\\+&%\\$#=~_\\-]*)|([^\\u4e00-\\u9fa5\\s0-9a-zA-Z\\./,;\\?'\\+&%\\$#=~_\\-]*))";
        // Log.v(TAG, "pattern = " + pattern);
        WEB_URL = Pattern.compile(pattern);
    }
}
