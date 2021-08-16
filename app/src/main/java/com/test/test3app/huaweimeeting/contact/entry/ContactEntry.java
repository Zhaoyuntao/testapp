package com.test.test3app.huaweimeeting.contact.entry;

/**
 * created by zhaoyuntao
 * on 2020-03-17
 * description:
 */
public class ContactEntry extends ContactBaseEntry {

    public ContactEntry(String name) {
        super(name);
    }

    public int getType() {
        return TYPE_ITEM;
    }
}
