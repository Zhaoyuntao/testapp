package com.test.test3app.textview;

import im.turbo.utils.log.S;
import com.zhaoyuntao.androidutils.tools.TextMeasure;

/**
 * created by zhaoyuntao
 * on 2020-04-08
 * description:
 */
public class MyCharIterator implements ZTestTextView.CharIterator {
    private String lastString;
    private final StringBuilder stringBuilder = new StringBuilder();
    private float textSize;
    private int widthOfView;
    private boolean needEmplisize;

    public MyCharIterator(float textSize, int widthOfView) {
        this.textSize = textSize;
        this.widthOfView = widthOfView;
    }

    @Override
    public boolean getCharString(CharSequence item, int codePoint) {
        stringBuilder.append(item);
        String measureString = stringBuilder.toString();
        if (lastString == null) {
            lastString = measureString;
        }
        float widthOfText = TextMeasure.measure(measureString + "\u2026", textSize)[0];
        if (widthOfText > widthOfView) {
            needEmplisize = true;
            S.s("reach:[" + lastString + "] measureString:[" + measureString + "]width:" + widthOfText + " wView:" + widthOfView);
            return false;
        } else {
            lastString = measureString;
            S.e("not reach:[" + lastString + "] measureString:[" + measureString + "]width:" + widthOfText + " wView:" + widthOfView);
            return true;
        }
    }

    public String getLastString() {
        return lastString;
    }

    public boolean isNeedEmplisize() {
        return needEmplisize;
    }
}
