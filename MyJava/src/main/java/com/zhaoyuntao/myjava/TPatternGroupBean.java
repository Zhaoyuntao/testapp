package com.zhaoyuntao.myjava;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 2020/8/25
 * description:
 */
public class TPatternGroupBean {
    private List<TPatternGroupItemBean> groupItemBeans;
    private int start;
    private int end;
    private int index;
    private String content;
    private String patternString;

    public TPatternGroupBean(int groupCount) {
        groupItemBeans = new ArrayList<>(groupCount);
    }

    public List<TPatternGroupItemBean> getGroupItemBeans() {
        return groupItemBeans;
    }

    public void addItem(TPatternGroupItemBean TPatternGroupItemBean) {
        this.groupItemBeans.add(TPatternGroupItemBean);
    }

    public void clearItems() {
        this.groupItemBeans.clear();
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPatternString() {
        return patternString;
    }

    public void setPatternString(String patternString) {
        this.patternString = patternString;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int length() {
        if (content == null) {
            return 0;
        }
        return content.length();
    }

    @Override
    public String toString() {
        return "\nGroup[" + content + "]  [start:" + start + "][end:" + end + "][index:" + index + "][" + patternString + "]"
                + "\n<\n"
                + "     " + groupItemBeans
                + "\n>\n";
    }
}
