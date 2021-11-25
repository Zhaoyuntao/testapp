package com.test.test3app.cashgift;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 02/11/2021
 * description:
 */
@IntDef({
        CashGiftThemes.THEME_0_NORMAL,
        CashGiftThemes.THEME_1_BLUE,
        CashGiftThemes.THEME_2_RED,
        CashGiftThemes.THEME_3_YELLOW,
        CashGiftThemes.THEME_4_GREEN
})
public @interface CashGiftThemes {
    int THEME_0_NORMAL = 1001;
    int THEME_1_BLUE = 1004;
    int THEME_2_RED = 1003;
    int THEME_3_YELLOW = 1005;
    int THEME_4_GREEN = 1002;
}
