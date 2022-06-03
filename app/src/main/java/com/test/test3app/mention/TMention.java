package com.test.test3app.mention;

import java.util.List;

/**
 * created by zhaoyuntao
 * on 03/06/2022
 * description:
 */
public class TMention {
    private final List<String> uidList;
    private final CharSequence text;

    public TMention(CharSequence text, List<String> uidList) {
        this.text = text;
        this.uidList = uidList;
    }

    public List<String> getUidList() {
        return uidList;
    }

    public CharSequence getText() {
        return text;
    }
}
