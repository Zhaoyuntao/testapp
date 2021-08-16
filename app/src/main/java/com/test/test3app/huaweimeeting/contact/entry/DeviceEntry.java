package com.test.test3app.huaweimeeting.contact.entry;

/**
 * created by zhaoyuntao
 * on 2020-03-17
 * description:
 */
public class DeviceEntry extends ContactBaseEntry {

    public DeviceEntry(String name) {
        super(name);
    }

    public int getType() {
        return TYPE_DEVICE;
    }
}
