
package im.turbo.baseui.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.text.TextUtilsCompat;
import androidx.core.view.ViewCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Locale;

import im.turbo.utils.ResourceUtils;

public class UiUtils {

    private static float sDensity;

    public static int getAlphaColor(@ColorInt int color, float percent) {
        return (color & 0x00FFFFFF) | (percent >= 1 ? 0xFF000000 : ((int) (0xFF * percent) << 24));
    }

    public static float dipFloatToPx(float dip) {
        return dip * getDensity();
    }

    public static int dipToPx(float dip) {
        return (int) (dip * getDensity() + 0.5f);
    }

    public static int dipToPx(int dip) {
        return (int) (dip * getDensity() + 0.5f);
    }

    public static float dipToPxFloat(float dip) {
        return (dip * getDensity() + 0.5f);
    }


    public static float getDensity() {
        if (sDensity == 0f) {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) ResourceUtils.getApplication().getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getMetrics(dm);
            sDensity = dm.density;
        }
        return sDensity;
    }

    public static int getScreenWidthPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) ResourceUtils.getApplication().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeightPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) ResourceUtils.getApplication().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getRealScreenHeightPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) ResourceUtils.getApplication().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getRealMetrics(dm);
        return dm.heightPixels;
    }

    static public int pxToDip(float px) {
        return (int) (px / getDensity() + 0.5f);
    }

    public static int spToPx(float sp) {
        return (int) (sp * getDensity() + 0.5f);
    }

    /**
     * get system layout direction
     *
     * @return
     */
    public static boolean isRTL() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
    }

    public static int[] getScreenSize(Context context) {
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowmanager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    public static Bundle mergePayload(List<Bundle> payloads) {
        if (payloads == null || payloads.isEmpty())
            return new Bundle();
        if (payloads.size() == 1)
            return payloads.get(0);
        int size = payloads.size();
        Bundle payload0 = payloads.get(0);
        for (int i = 1; i < size; i++) {
            payload0.putAll(payloads.get(i));
        }
        return payload0;
    }


    /**
     * 修改屏幕方向
     *
     * @return
     */
    public static boolean fixOrientation(Activity activity) {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(activity);
            o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否是全透明
     *
     * @return
     */
    public static boolean isTranslucentOrFloating(Context context) {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = context.obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }


    /**
     * @param button        只接受ImageButtonWithText或者Button
     * @param editTextArray edittext列表
     */
    public static void editCtrlEnable(final Object button, final EditText... editTextArray) {

        if (editTextArray == null || button == null)
            return;
        //初始化的时候检测当前edittext里的值，判断状态。
        boolean isBtnEnable = true;
        for (int i = 0; i < editTextArray.length; i++) {
            if (editTextArray[i] == null) {
                return;
            }
            editTextArray[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    boolean isBtnEnable = true;
                    //当前输入框不为空，置为true
                    if (!TextUtils.isEmpty(s)) {
                        isBtnEnable = true;
                        //再检测其他输入框是否为空
                        for (int i = 0; i < editTextArray.length; i++) {
                            EditText et = editTextArray[i];
                            if (et != null && TextUtils.isEmpty(et.getText().toString())) {
                                isBtnEnable = false;
                            }
                        }
                    } else {
                        isBtnEnable = false;
                    }
                    if (button instanceof Button) {
                        ((Button) button).setEnabled(isBtnEnable);
                    }

                }
            });

            if (TextUtils.isEmpty(editTextArray[i].getText().toString())) {
                isBtnEnable = false;
            }

        }
        //初始化的时候根据状态控制按钮enable与否
        if (button instanceof Button) {
            ((Button) button).setEnabled(isBtnEnable);
        }

    }


    public static void clearAllViewMember(Object obj) {
        if (obj == null) {
            return;
        }
        try {
            Class<?> cls = obj.getClass();
            if (cls == null) {
                return;
            }
            Field[] flds = cls.getDeclaredFields();
            if (flds == null) {
                return;
            }
            for (Field fld : flds) {
                String name = fld.getName();
                // LogUtil.debug(TAG, "check:" + name);
                if (!name.startsWith("m_")) {
                    continue;
                }
                int modify = fld.getModifiers();
                if (Modifier.isStatic(modify)) {
                    continue;
                }
                if (Modifier.isFinal(modify)) {
                    continue;
                }
                if (Modifier.isPublic(modify)) {
                    continue;
                }
                fld.setAccessible(true);
                Object fieldValue = fld.get(obj);
                if (fieldValue == null) {
                    // LogUtil.debug(TAG, "null:" + name);
                    continue;
                }
                if (fieldValue instanceof View || fieldValue instanceof Bitmap
                        || fieldValue instanceof Drawable
                        || fieldValue instanceof Context
                        || fieldValue instanceof Dialog
                        || fieldValue instanceof Handler
                        || fieldValue instanceof BaseAdapter
                        || fieldValue instanceof Animation
                        || fieldValue instanceof AssetFileDescriptor) {
                    // LogUtil.debug(TAG, "clear:" + name);
                    fld.set(obj, null);
                }
            }
        } catch (Exception e) {
            // LogUtil.error(TAG, e);
        }
    }

    public static boolean isRTLLanguage() {
//		Configuration config = BOTApplication.getContext().getResources().getConfiguration();
//		return LayoutDirection.RTL == config.getLayoutDirection();
        int direction = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault());
        return direction == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    public static void setLayerType(View v, int layerType, Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            v.setLayerType(layerType, paint);
        }
    }

    public static void fixGravityAndDirection(TextView textView) {
        if (null == textView) {
            return;
        }
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        textView.setTextDirection(View.TEXT_DIRECTION_LTR);
    }

    public final static int getPreImageSize() {
        int size = (int) (210 * getDensity());
        return size;
    }

    public final static int scale(int value) {
        return (int) (value * getDensity());
    }

    public final static int dp(float value) {
        return (int) Math.ceil(getDensity() * value);
    }

    public final static int rescale(int value) {
        float density = getDensity();
        if (density > 1 && density < 1.5) {
            density = 1.5f;
        }
        return (int) (value * getDensity());
    }

    public final static float dip2px(float dpValue) {
        return (dpValue * getDensity());
    }

    public final static float px2sp(float pxValue) {
        final float scale = ResourceUtils.getApplication().getResources()
                .getDisplayMetrics().scaledDensity;
        return (pxValue / scale);
    }
}
