package com.test.test3app.windowtransition;

import com.test.test3app.wallpaper.Selectable;

import java.util.Objects;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
public class ImageBean implements Selectable<String> {
    public String id;
    public int resId;

    public ImageBean(String id) {
        this.id = id;
    }

    public ImageBean(String id, int resId) {
        this.id = id;
        this.resId = resId;
    }

    public ImageBean() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageBean)) return false;
        ImageBean that = (ImageBean) o;
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
