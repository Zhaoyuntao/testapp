package com.test.test3app.expandrecyclerview;

import java.util.ArrayList;
import java.util.List;


/**
 * created by zhaoyuntao
 * on 2020-03-16
 * description:
 */
public class ExpandableEntry<A> {
    private boolean expand;
    private boolean isParent = true;
    private boolean expandable = true;
    private List<ExpandableEntry> list = new ArrayList<>();

    private A a;

    public <B extends A> ExpandableEntry(B b) {
        this.a = b;
    }

    boolean isParent() {
        return isParent;
    }

    boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    int size() {
        return list.size();
    }

    List<ExpandableEntry> getAll() {
        List<ExpandableEntry> list = new ArrayList<>();
        list.add(this);
        if (expand) {
            list.addAll(this.list);
        }
        return list;
    }

    ExpandableEntry getChildAt(int i) {
        return list.get(i);
    }

    A getParamEntry() {
        return a;
    }

    public <B> void addChildEntry(B a) {
        ExpandableEntry<B> expandableEntry = new ExpandableEntry<>(a);
        list.add(expandableEntry);
        this.isParent = true;
        expandableEntry.isParent = false;
    }
}
