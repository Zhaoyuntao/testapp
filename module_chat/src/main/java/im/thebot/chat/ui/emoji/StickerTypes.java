package im.thebot.chat.ui.emoji;

import androidx.annotation.StringDef;

/**
 * created by zhaoyuntao
 * on 12/09/2021
 * description:
 */
@StringDef({
        StickerTypes.RecentCategory,
        StickerTypes.SmileysAndPeopleCategory,
        StickerTypes.AnimalsAndNatureCategory,
        StickerTypes.FoodAndDrinkCategory,
        StickerTypes.ActivitiesCategory,
        StickerTypes.TravelAndPlacesCategory,
        StickerTypes.ObjectsCategory,
        StickerTypes.SymbolsCategory,
        StickerTypes.FlagsCategory,
})
public @interface StickerTypes {
    String RecentCategory = "RecentCategory";
    String SmileysAndPeopleCategory = "SmileysAndPeopleCategory";
    String AnimalsAndNatureCategory = "AnimalsAndNatureCategory";
    String FoodAndDrinkCategory = "FoodAndDrinkCategory";
    String ActivitiesCategory = "ActivitiesCategory";
    String TravelAndPlacesCategory = "TravelAndPlacesCategory";
    String ObjectsCategory = "ObjectsCategory";
    String SymbolsCategory = "SymbolsCategory";
    String FlagsCategory = "FlagsCategory";
}
