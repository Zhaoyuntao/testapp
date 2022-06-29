package im.turbo.basetools.selector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by zhaoyuntao
 * on 2020/7/6
 * description:
 */
public class ListItemSelector<T extends Selectable<?>> {
    private final LinkedHashSet<T> selectedItems;
    private final Set<Object> selectItemsKeySet;
    private final Set<Object> defaultSelectedItems;
    private List<T> data;
    private final Map<Object, T> map;
    private final Map<Object, Boolean> mapSelectStateChanged;
    private final Object lock = new Object();
    private boolean lockDefaultSelection;
    private Comparator<T> comparator;

    public ListItemSelector() {
        selectedItems = new LinkedHashSet<>();
        selectItemsKeySet = new HashSet<>();
        defaultSelectedItems = new HashSet<>();
        mapSelectStateChanged = new HashMap<>();
        map = new HashMap<>();
        data = new ArrayList<>();
    }

    public ListItemSelector(List<T> data) {
        this();
        setData(data);
    }

    public ListItemSelector(List<T> data, Comparator<T> comparator) {
        this();
        this.comparator = comparator;
        setData(data);
    }

    /**
     * Select the item.
     *
     * @param t
     */
    public int select(T t) {
        synchronized (lock) {
            if (t == null || data == null) {
                return -1;
            }
            if (defaultSelectedItems.contains(t.getUniqueIdentificationId()) && lockDefaultSelection) {
                return -1;
            }
            selectedItems.add(t);
            markItemSelectedChanged(t, true);
            selectItemsKeySet.add(t.getUniqueIdentificationId());
            return data.indexOf(t);
        }
    }

    /**
     * Select the item.
     *
     * @param o
     */
    public int select(Object o) {
        synchronized (lock) {
            if (o == null || data == null) {
                return -1;
            }
            T t = map.get(o);
            return select(t);
        }
    }

    /**
     * Select the item at the specified position.
     *
     * @param position
     */
    public int select(int position) {
        synchronized (lock) {
            if (data == null || position < 0 || position >= data.size()) {
                throw new IndexOutOfBoundsException("Invalid index: " + position + " (size: " + (data == null ? "null" : data.size()) + ")");
            }
            T t = data.get(position);
            return select(t);
        }
    }

    /**
     * UnSelect the item.
     *
     * @param t
     */
    public int unSelect(T t) {
        synchronized (lock) {
            if (t == null || data == null) {
                return -1;
            }
            if (defaultSelectedItems.contains(t.getUniqueIdentificationId()) && lockDefaultSelection) {
                return -1;
            }
            defaultSelectedItems.remove(t.getUniqueIdentificationId());
            selectedItems.remove(t);
            markItemSelectedChanged(t, false);
            selectItemsKeySet.remove(t.getUniqueIdentificationId());
            return data.indexOf(t);
        }
    }

    /**
     * UnSelect the item at the specified position.
     *
     * @param position
     */
    public int unSelect(int position) {
        synchronized (lock) {
            if (data == null || position < 0 || position >= data.size()) {
                throw new IndexOutOfBoundsException("Invalid index: " + position + " (size: " + (data == null ? "null" : data.size()) + ")");
            }
            T t = data.get(position);
            return unSelect(t);
        }
    }

    /**
     * Select the item.
     *
     * @param o
     */
    public int unSelect(Object o) {
        synchronized (lock) {
            if (o == null || data == null) {
                return -1;
            }
            T t = map.get(o);
            return unSelect(t);
        }
    }

    /**
     * If the item is selected.
     *
     * @param t
     * @return
     */
    public boolean isSelected(T t) {
        synchronized (lock) {
            if (t == null || data == null) {
                return false;
            }
//            S.s("Selector.isSelected["+t.getUniqueIdentificationId()+"]:"+selectItemsKeySet.contains(t.getUniqueIdentificationId()));
            return selectItemsKeySet.contains(t.getUniqueIdentificationId());
        }
    }

    /**
     * If the item is selected.
     *
     * @param o
     * @return
     */
    public boolean isSelected(Object o) {
        synchronized (lock) {
            if (o == null || data == null) {
                return false;
            }
//            S.s("Selector.isSelected["+o+"]:"+selectItemsKeySet.contains(o));
            return selectItemsKeySet.contains(o);
        }
    }

    /**
     * If the item at the specified position is selected.
     *
     * @param position
     * @return
     */
    public boolean isSelected(int position) {
        synchronized (lock) {
            if (data == null || position < 0 || position >= data.size()) {
                throw new IndexOutOfBoundsException("Invalid index: " + position + " (size: " + (data == null ? "null" : data.size()) + ")");
            }
            T t = data.get(position);
            return isSelected(t);
        }
    }

    /**
     * Set the select status to opposite value of item at specified position.
     *
     * @param position
     * @return If the item is finally selected.
     */
    public boolean changeSelect(int position) {
        synchronized (lock) {
            if (data == null || position < 0 || position >= data.size()) {
                throw new IndexOutOfBoundsException("Invalid index: " + position + " (size: " + (data == null ? "null" : data.size()) + ")");
            }
            T t = data.get(position);
            return changeSelect(t);
        }
    }

    /**
     * Set the select status to opposite value of item by it's unique key.
     *
     * @param o
     * @return If the item is finally selected.
     */
    public boolean changeSelect(Object o) {
        synchronized (lock) {
            if (o == null || data == null) {
                throw new IndexOutOfBoundsException("Invalid id: " + o);
            }
            T t = map.get(o);
            return changeSelect(t);
        }
    }

    /**
     * Set the select status to opposite value of item by it's unique key.
     *
     * @param list
     * @return If the item is finally selected.
     */
    public void changeSelect(List<T> list) {
        synchronized (lock) {
            if (list == null || data == null) {
                throw new IndexOutOfBoundsException("Invalid list: " + list);
            }
            for (T t : list) {
                changeSelect(t);
            }
        }
    }

    /**
     * Set the select status to opposite value of item by it's unique key.
     *
     * @param list
     * @return If the item is finally selected.
     */
    public void changeSelect(List<T> list, boolean select) {
        synchronized (lock) {
            if (list == null || data == null) {
                throw new IndexOutOfBoundsException("Invalid list: " + list);
            }
            for (T t : list) {
                if (select) {
                    select(t);
                } else {
                    unSelect(t);
                }
            }
        }
    }

    /**
     * Set the select status to opposite value of item.
     *
     * @param t
     * @return If the item is finally selected.
     */
    public boolean changeSelect(T t) {
        if (isSelected(t)) {
            unSelect(t);
            return false;
        } else {
            select(t);
            return true;
        }
    }

    /**
     * Set data list of all items,it will clear selected status of all items.
     *
     * @param data
     */
    public void addAllData(List<T> data) {
        addAllData(data, null, false);
    }

    public void addAllData(List<T> data, @Nullable List<T> defaultSelectedItems, boolean lockedDefaultSelectedItemState) {
        synchronized (lock) {
            if (data == null) {
                return;
            }
            for (T t : data) {
                if (!this.data.contains(t)) {
                    this.data.add(t);
                }
            }
            sortItems();
            for (T t : this.data) {
                map.put(t.getUniqueIdentificationId(), t);
            }
            this.lockDefaultSelection = lockedDefaultSelectedItemState;
            initDefaultSelection(defaultSelectedItems);
        }
    }

    /**
     * Set data list of all items,it will clear selected status of all items.
     *
     * @param data
     */
    public void setData(@NonNull List<T> data) {
        setData(data, null, lockDefaultSelection);
    }

    public void setData(@NonNull List<T> data, @Nullable List<T> defaultSelectedItems, boolean lockedDefaultSelectedItemState) {
        clear(false);
        synchronized (lock) {
            this.data = new ArrayList<>(data);
            sortItems();
            for (T t : data) {
                map.put(t.getUniqueIdentificationId(), t);
            }
            this.lockDefaultSelection = lockedDefaultSelectedItemState;
            initDefaultSelection(defaultSelectedItems);
        }
    }

    private void initDefaultSelection(List<T> defaultSelectedItems) {
        if (defaultSelectedItems != null) {
            for (T t : defaultSelectedItems) {
                this.defaultSelectedItems.add(t.getUniqueIdentificationId());
                this.selectedItems.add(t);
                this.selectItemsKeySet.add(t.getUniqueIdentificationId());
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
            return replaceData(getPosition(t), t);
        }
    }

    public T replaceData(int position, T t) {
        synchronized (lock) {
            if (data == null) {
                throw new RuntimeException("BlueRecyclerViewItemCache.replaceData to a null list");
            }
            if (position == -1) {
                return null;
            }
            T old = data.remove(position);
            data.add(position, t);
            map.put(t.getUniqueIdentificationId(), t);
            return old;
        }
    }

    public void sortItems() {
        if (comparator != null) {
            Collections.sort(data, comparator);
        }
    }

    /**
     * Get the item position.
     *
     * @param o
     */
    public int getPosition(Object o) {
        synchronized (lock) {
            if (o == null || data == null) {
                return -1;
            }
            T t = map.get(o);
            return getPosition(t);
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

    public T get(Object key) {
        synchronized (lock) {
            if (key == null || map == null) {
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
    @Nullable
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
     * Remove item by range.
     *
     * @return remove count.
     */
    @Nullable
    public List<T> remove(int positionStart, int count) {
        synchronized (lock) {
            if (data == null || positionStart < 0 || positionStart + count > data.size()) {
                return null;
            }
            List<T> itemToBeRemoved = new ArrayList<>(data.size());
            for (int i = 0; i < count; i++) {
                itemToBeRemoved.add(data.get(positionStart + i));
            }
            data.removeAll(itemToBeRemoved);
            return itemToBeRemoved;
        }
    }

    /**
     * Remove item by unique key.
     *
     * @param s
     * @return
     */
    public int remove(String s) {
        synchronized (lock) {
            if (data == null || s == null) {
                return -1;
            }
            int position = getPosition(s);
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
     * Remove items.
     *
     * @param list
     * @return
     */
    public boolean remove(List<T> list) {
        synchronized (lock) {
            if (data == null || list == null || list.size() <= 0) {
                return false;
            }
            boolean changed = data.removeAll(list);
            if (changed) {
                for (T t : list) {
                    map.remove(t.getUniqueIdentificationId());
                }
            }
            return changed;
        }
    }

    /**
     * Remove item.
     */
    public void clear() {
        clear(false);
    }

    public void clear(boolean clearSelection) {
        synchronized (lock) {
            if (data == null || map == null) {
                return;
            }
            if (clearSelection) {
                defaultSelectedItems.clear();
                selectItemsKeySet.clear();
                selectedItems.clear();
                mapSelectStateChanged.clear();
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

    public boolean contains(T t) {
        synchronized (lock) {
            return t != null && map.containsKey(t.getUniqueIdentificationId());
        }
    }

    public boolean contains(Object o) {
        synchronized (lock) {
            return o != null && map.containsKey(o);
        }
    }

    public int getSelectedPosition(T t) {
        synchronized (lock) {
            return new ArrayList<>(selectedItems).indexOf(t);
        }
    }

    public boolean isItemSelectStateLocked(int position) {
        synchronized (lock) {
            return isItemSelectStateLocked(data.get(position));
        }
    }

    public boolean isItemSelectStateLocked(Object o) {
        synchronized (lock) {
            return isItemSelectStateLocked(map.get(o));
        }
    }

    public boolean isItemSelectStateLocked(T t) {
        synchronized (lock) {
            return t != null && lockDefaultSelection && isItemDefaultSelected(t);
        }
    }

    /**
     * Return all selected items.
     *
     * @return
     */
    @NonNull
    public ArrayList<T> getAllSelectedItems() {
        synchronized (lock) {
            if (data == null) {
                return new ArrayList<>(0);
            }
            return new ArrayList<>(selectedItems);
        }
    }

    public ArrayList<T> getAllSelectedItemsWithoutDefaultSelection() {
        synchronized (lock) {
            ArrayList<T> allSelectedItems = getAllSelectedItems();
            ArrayList<T> result = new ArrayList<>(allSelectedItems.size());
            for (T t : allSelectedItems) {
                if (!defaultSelectedItems.contains(t.getUniqueIdentificationId())) {
                    result.add(t);
                }
            }
            return result;
        }
    }

    public ArrayList<T> popAllSelectedItems() {
        synchronized (lock) {
            ArrayList<T> arrayList = getAllSelectedItems();
            clearAllSelection();
            return arrayList;
        }
    }

    public ArrayList<T> popAllSelectedItemsWithoutDefaultSelection() {
        synchronized (lock) {
            ArrayList<T> arrayList = getAllSelectedItemsWithoutDefaultSelection();
            clearAllSelectionIncludeDefaultSelection();
            return arrayList;
        }
    }

    /**
     * Clear the selected status of all items.
     */
    public void clearAllSelection() {
        synchronized (lock) {
            selectedItems.clear();
            selectItemsKeySet.clear();
            mapSelectStateChanged.clear();
        }
    }

    /**
     * Clear the selected status of all items.
     */
    public void selectAll() {
        synchronized (lock) {
            selectedItems.clear();
            selectedItems.addAll(data);
            selectItemsKeySet.clear();
            for (T t : data) {
                selectItemsKeySet.add(t.getUniqueIdentificationId());
                mapSelectStateChanged.put(t.getUniqueIdentificationId(), true);
            }
        }
    }

    public void clearAllSelectionIncludeDefaultSelection() {
        synchronized (lock) {
            clearAllSelection();
            defaultSelectedItems.clear();
        }
    }

    public int getSelectSize() {
        synchronized (lock) {
            return selectedItems.size();
        }
    }

    public int getSelectSizeWithoutDefaultSelection() {
        synchronized (lock) {
            return selectedItems.size() - defaultSelectedItems.size();
        }
    }

    public int getDefaultSelectSize() {
        synchronized (lock) {
            return defaultSelectedItems.size();
        }
    }

    public boolean isItemDefaultSelected(T t) {
        return defaultSelectedItems.contains(t.getUniqueIdentificationId());
    }

    private void markItemSelectedChanged(T t, boolean selected) {
        markItemSelectedChanged(t.getUniqueIdentificationId(), selected);
    }

    private void markItemSelectedChanged(Object o, boolean selected) {
        synchronized (lock) {
            if (mapSelectStateChanged.containsKey(o)) {
                Boolean selectedState = mapSelectStateChanged.get(o);
                if (selectedState == null) {
                    mapSelectStateChanged.put(o, selected);
                } else {
                    if (selected != selectedState) {
                        mapSelectStateChanged.remove(o);
                    }
                }
            } else {
                mapSelectStateChanged.put(o, selected);
            }
        }
    }

    public boolean ifSelectChanged(T t) {
        return t != null && ifSelectChanged(t.getUniqueIdentificationId());
    }

    public boolean ifSelectChanged(Object o) {
        synchronized (lock) {
            return o != null && mapSelectStateChanged.remove(o) != null;
        }
    }

    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }
}
