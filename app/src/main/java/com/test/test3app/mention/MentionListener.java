package com.test.test3app.mention;

/**
 * created by zhaoyuntao
 * on 2020/7/15
 * description:
 */
public abstract class MentionListener {
    private boolean keepSearch;

    public abstract void onSearchPeople(String contentAfterReferenceSymbol);

    public abstract void onStartMentionPeople();

    public abstract void onStopMentionPeople();

    public abstract void onReferenceRemoved(String hid);

    final boolean isKeepSearch() {
        return keepSearch;
    }

    final public void setKeepSearch(boolean keepSearch) {
        this.keepSearch = keepSearch;
    }
}
