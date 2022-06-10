package im.thebot.chat.ui.emoji;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;

import com.example.module_chat.R;

import im.thebot.chat.ui.emoji.category.ActivitiesCategory;
import im.thebot.chat.ui.emoji.category.AnimalsAndNatureCategory;
import im.thebot.chat.ui.emoji.category.FlagsCategory;
import im.thebot.chat.ui.emoji.category.FoodAndDrinkCategory;
import im.thebot.chat.ui.emoji.category.ObjectsCategory;
import im.thebot.chat.ui.emoji.category.RecentCategory;
import im.thebot.chat.ui.emoji.category.SmileysAndPeopleCategory;
import im.thebot.chat.ui.emoji.category.SymbolsCategory;
import im.thebot.chat.ui.emoji.category.TravelAndPlacesCategory;

/**
 * created by zhaoyuntao
 * on 28/08/2020
 * description:
 */
public class EmojiUtils {
    private static EmojiMap emojiMap;

    public static void init(Application application) {
        final EmojiCompat.Config config;
        // Use a downloadable font for EmojiCompat.
        final FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);
        config = new FontRequestEmojiCompatConfig(application, fontRequest)
                .setReplaceAll(true)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onInitialized() {
//                        S.s("emoji init success");
                    }

                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
//                        S.e("EmojiCompat initialization failed" + throwable);
                    }
                });
        EmojiCompat.init(config);
    }

    public static void preLoadHistory() {
        RecentCategory.preload();
    }

    public static void readJson(@NonNull EmojiReader emojiReader) {
        if (emojiMap == null) {
            EmojiMap.getInstance().addEmoji(new RecentCategory());
            EmojiMap.getInstance().addEmoji(new SmileysAndPeopleCategory());
            EmojiMap.getInstance().addEmoji(new AnimalsAndNatureCategory());
            EmojiMap.getInstance().addEmoji(new FoodAndDrinkCategory());
            EmojiMap.getInstance().addEmoji(new ActivitiesCategory());
            EmojiMap.getInstance().addEmoji(new TravelAndPlacesCategory());
            EmojiMap.getInstance().addEmoji(new ObjectsCategory());
            EmojiMap.getInstance().addEmoji(new SymbolsCategory());
            EmojiMap.getInstance().addEmoji(new FlagsCategory());
            emojiMap = EmojiMap.getInstance();
        }
        emojiReader.onComplete(emojiMap);
    }
}
