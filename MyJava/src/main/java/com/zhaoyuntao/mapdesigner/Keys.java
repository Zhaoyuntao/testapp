package com.zhaoyuntao.mapdesigner;

import java.text.SimpleDateFormat;

/**
 * Created by zhaoyuntao on 2019-4-29.
 */

public class Keys {
    private String key;
    private String did;
    private String mac;
    private String vendor = "swdk";
    private long time;
    private boolean used;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getMac() {
        return mac;
    }

    public String getMacFormat() {
        String mac=this.mac;
        mac=mac.replaceAll(" ","");
        if (mac != null && mac.length() % 2 == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < mac.length(); i += 2) {
                stringBuilder.append(mac.charAt(i));
                if ((i + 1) < mac.length()) {
                    stringBuilder.append(mac.charAt(i + 1));
                    if ((i + 1) != mac.length() - 1) {
                        stringBuilder.append(":");
                    }
                }
            }
            return stringBuilder.toString();
        } else {
            return mac;
        }
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "key:" + key + ", did:" + did + ", mac:" + mac + ", vendor:" + vendor  + ", 状态:" + (used ? "已使用" : "未使用") + ((used && time != 0) ? " 时间:" + new SimpleDateFormat("Y-M-d h:mm:ss").format(time) : "");
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
