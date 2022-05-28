package com.test.test3app.lifecircle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class LifeCycleHelper {
    public static boolean checkLifeCircle(@Nullable Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof AppCompatActivity) {
            return LifeCycleHelper.checkLifeCircle((AppCompatActivity) o);
        }
        if (o instanceof Activity) {
            return LifeCycleHelper.checkLifeCircle((Activity) o);
        }
        if (o instanceof Context) {
            return LifeCycleHelper.checkLifeCircle((Context) o);
        }
        if (o instanceof android.app.Fragment) {
            return LifeCycleHelper.checkLifeCircle((android.app.Fragment) o);
        }
        if (o instanceof Fragment) {
            return LifeCycleHelper.checkLifeCircle((Fragment) o);
        }
        if (o instanceof View) {
            return LifeCycleHelper.checkLifeCircle((View) o);
        }
        return false;
    }

    public static boolean checkLifeCircle(@Nullable Fragment fragment) {
        if (fragment == null || fragment.isRemoving() || fragment.isDetached() || !fragment.isAdded()) {
            return false;
        }
        return checkLifeCircle(fragment.getActivity());
    }

    public static boolean checkLifeCircle(@Nullable android.app.Fragment fragment) {
        if (fragment == null || fragment.isRemoving() || fragment.isDetached() || !fragment.isAdded()) {
            return false;
        }
        return checkLifeCircle(fragment.getActivity());
    }

    public static boolean checkLifeCircle(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Application) {
            return true;
        }
        Activity activity = findActivity(context);
        return checkLifeCircle(activity);
    }

    public static boolean checkLifeCircle(@Nullable View view) {
        if (view == null || view.getContext() == null) {
            return false;
        }
        Activity activity = findActivity(view.getContext());
        return checkLifeCircle(activity);
    }

    public static boolean checkLifeCircle(@Nullable Activity activity) {
        return activity != null && !activity.isFinishing() && !activity.isDestroyed();
    }

    private static Activity findActivity(@NonNull Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return findActivity(((ContextWrapper) context).getBaseContext());
        } else {
            return null;
        }
    }

    @Nullable
    public static Activity findActivity(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof AppCompatActivity) {
            return (AppCompatActivity) obj;
        } else if (obj instanceof Activity) {
            return (Activity) obj;
        } else if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            return (fragment.isRemoving() || fragment.isDetached() || !fragment.isAdded()) ? null : fragment.getActivity();
        } else if (obj instanceof android.app.Fragment) {
            return ((android.app.Fragment) obj).getActivity();
        } else if (obj instanceof View) {
            return findActivity(((View) obj).getContext());
        } else if (obj instanceof Context) {
            return findActivity((Context) obj);
        }
        return null;
    }
}
