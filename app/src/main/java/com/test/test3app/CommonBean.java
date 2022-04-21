package com.test.test3app;

import com.test.test3app.wallpaper.Selectable;

import java.util.Objects;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
public class CommonBean implements Selectable<String> {
    public String id;
    public int resId;

    public CommonBean(String id) {
        this.id = id;
    }

    public CommonBean(String id, int resId) {
        this.id = id;
        this.resId = resId;
    }

    public CommonBean() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommonBean)) return false;
        CommonBean that = (CommonBean) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String getUniqueIdentificationId() {
        return id;
    }
}
