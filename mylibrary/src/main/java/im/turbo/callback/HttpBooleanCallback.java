package im.turbo.callback;

/**
 * created by zhaoyuntao
 * on 2021/4/7
 * description:
 */
public interface HttpBooleanCallback {
    void onSuccess(boolean result);

    default void onFailed(int errorCode, String errorMessage){}
}
