package com.test.test3app.cashgift;

import android.text.TextUtils;


import com.test.test3app.R;
import com.test.test3app.threadpool.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 02/11/2021
 * description:
 */
public class CashGiftThemeUtils {
    public static CashGiftThemeBean getCashGiftTheme(@CashGiftThemes int theme) {
        CashGiftThemeBean cashGiftThemeBean = new CashGiftThemeBean(theme);
        switch (theme) {
            case CashGiftThemes.THEME_1_BLUE:
                cashGiftThemeBean.setName(Utilities.getString(R.string.cash_theme_name_blue));
                cashGiftThemeBean.setSkinCellRes(R.drawable.png_cash_gift_skin_blue);
                cashGiftThemeBean.setSkinNotOpenRes(R.drawable.png_skin_cash_not_opend_blue);
                cashGiftThemeBean.setSkinOpenRes(R.drawable.png_skin_cash_opened_blue);
                cashGiftThemeBean.setSkinPreviewRes(R.drawable.svg_preview_back_blue);
                cashGiftThemeBean.setSkinPreviewIconRes(R.drawable.png_preview_icon_blue);
                cashGiftThemeBean.setButtonRes(R.drawable.png_skin_button_blue);
                cashGiftThemeBean.setBorderColor(Utilities.getColor(R.color.theme_border_color_blue));
                cashGiftThemeBean.setTextColor(Utilities.getColor(R.color.theme_text_color_blue));
                cashGiftThemeBean.setTextBackgroundColor(Utilities.getColor(R.color.theme_text_back_color_blue));
                cashGiftThemeBean.setSkinBackgroundRes(R.drawable.png_skin_background_detail_blue);
                cashGiftThemeBean.setMessage(Utilities.getString(R.string.string_subject_cash_gift_theme_blue));
                break;
            case CashGiftThemes.THEME_2_RED:
                cashGiftThemeBean.setName(Utilities.getString(R.string.cash_theme_name_red));
                cashGiftThemeBean.setSkinCellRes(R.drawable.png_cash_gift_skin_red);
                cashGiftThemeBean.setSkinNotOpenRes(R.drawable.png_skin_cash_not_opend_red);
                cashGiftThemeBean.setSkinOpenRes(R.drawable.png_skin_cash_opened_red);
                cashGiftThemeBean.setSkinPreviewRes(R.drawable.svg_preview_back_red);
                cashGiftThemeBean.setSkinPreviewIconRes(R.drawable.png_preview_icon_red);
                cashGiftThemeBean.setButtonRes(R.drawable.png_skin_button_red);
                cashGiftThemeBean.setBorderColor(Utilities.getColor(R.color.theme_border_color_red));
                cashGiftThemeBean.setTextColor(Utilities.getColor(R.color.theme_text_color_red));
                cashGiftThemeBean.setTextBackgroundColor(Utilities.getColor(R.color.theme_text_back_color_red));
                cashGiftThemeBean.setSkinBackgroundRes(R.drawable.png_skin_background_detail_red);
                cashGiftThemeBean.setMessage(Utilities.getString(R.string.string_subject_cash_gift_theme_red));
                break;
            case CashGiftThemes.THEME_3_YELLOW:
                cashGiftThemeBean.setName(Utilities.getString(R.string.cash_theme_name_yellow));
                cashGiftThemeBean.setSkinCellRes(R.drawable.png_cash_gift_skin_yellow);
                cashGiftThemeBean.setSkinNotOpenRes(R.drawable.png_skin_cash_not_opend_yellow);
                cashGiftThemeBean.setSkinOpenRes(R.drawable.png_skin_cash_opened_yellow);
                cashGiftThemeBean.setSkinPreviewRes(R.drawable.svg_preview_back_yellow);
                cashGiftThemeBean.setSkinPreviewIconRes(R.drawable.png_preview_icon_yellow);
                cashGiftThemeBean.setButtonRes(R.drawable.png_skin_button_yellow);
                cashGiftThemeBean.setBorderColor(Utilities.getColor(R.color.theme_border_color_yellow));
                cashGiftThemeBean.setTextColor(Utilities.getColor(R.color.theme_text_color_yellow));
                cashGiftThemeBean.setTextBackgroundColor(Utilities.getColor(R.color.theme_text_back_color_yellow));
                cashGiftThemeBean.setSkinBackgroundRes(R.drawable.png_skin_background_detail_yellow);
                cashGiftThemeBean.setMessage(Utilities.getString(R.string.string_subject_cash_gift_theme_yellow));
                break;
            case CashGiftThemes.THEME_4_GREEN:
                cashGiftThemeBean.setName(Utilities.getString(R.string.cash_theme_name_green));
                cashGiftThemeBean.setSkinCellRes(R.drawable.png_cash_gift_skin_green);
                cashGiftThemeBean.setSkinNotOpenRes(R.drawable.png_skin_cash_not_opend_green);
                cashGiftThemeBean.setSkinOpenRes(R.drawable.png_skin_cash_opened_green);
                cashGiftThemeBean.setSkinPreviewRes(R.drawable.svg_preview_back_green);
                cashGiftThemeBean.setSkinPreviewIconRes(R.drawable.png_preview_icon_green);
                cashGiftThemeBean.setButtonRes(R.drawable.png_skin_button_green);
                cashGiftThemeBean.setBorderColor(Utilities.getColor(R.color.theme_border_color_green));
                cashGiftThemeBean.setTextColor(Utilities.getColor(R.color.theme_text_color_green));
                cashGiftThemeBean.setTextBackgroundColor(Utilities.getColor(R.color.theme_text_back_color_green));
                cashGiftThemeBean.setSkinBackgroundRes(R.drawable.png_skin_background_detail_green);
                cashGiftThemeBean.setMessage(Utilities.getString(R.string.string_subject_cash_gift_theme_green));
                break;
            case CashGiftThemes.THEME_0_NORMAL:
            default:
                cashGiftThemeBean.setName(Utilities.getString(R.string.cash_theme_name_normal));
                cashGiftThemeBean.setSkinCellRes(R.drawable.png_cash_gift_skin_normal);
                cashGiftThemeBean.setSkinNotOpenRes(R.drawable.png_skin_cash_not_opend_normal);
                cashGiftThemeBean.setSkinOpenRes(R.drawable.png_skin_cash_opened_normal);
                cashGiftThemeBean.setSkinPreviewRes(R.drawable.svg_preview_back_normal);
                cashGiftThemeBean.setSkinPreviewIconRes(R.drawable.png_preview_icon_normal);
                cashGiftThemeBean.setButtonRes(R.drawable.png_skin_button_normal);
                cashGiftThemeBean.setBorderColor(Utilities.getColor(R.color.theme_border_color_normal));
                cashGiftThemeBean.setTextColor(Utilities.getColor(R.color.theme_text_color_normal));
                cashGiftThemeBean.setTextBackgroundColor(Utilities.getColor(R.color.theme_text_back_color_normal));
                cashGiftThemeBean.setSkinBackgroundRes(R.drawable.png_skin_background_detail_normal);
                cashGiftThemeBean.setMessage(Utilities.getString(R.string.string_subject_cash_gift_theme_normal));
                break;
        }
        return cashGiftThemeBean;
    }

    public static List<CashGiftThemeBean> getAllCashGiftThemes() {
        List<CashGiftThemeBean> list = new ArrayList<>(5);
        list.add(getCashGiftTheme(CashGiftThemes.THEME_0_NORMAL));
        list.add(getCashGiftTheme(CashGiftThemes.THEME_4_GREEN));
        list.add(getCashGiftTheme(CashGiftThemes.THEME_2_RED));
        list.add(getCashGiftTheme(CashGiftThemes.THEME_1_BLUE));
        list.add(getCashGiftTheme(CashGiftThemes.THEME_3_YELLOW));
        return list;
    }

    public static String getNotifyParams(int themeId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("themeID", themeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static int getThemeIdFromNotifyParams(String notifyParam) {
        if (TextUtils.isEmpty(notifyParam)) {
            return CashGiftThemes.THEME_0_NORMAL;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(notifyParam);
        } catch (JSONException e) {
            return CashGiftThemes.THEME_0_NORMAL;
        }
        return jsonObject.optInt("themeID");
    }
}
