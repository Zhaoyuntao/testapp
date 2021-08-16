package com.test.test3app.huaweimeeting.contact.entry;

/**
 * created by zhaoyuntao
 * on 2020-03-17
 * description:
 */
public abstract class ContactBaseEntry {
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_DEVICE = 2;
    private String name;

    public ContactBaseEntry(String name) {
        setName(name);
    }

    public abstract int getType();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
