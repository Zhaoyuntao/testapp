package im.turbo.basetools.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;


import im.turbo.baseui.toast.ToastUtil;
import im.turbo.utils.ResourceUtils;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 21/09/2021
 * description:
 */
public class ClipBoardUtils {
    public static void copy(CharSequence message, String alert) {
        if (message == null) {
            return;
        }
        ClipboardManager clip = (ClipboardManager) (ResourceUtils.getSystemService(Context.CLIPBOARD_SERVICE));
        if (clip != null) {
            clip.setPrimaryClip(ClipData.newPlainText(null, message));
        }
        if (!TextUtils.isEmpty(alert)) {
            ToastUtil.show(alert);
        }
    }
}
