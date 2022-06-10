package im.turbo.baseui.permission;

/**
 * created by zhaoyuntao
 * on 2022/5/28
 */
public interface InstallResult {
    void onRequestInstallResult(boolean result);

    default void onCanceled() {
    }
}
