package im.thebot.chat.ui.emoji;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import im.thebot.chat.ui.emoji.category.BaseEmojiCategory;
import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 01/09/2020
 * description:
 */
public class EmojiMap {
    private final List<BaseEmojiCategory> list;
    private List<BaseEmojiCategory> listUi;

    private final Object lock;

    private EmojiMap() {
        list = new ArrayList<>();
        lock = new Object();
    }

    private static class EmojiMapHolder {

        private static final EmojiMap instance = new EmojiMap();
    }

    public static EmojiMap getInstance() {
        return EmojiMapHolder.instance;
    }

    public void addEmoji(@NonNull BaseEmojiCategory baseEmojiCategory) {
        synchronized (lock) {
            list.add(baseEmojiCategory);
        }
    }

    public List<BaseEmojiCategory> getAllEmoji() {
        synchronized (lock) {
            if (listUi == null || list.size() != listUi.size()) {
                listUi = new ArrayList<>(list);
            }
            return listUi;
        }
    }

    public BaseEmojiCategory getEmoji(int i) {
        synchronized (lock) {
            return list.get(i);
        }
    }

    public int size() {
        synchronized (lock) {
            return list.size();
        }
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        synchronized (lock) {
            for (BaseEmojiCategory entry : list) {
                stringBuilder.append(ResourceUtils.getString(entry.getDescRes())).append(":\n").append(entry.getType()).append("\n");
            }
        }
        return "EmojiMap:\n" +
                stringBuilder;
    }
}
