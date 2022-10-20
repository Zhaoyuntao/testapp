package im.turbo.basetools.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import im.turbo.utils.ResourceUtils;
import im.turbo.utils.ScreenUtils;
import im.turbo.utils.log.S;


/**
 * created by zhaoyuntao
 * on 2019-10-25
 * description:
 * inputmethod
 */
public class InputMethodUtils {
    private static int mHeightOfKeyBoard;

    /**
     * open or close inputmethod,if already closed,it open.if opened,it close.
     *
     * @param view
     */
    public static void openInputMethod(View view) {
        if (view == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean result = imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
        }
    }

    public static void forceOpenInputKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) ResourceUtils.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * close inputmethod
     *
     * @param view
     */
    public static void closeInputMethod(View view) {
        if (view == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!imm.isActive()) {
                return;
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignore) {
        }
    }

    public static boolean checkKeyBoardOpen(View rootWindow) {
        return checkKeyBoardOpen(rootWindow, null);
    }

    public static boolean checkKeyBoardOpen(View rootView, OnKeyBoardOpenListener onKeyBoardOpenListener) {

        if (rootView == null || !rootView.isAttachedToWindow()) {
            return false;
        }
        View rootWindowView = rootView.getRootView();
        if (rootWindowView == null || !rootWindowView.isAttachedToWindow()) {
            return false;
        }
        int heightRootWindowView = rootWindowView.getHeight();
        int heightKeyBoard = heightRootWindowView - rootView.getHeight();
        // save state
        final int heightKeyBoardNow = heightKeyBoard - mHeightOfKeyBoard;
        int maxHeightInputmethod = (int) (heightRootWindowView * 0.60f);
        int minHeightInputmethod = (int) (heightRootWindowView * 0.2f);
        S.s("screen:"+ ScreenUtils.getScreenHeightPX() +" heightRootWindowView:" + heightRootWindowView + " heightKeyBoard:" + heightKeyBoard + " minHeightInputmethod:" + minHeightInputmethod);
        boolean keyBoardOpened = heightKeyBoard > minHeightInputmethod;
        if (keyBoardOpened) {
            if (onKeyBoardOpenListener != null) {
                onKeyBoardOpenListener.getKeyBoardHeight(Math.min(heightKeyBoardNow, maxHeightInputmethod));
            }
        } else {
            mHeightOfKeyBoard = heightKeyBoard;
        }

        return keyBoardOpened;
    }

    public interface OnKeyBoardOpenListener {
        void getKeyBoardHeight(int heightOfKeyBoard);
    }
}
