package im.turbo.basetools.observer;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * created by zhaoyuntao
 * on 27/04/2022
 * description:
 */
public class ObjectHolder<O> {
    String tag;
    private O obj;
    private WeakReference<O> holder;

    public ObjectHolder(@NonNull O ojb, @NonNull String tag, boolean weakReference) {
        this.tag = tag;
        if (weakReference) {
            holder = new WeakReference<>(ojb);
        } else {
            this.obj = ojb;
        }
    }

    public O getObject() {
        return obj != null ? obj : holder.get();
    }
}
