package com.test.test3app.cashgift;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;


/**
 * created by zhaoyuntao
 * on 02/11/2021
 * description:
 */
public class CashGiftThemeBean {
    @CashGiftThemes
    private final int theme;
    private String name;
    @ColorInt
    private int textColor;
    @ColorInt
    private int borderColor;
    @DrawableRes
    private int skinCellRes;
    private String skinCellUrl;
    @DrawableRes
    private int skinOpenRes;
    private String skinOpenUrl;
    @DrawableRes
    private int skinNotOpenRes;
    private String skinNotOpenUrl;
    @DrawableRes
    private int buttonRes;
    private String buttonUrl;
    @DrawableRes
    private int skinPreviewRes;
    private String skinPreviewUrl;
    @DrawableRes
    private int skinPreviewIconRes;
    private String skinPreviewIconUrl;
    @DrawableRes
    private int skinBackgroundRes;
    private String skinBackgroundUrl;
    @ColorInt
    private int textBackgroundColor;
    private String message;

    public CashGiftThemeBean(int theme) {
        this.theme = theme;
    }

    public int getTheme() {
        return theme;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getSkinCellRes() {
        return skinCellRes;
    }

    public void setSkinCellRes(int skinCellRes) {
        this.skinCellRes = skinCellRes;
    }

    public String getSkinCellUrl() {
        return skinCellUrl;
    }

    public void setSkinCellUrl(String skinCellUrl) {
        this.skinCellUrl = skinCellUrl;
    }

    public int getTextBackgroundColor() {
        return textBackgroundColor;
    }

    public void setTextBackgroundColor(int textBackgroundColor) {
        this.textBackgroundColor = textBackgroundColor;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public int getButtonRes() {
        return buttonRes;
    }

    public void setButtonRes(int buttonRes) {
        this.buttonRes = buttonRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSkinOpenRes() {
        return skinOpenRes;
    }

    public void setSkinOpenRes(int skinOpenRes) {
        this.skinOpenRes = skinOpenRes;
    }

    public String getSkinOpenUrl() {
        return skinOpenUrl;
    }

    public void setSkinOpenUrl(String skinOpenUrl) {
        this.skinOpenUrl = skinOpenUrl;
    }

    public int getSkinNotOpenRes() {
        return skinNotOpenRes;
    }

    public void setSkinNotOpenRes(int skinNotOpenRes) {
        this.skinNotOpenRes = skinNotOpenRes;
    }

    public String getSkinNotOpenUrl() {
        return skinNotOpenUrl;
    }

    public void setSkinNotOpenUrl(String skinNotOpenUrl) {
        this.skinNotOpenUrl = skinNotOpenUrl;
    }

    public int getSkinPreviewRes() {
        return skinPreviewRes;
    }

    public void setSkinPreviewRes(int skinPreviewRes) {
        this.skinPreviewRes = skinPreviewRes;
    }

    public String getSkinPreviewUrl() {
        return skinPreviewUrl;
    }

    public void setSkinPreviewUrl(String skinPreviewUrl) {
        this.skinPreviewUrl = skinPreviewUrl;
    }

    public int getSkinPreviewIconRes() {
        return skinPreviewIconRes;
    }

    public void setSkinPreviewIconRes(int skinPreviewIconRes) {
        this.skinPreviewIconRes = skinPreviewIconRes;
    }

    public String getSkinPreviewIconUrl() {
        return skinPreviewIconUrl;
    }

    public void setSkinPreviewIconUrl(String skinPreviewIconUrl) {
        this.skinPreviewIconUrl = skinPreviewIconUrl;
    }

    public int getSkinBackgroundRes() {
        return skinBackgroundRes;
    }

    public void setSkinBackgroundRes(int skinBackgroundRes) {
        this.skinBackgroundRes = skinBackgroundRes;
    }

    public String getSkinBackgroundUrl() {
        return skinBackgroundUrl;
    }

    public void setSkinBackgroundUrl(String skinBackgroundUrl) {
        this.skinBackgroundUrl = skinBackgroundUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
