package com.test.test3app.threadpool;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * created by zhaoyuntao
 * on 2019-10-27
 * description:
 */
public abstract class ZRunnable implements Runnable {

    private WeakReference<Object> objectWeakReference;

    public ZRunnable(Object t) {
        this.objectWeakReference = new WeakReference<>(t);
    }

    protected boolean judgement() {
        Object t = objectWeakReference.get();
        if (t == null) {
            return false;
        }
        if (t instanceof Activity) {
            Activity activity = (Activity) t;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return false;
            }
        } else if (t instanceof Fragment) {
            Fragment fragment = (Fragment) t;
            if (fragment.isRemoving()) {
                return false;
            }
            if (fragment.isDetached() || !fragment.isAdded()) {
                return false;
            } else {
                Activity activity = fragment.getActivity();
                if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                    return false;
                }
            }
        } else if (t instanceof android.app.Fragment) {
            android.app.Fragment fragment = (android.app.Fragment) t;
            if (fragment.isRemoving()) {
                return false;
            }
            if (fragment.isDetached() || !fragment.isAdded()) {
                return false;
            } else {
                Activity activity = fragment.getActivity();
                if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                    return false;
                }
            }
        } else if (t instanceof Context) {
            Context context = (Context) t;
            return (context.getResources() != null);
        }
        else if (t instanceof View) {
            View view = (View) t;
            return view.isAttachedToWindow();
        }
        return true;
    }

    protected abstract void todo();

    protected Object get(){
        return objectWeakReference.get();
    }

    @Override
    public void run() {
        if (judgement()) {
            todo();
        }
    }

}

