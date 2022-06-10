package im.turbo.basetools.selector;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * created by zhaoyuntao
 * on 2020/7/6
 * description:
 */
public class SimpleSelector<T> {
    private final List<T> selectedItems;
    private final Set<T> defaultSelectedItems;
    private List<T> data;
    private final Object lock = new Object();
    private boolean lockDefaultSelection;

    public SimpleSelector() {
        selectedItems = new ArrayList<>();
        defaultSelectedItems = new HashSet<>();
        data = new ArrayList<>();
    }

    public SimpleSelector(List<T> data) {
        this();
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
            if (defaultSelectedItems.contains(t) && lockDefaultSelection) {
                return -1;
            }
            selectedItems.add(t);
            return data.indexOf(t);
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
            if (defaultSelectedItems.contains(t) && lockDefaultSelection) {
                return -1;
            }
            defaultSelectedItems.remove(t);
            selectedItems.remove(t);
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
            return selectedItems.contains(t);
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
            this.data.addAll(data);
            this.lockDefaultSelection = lockedDefaultSelectedItemState;
            initDefaultSelection(defaultSelectedItems);
        }
    }

    /**
     * Set data list of all items,it will clear selected status of all items.
     *
     * @param data
     */
    public void setData(List<T> data) {
        setData(data, null, lockDefaultSelection);
    }

    public void setData(List<T> data, @Nullable List<T> defaultSelectedItems, boolean lockedDefaultSelectedItemState) {
        clear();
        synchronized (lock) {
            if (data == null) {
                return;
            }
            this.data = data;
            this.lockDefaultSelection = lockedDefaultSelectedItemState;
            initDefaultSelection(defaultSelectedItems);
        }
    }

    private void initDefaultSelection(List<T> defaultSelectedItems) {
        if (defaultSelectedItems != null) {
            for (T t : defaultSelectedItems) {
                this.defaultSelectedItems.remove(t);
                this.defaultSelectedItems.add(t);
                this.selectedItems.add(t);
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
            return old;
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
            return data.remove(position);
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
            return data.removeAll(list);
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
            if (data == null) {
                return;
            }
            if (clearSelection) {
                defaultSelectedItems.clear();
                selectedItems.clear();
            }
            data.clear();
        }
    }

    public int size() {
        synchronized (lock) {
            return data.size();
        }
    }

    public boolean contains(T t) {
        synchronized (lock) {
            return t != null && data.contains(t);
        }
    }

    public int getSelectedPosition(T t) {
        synchronized (lock) {
            return selectedItems.indexOf(t);
        }
    }

    public boolean isItemSelectStateLocked(int position) {
        synchronized (lock) {
            return isItemSelectStateLocked(data.get(position));
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
                if (!defaultSelectedItems.contains(t)) {
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
        }
    }

    /**
     * Clear the selected status of all items.
     */
    public void selectAll() {
        synchronized (lock) {
            selectedItems.clear();
            selectedItems.addAll(data);
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
        return defaultSelectedItems.contains(t);
    }
}
