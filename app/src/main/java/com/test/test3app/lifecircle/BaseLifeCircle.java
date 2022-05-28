package com.test.test3app.lifecircle;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * created by zhaoyuntao
 * on 2020-05-27
 * description:
 */
public abstract class BaseLifeCircle {

    private WeakReference<Object> lifeCircleWeakReference;

    public BaseLifeCircle(android.app.Activity lifeCircle) {
        init(lifeCircle);
    }

    public BaseLifeCircle(androidx.fragment.app.Fragment lifeCircle) {
        init(lifeCircle);
    }

    public BaseLifeCircle(android.app.Fragment lifeCircle) {
        init(lifeCircle);
    }

    public BaseLifeCircle(Context lifeCircle) {
        init(lifeCircle);
    }

    public BaseLifeCircle(android.view.View lifeCircle) {
        init(lifeCircle);
    }

    public BaseLifeCircle(android.app.Application application) {
        init(application);
    }

    private <T> void init(T lifeCircle) {
        this.lifeCircleWeakReference = new WeakReference<>(lifeCircle);
    }

    final public boolean checkLifeCircle() {
        return LifeCycleHelper.checkLifeCircle(lifeCircleWeakReference.get());
    }

    public Object get() {
        if (lifeCircleWeakReference == null) {
            return null;
        }
        return lifeCircleWeakReference.get();
    }

    public void clear() {
        if (lifeCircleWeakReference != null) {
            lifeCircleWeakReference.clear();
        }
    }
}
