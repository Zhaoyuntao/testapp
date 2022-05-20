package com.test;

import java.lang.ref.WeakReference;

/**
 * created by zhaoyuntao
 * on 18/05/2022
 * description:
 */
class ObjectHolder {
    private WeakReference<Activityc> weakReference;
    private Activityc activity;

    ObjectHolder(Activityc activity, boolean isWeak) {
        if (isWeak) {

            this.weakReference = new WeakReference<>(activity);
        } else {
            this.activity = activity;
        }
    }

    Activityc get() {
        return weakReference == null ? activity : weakReference.get();
    }

}
