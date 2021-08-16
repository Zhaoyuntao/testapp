package com.test.test3app.stickerreply;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by zhaoyuntao
 * on 2020/7/6
 * description:
 */
public class BlueRecyclerViewItemCache<T extends Selectable<String>> {
    private List<T> data;
    private Map<String, T> map;
    private final Object lock = new Object();

    public BlueRecyclerViewItemCache() {
        data = new ArrayList<>();
        map = new HashMap<>();
    }

    public BlueRecyclerViewItemCache(List<T> data) {
        this();
        setData(data);
    }

    /**
     * Set data list of all items,it will clear selected status of all items.
     *
     * @param data
     */
    public void addAllData(List<T> data) {
        synchronized (lock) {
            if (data == null) {
                return;
            }
            this.data.addAll(data);
            for (T t : this.data) {
                map.put(t.getUniqueIdentificationId(), t);
            }
        }
    }

    /**
     * Set data list of all items,it will clear selected status of all items.
     *
     * @param data
     */
    public void setData(List<T> data) {
        synchronized (lock) {
            if (data == null) {
                return;
            }
            this.data = data;
            for (T t : data) {
                map.put(t.getUniqueIdentificationId(), t);
            }
        }
    }

    /**
     * Get data list of all items.
     */
    public List<T> getData() {
        synchronized (lock) {
            if (data == null) {
                return null;
            }
            return new ArrayList<>(data);
        }
    }

    /**
     * Add a item.
     *
     * @param t
     */
    public int addData(T t) {
        synchronized (lock) {
            if (data == null) {
                throw new RuntimeException("BlueRecyclerViewItemCache.addData to a null list");
            }
            int position = data.size();
            data.add(t);
            map.put(t.getUniqueIdentificationId(), t);
            return position;
        }
    }

    /**
     * Add a item on position.
     *
     * @param position
     * @param t
     */
    public void addData(int position, T t) {
        synchronized (lock) {
            if (data == null) {
                throw new RuntimeException("BlueRecyclerViewItemCache.addData to a null list");
            }
            data.add(position, t);
            map.put(t.getUniqueIdentificationId(), t);
        }
    }

    /**
     * Replace old item with new item.
     *
     * @param t
     * @return old Item.
     */
    public T replaceData(T t) {
        synchronized (lock) {
            if (data == null) {
                throw new RuntimeException("BlueRecyclerViewItemCache.replaceData to a null list");
            }
            int position = getPosition(t);
            if (position == -1) {
                return null;
            }
            T old = data.remove(position);
            data.add(position, t);
            map.put(t.getUniqueIdentificationId(), t);
            return old;
        }
    }

    /**
     * Get the item position.
     *
     * @param o
     */
    public int getPosition(String o) {
        synchronized (lock) {
            if (o == null || data == null) {
                return -1;
            }
            T t = map.get(o);
            if (t != null) {
                return data.indexOf(t);
            }
            return -1;
        }
    }

    /**
     * Get the item position.
     *
     * @param t
     */
    public int getPosition(T t) {
        synchronized (lock) {
            if (t == null || data == null) {
                return -1;
            }
            return data.indexOf(t);
        }
    }

    public T get(String key) {
        synchronized (lock) {
            if (TextUtils.isEmpty(key) || map == null) {
                return null;
            }
            return map.get(key);
        }
    }

    public T get(int position) {
        synchronized (lock) {
            if (position < 0 || data == null) {
                return null;
            }
            return data.get(position);
        }
    }

    /**
     * Remove item by position.
     *
     * @param position
     * @return
     */
    public T remove(int position) {
        synchronized (lock) {
            if (data == null || position < 0 || position >= data.size()) {
                return null;
            }
            T t = data.remove(position);
            if (t != null) {
                return map.remove(t.getUniqueIdentificationId());
            } else {
                return null;
            }
        }
    }

    /**
     * Remove item by unique key.
     *
     * @param o
     * @return
     */
    public int remove(String o) {
        synchronized (lock) {
            if (data == null || o == null) {
                return -1;
            }
            int position = getPosition(o);
            if (position == -1) {
                return -1;
            }
            remove(position);
            return position;
        }
    }

    /**
     * Remove item.
     *
     * @param t
     * @return
     */
    public int remove(T t) {
        synchronized (lock) {
            if (data == null || t == null) {
                return -1;
            }
            int position = getPosition(t);
            if (position == -1) {
                return -1;
            }
            remove(position);
            return position;
        }
    }

    /**
     * Remove item.
     */
    public void clear() {
        synchronized (lock) {
            if (data == null || map == null) {
                return;
            }
            data.clear();
            map.clear();
        }
    }

    public int size() {
        synchronized (lock) {
            return data.size();
        }
    }
}
