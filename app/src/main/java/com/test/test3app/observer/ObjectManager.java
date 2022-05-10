package com.test.test3app.observer;

import android.os.SystemClock;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhaoyuntao.androidutils.tools.S;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author zhaoyuntao
 * date 2021/6/28
 */
public class ObjectManager<T> {
    private static final boolean DEV_LOG = false;
    private final boolean weakReference;
    private final Map<String, ObjectHolder<T>> allObjects = new LinkedHashMap<>();

    public ObjectManager(boolean weakReference) {
        this.weakReference = weakReference;
    }

    public boolean addObject(@Nullable T obj) {
        if (obj == null) {
            return false;
        }
        return addObject(String.valueOf(obj.hashCode()), obj);
    }

    public boolean addObject(@NonNull String tag, @Nullable T obj) {
        if (obj == null || TextUtils.isEmpty(tag)) {
            return false;
        }
        boolean notContains;
        synchronized (allObjects) {
            notContains = allObjects.put(tag, new ObjectHolder<>(obj, tag, weakReference)) == null;
        }
        onObjectAdded(obj);
        return notContains;
    }

    protected void onObjectAdded(@NonNull T obj) {
        // nothing to do
    }

    public void removeObject(@Nullable T obj) {
        if (obj == null) {
            return;
        }
        removeObject(String.valueOf(obj.hashCode()));
    }

    public void removeObject(@NonNull String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        ObjectHolder<T> objectInfo;
        synchronized (allObjects) {
            objectInfo = allObjects.remove(tag);
        }
        if (objectInfo != null) {
            onObjectRemoved(objectInfo.getObject());
        }
    }

    protected void onObjectRemoved(@NonNull T object) {
        // nothing to do
    }

    public void clear() {
        synchronized (allObjects) {
            allObjects.clear();
        }
        onObjectClear();
    }

    protected void onObjectClear() {
        // nothing to do
    }

    public boolean notifyObjects(@NonNull BreakNotifyAction<T> action) {
        return _notifyObjects(null, action) != null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <TT extends T> TT pickObject(Class<TT> clazz) {
        return (TT) _notifyObjects(null, clazz::isInstance);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <TT extends T> TT pickObject(String tag) {
        return (TT) getObject(tag);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public T getObject(String tag) {
        synchronized (allObjects) {
            ObjectHolder<T> objectInfo = allObjects.get(tag);
            return objectInfo == null ? null : objectInfo.getObject();
        }
    }

    public void notifyObjects(@NonNull NotifyAction<T> action) {
        _notifyObjects(action, null);
    }

    private T _notifyObjects(@Nullable NotifyAction<T> action, @Nullable BreakNotifyAction<T> breadAction) {
        // The object may unregister itself!
        Map<String, ObjectHolder<T>> objectsCopied;
        synchronized (allObjects) {
            objectsCopied = new LinkedHashMap<>(allObjects);
        }
        for (Map.Entry<String, ObjectHolder<T>> objectInfoEntry : objectsCopied.entrySet()) {
            ObjectHolder<T> objectInfo = objectInfoEntry.getValue();
            String key = objectInfoEntry.getKey();
            T obj = objectInfo.getObject();
            if (obj == null) {
                S.e(LogTag.TAG_BASE_MISC + "object leak found: " + objectInfo.tag);
                synchronized (allObjects) {
                    allObjects.remove(key);
                }
            } else {
                if (DEV_LOG) {
                    S.s(LogTag.TAG_BASE_MISC + "notify " + objectInfo.tag);
                }
                long timeStart = SystemClock.elapsedRealtime();
                //Don't do time-consuming things here, or notifier could be blocked.
                if (action != null) {
                    action.notify(obj);
                } else if (breadAction != null) {
                    if (breadAction.notifyIfNeedBreak(obj)) {
                        return obj;
                    }
                }
                long timeEnd = SystemClock.elapsedRealtime();
                if ((timeEnd - timeStart) > 2000) {
                    S.ed(LogTag.TAG_LOG_ERROR + LogTag.TAG_THREAD + "ObjectManager.Notify Error:some thing blocked the notifier:" + obj);
                }
            }
        }
        if (DEV_LOG) {
            S.s(LogTag.TAG_BASE_MISC + " notify done, cur size:" + size());
        }
        return null;
    }

    public int size() {
        synchronized (allObjects) {
            return allObjects.size();
        }
    }

    public boolean isListenerEmpty() {
        synchronized (allObjects) {
            return allObjects.isEmpty();
        }
    }
}
