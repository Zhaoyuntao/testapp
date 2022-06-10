package im.thebot.chat.ui.emoji.category;


import android.text.TextUtils;

import androidx.annotation.StringRes;

import com.example.module_chat.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import im.thebot.chat.ui.emoji.EmojiBean;
import im.thebot.chat.ui.emoji.StickerTypes;
import im.turbo.thread.ThreadPool;

/**
 * created by zhaoyuntao
 * on 12/09/2021
 * description:
 */
public class RecentCategory implements BaseEmojiCategory {
    private static final int MAX_RECENT_COUNT = 8;
    private static List<EmojiBean> list;
    private static final Object lock = new Object();
    private static final String KEY_STORAGE = "user.config.emoji-recent";
    private static Runnable saveTask;
    //todo lv2
//    private static LiteStorage kvs;

    @Override
    public String getType() {
        return StickerTypes.RecentCategory;
    }

    @StringRes
    @Override
    public int getDescRes() {
        return R.string.emoji_recent;
    }

    @Override
    public int getNormalIconRes() {
        return R.drawable.emoji_recent_in_active;
    }

    @Override
    public int getActiveIconRes() {
        return R.drawable.emoji_recent_active;
    }

    @Override
    public EmojiBean[] getData() {
        _preload();
        synchronized (lock) {
            return list.toArray(new EmojiBean[0]);
        }
    }

    public static void addRecentSticker(EmojiBean emojiBean) {
        _preload();
        synchronized (lock) {
            if (list.contains(emojiBean)) {
                list.remove(emojiBean);
            }
            list.add(0, emojiBean);
            if (list.size() > MAX_RECENT_COUNT) {
                list.remove(list.size() - 1);
            }
        }
        save();
    }

    public static void preload() {
        ThreadPool.runIO(RecentCategory::_preload);
    }

    private static void _preload() {

        if (list != null) {
            return;
        }
        //todo lv2
//        if (kvs == null) {
//            kvs = UserConfigCenter.Companion.getInstance().getKvs();
//        }
        synchronized (lock) {
            list = new ArrayList<>();
        }
        //todo lv2
        String json = null;//kvs.getString(KEY_STORAGE, "");
        if (!TextUtils.isEmpty(json)) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonArray != null && jsonArray.length() > 0) {

                synchronized (lock) {
                    for (int i = 0; i < jsonArray.length() && i < MAX_RECENT_COUNT; i++) {
                        String emoji = jsonArray.optString(i);
                        EmojiBean emojiBean = new EmojiBean();
                        emojiBean.setCode(emoji);
                        list.add(emojiBean);
                    }
                }
            }
        }
    }

    private static void save() {

        if (saveTask == null) {
            //todo lv2
//            if (kvs == null) {
//                kvs = UserConfigCenter.Companion.getInstance().getKvs();
//            }
            saveTask = () -> {
                if (list == null || list.size() == 0) {
                    return;
                }
                JSONArray jsonArray = new JSONArray();
                synchronized (lock) {
                    for (EmojiBean emojiBean : list) {
                        jsonArray.put(emojiBean.getCode());
                    }
                }
                //todo lv2
//                kvs.putString(KEY_STORAGE, jsonArray.toString());
            };
        }
        ThreadPool.removeDb(saveTask);
        ThreadPool.runDbDelayed(1000, saveTask);
    }
}
