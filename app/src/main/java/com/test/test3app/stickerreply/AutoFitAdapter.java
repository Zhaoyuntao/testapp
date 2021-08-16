package com.test.test3app.stickerreply;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.zhaoyuntao.androidutils.tools.S;

import java.util.List;

/**
 * created by zhaoyuntao
 * on 30/12/2020
 * description:
 */
abstract class AutoFitAdapter<T extends Selectable<String>> {
    private final BlueRecyclerViewItemCache<T> cache;
    private AutoFixCallback autoFixCallback;

    public AutoFitAdapter() {
        cache = new BlueRecyclerViewItemCache<>();
    }

    public void initData(@NonNull List<T> data) {
        cache.clear();
        cache.setData(data);
        for (int i = 0; i < cache.size(); i++) {
            View view = onCreateView(autoFixCallback.onGetParent(), i);
            onBindViewData(autoFixCallback.onGetParent(), view, i, cache.get(i));
            autoFixCallback.onAddView(view);
        }
    }

    public void addData(@NonNull T t) {
        int position = cache.addData(t);
        View view = onCreateView(autoFixCallback.onGetParent(), position);
        onBindViewData(autoFixCallback.onGetParent(), view, position, cache.get(position));
        autoFixCallback.onAddView(view);
    }

    public void addData(@NonNull T t, int position) {
        cache.addData(position, t);
        View view = onCreateView(autoFixCallback.onGetParent(), position);
        onBindViewData(autoFixCallback.onGetParent(), view, position, cache.get(position));
        autoFixCallback.onAddView(position, view);
    }

    public void removeData(@NonNull T t) {
        int position = cache.remove(t);
        if (position < 0) {
            return;
        }
        if (autoFixCallback.onGetChildCount() > position) {
            autoFixCallback.onRemoveView(position);
        }
    }

    public void removeData(int position) {
        S.s("removeData:" + position);
        if (positionOverStack(position)) {
            return;
        }
        cache.remove(position);
        if (autoFixCallback.onGetChildCount() > position) {
            autoFixCallback.onRemoveView(position);
        }
    }

    public void changeItemPosition(int fromPosition, int toPosition) {
        T t = cache.get(fromPosition);
        if (t == null) {
            return;
        }
        cache.addData(toPosition, t);
        cache.remove(fromPosition);
        autoFixCallback.onChangeViewPosition(fromPosition, toPosition);
    }

    public void notifyItemChanged(int position) {
        notifyItemChanged(position, null);
    }

    public void notifyItemChanged(int position, Bundle payload) {
        if (positionOverStack(position)) {
            return;
        }
        View view = autoFixCallback.onGetView(position);
        T t = cache.get(position);
        if (payload == null) {
            onBindViewData(autoFixCallback.onGetParent(), view, position, t);
        } else {
            onChangeViewData(autoFixCallback.onGetParent(), view, position, t, payload);
        }
    }

    public void notifyDataSetChanged() {
        autoFixCallback.onRemoveAllViews();
        for (int i = 0; i < cache.size(); i++) {
            View view = onCreateView(autoFixCallback.onGetParent(), i);
            onBindViewData(autoFixCallback.onGetParent(), view, i, cache.get(i));
            autoFixCallback.onAddView(view);
        }
    }

    public void clearAll() {
        autoFixCallback.onRemoveAllViews();
        cache.clear();
    }

    @NonNull
    public abstract View onCreateView(@NonNull ViewGroup parent, int position);

    public abstract void onBindViewData(@NonNull ViewGroup parent, @NonNull View child, int position, @NonNull T t);

    public abstract void onChangeViewData(@NonNull ViewGroup parent, @NonNull View child, int position, @NonNull T t, @NonNull Bundle payload);

    private boolean positionOverStack(int position) {
        return position >= cache.size() || position >= autoFixCallback.onGetChildCount();
    }

    public void setAutoFixCallback(AutoFixCallback autoFixCallback) {
        this.autoFixCallback = autoFixCallback;
    }

    public T getData(String key) {
        return cache.get(key);
    }

    public T getData(int position) {
        return cache.get(position);
    }

    public int getDataPosition(T t) {
        return cache.getPosition(t);
    }

    interface AutoFixCallback {
        void onAddView(View view);

        void onAddView(int position, View view);

        void onRemoveView(int position);

        View onGetView(int position);

        int onGetChildCount();

        ViewGroup onGetParent();

        void onRemoveAllViews();

        void onChangeViewPosition(int fromPosition, int toPosition);
    }
}
