
package im.turbo.baseui.toast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.module_chat.R;

import im.turbo.basetools.preconditions.Preconditions;
import im.turbo.baseui.utils.UiUtils;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.ResourceUtils;

public class ToastUtil {
    private static Toast mLastToast;
    private static int offsetY;

    /**
     * Show toast in view.
     *
     * @param view           Can be null.
     * @param textResourceId
     */
    public static void show(final View view, @StringRes int textResourceId) {
        show(view, ResourceUtils.getString(textResourceId));
    }

    public static void show(final View view, @StringRes int textResourceId, @DrawableRes int drawableResourceId) {
        show(view, ResourceUtils.getString(textResourceId), drawableResourceId);
    }

    public static void show(final View view, String text) {
        show(view, text, -1);
    }

    public static void show(View view, String text, @DrawableRes int drawableResourceId) {
        if (view == null) {
            return;
        }
        _runToast(text, drawableResourceId);
    }

    /**
     * Show toast in activity.
     *
     * @param activity       Can be null.
     * @param textResourceId
     */
    public static void show(final Activity activity, @StringRes int textResourceId) {
        show(activity, ResourceUtils.getString(textResourceId));
    }

    public static void show(final Activity activity, @StringRes int textResourceId, @DrawableRes int drawableResourceId) {
        show(activity, ResourceUtils.getString(textResourceId), drawableResourceId);
    }

    public static void show(final Activity activity, String text) {
        show(activity, text, -1);
    }

    public static void show(final Activity activity, String text, @DrawableRes int drawableResourceId) {
        if (activity == null) {
            return;
        }
        _runToast(text, drawableResourceId);
    }

    /**
     * Show toast in fragment.
     *
     * @param fragment       Can be null.
     * @param textResourceId
     */
    public static void show(final Fragment fragment, @StringRes int textResourceId) {
        show(fragment, ResourceUtils.getString(textResourceId));
    }

    public static void show(final Fragment fragment, @StringRes int textResourceId, @DrawableRes int drawableResourceId) {
        show(fragment, ResourceUtils.getString(textResourceId), drawableResourceId);
    }

    public static void show(final Fragment fragment, String text) {
        show(fragment, text, -1);
    }

    public static void show(final Fragment fragment, String text, @DrawableRes int drawableResourceId) {
        if (fragment == null) {
            return;
        }
        _runToast(text, drawableResourceId);
    }

    /**
     * Show toast with a available context.
     *
     * @param context        must instanceof activty
     * @param textResourceId
     */
    public static void show(final Context context, @StringRes int textResourceId) {
        show(context, ResourceUtils.getString(textResourceId));
    }

    private static void show(final Context context, String text) {
        show(context, text, -1);
    }

    private static void show(final Context context, String text, @DrawableRes int drawableResourceId) {
        if (context == null) {
            return;
        }
        _runToast(text, drawableResourceId);
    }

    /**
     * Show toast with a application.
     *
     * @param textResourceId
     */
    public static void show(@StringRes int textResourceId) {
        show(ResourceUtils.getString(textResourceId));
    }

    public static void show(String text) {
        show(text, -1);
    }

    public static void show(String text, String subtitle) {
        _runToast(text, subtitle, -1);
    }

    public static void show(@StringRes int textResourceId, @DrawableRes int drawableResourceId) {
        _runToast(ResourceUtils.getString(textResourceId), drawableResourceId);
    }

    public static void show(String text, @DrawableRes int drawableResourceId) {
        _runToast(text, drawableResourceId);
    }

    public static void show(String text, boolean showLong) {
        _runToast(text, -1, showLong);
    }

    /**
     * show on top of the window
     *
     * @param textResourceId
     * @param onTop
     */
    public static void show(@StringRes int textResourceId, boolean onTop) {
        if (onTop) {
            _runToast(ResourceUtils.getString(textResourceId), null, -1, false, Gravity.TOP);
        } else {
            _runToast(ResourceUtils.getString(textResourceId), null, -1, false, Gravity.BOTTOM);
        }
    }

    private static void _runToast(final String text, @DrawableRes int drawableResourceId) {
        _runToast(text, null, drawableResourceId, false);
    }

    private static void _runToast(final String text, final String subtitle, @DrawableRes int drawableResourceId) {
        _runToast(text, subtitle, drawableResourceId, false);
    }

    private static void _runToast(final String text, @DrawableRes int drawableResourceId, boolean showLong) {
        _runToast(text, null, drawableResourceId, showLong);
    }

    private static void _runToast(final String text, final String subtitle, @DrawableRes int drawableResourceId, boolean showLong) {
        _runToast(text, subtitle, drawableResourceId, showLong, Gravity.BOTTOM);
    }

    private static void _runToast(final String text, final String subtitle, @DrawableRes int drawableResourceId, boolean showLong, int gravity) {
        if (Preconditions.isUiThread()) {
            _showToast(text, subtitle, drawableResourceId, showLong, gravity);
        } else {
            ThreadPool.runMain(new Runnable() {
                @Override
                public void run() {
                    _showToast(text, subtitle, drawableResourceId, showLong, gravity);
                }
            });
        }
    }

    private static void _showToast(final String text, final String subtitle, @DrawableRes int drawableResourceId, boolean showLong, int gravity) {
        final Context context = ResourceUtils.getApplication();
        if (context == null) {
            return;
        }
//        if (!AppFrontBackHelper.getInstance().isAppForeground()) {
//            return;
//        }
        try {
            if (offsetY == 0) {
                int[] sizeOfScreen = UiUtils.getScreenSize(context);
                offsetY = (int) (sizeOfScreen[1] * 0.1f);
            }
            Toast toast = getRootView(context, showLong, gravity);
            View rootView = toast.getView();
            if (rootView == null) {
                return;
            }
            TextView textView = rootView.findViewById(R.id.toast_text_view);
            textView.setText(text);
            TextView textSubview = rootView.findViewById(R.id.toast_text_subview);
            if (!TextUtils.isEmpty(subtitle)) {
                textSubview.setText(subtitle);
                textSubview.setVisibility(View.VISIBLE);
            } else {
                textSubview.setVisibility(View.GONE);
            }
            if (drawableResourceId != -1) {
                ImageView imageView = rootView.findViewById(R.id.toast_image_view);
                Drawable drawable = null;
                try {
                    drawable = ContextCompat.getDrawable(context, drawableResourceId);
                } catch (Resources.NotFoundException ignore) {
                }
                if (drawable != null) {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(drawableResourceId);
                } else {
                    imageView.setVisibility(View.GONE);
                    imageView.setImageDrawable(null);
                }
            }
            toast.show();
        } catch (Exception e) {
        }
    }

    private static Toast getRootView(Context context, boolean showLong, int gravity) {
        Toast toast;
        if (mLastToast != null) {
            mLastToast.cancel();
        }
        toast = new Toast(context);
        toast.setGravity(gravity, 0, offsetY);
        @SuppressLint("InflateParams")
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_toast_view, null);
        toast.setDuration(showLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setView(rootView);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            toast.addCallback(new Toast.Callback() {
//                @Override
//                public void onToastShown() {
//                    mLastToast = toast;
//                }
//
//                @Override
//                public void onToastHidden() {
//                    toast.removeCallback(this);
//                    mLastToast = null;
//                }
//            });
//        }
        return toast;
    }
}
