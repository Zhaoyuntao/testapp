package im.turbo.callback;

/**
 * created by zhaoyuntao
 * on 2021/4/7
 * description:
 */
public interface HttpCallback {
    default void onSuccess() {
    }

    default void onFailure(int errorCode, String errorMessage) {
    }
}
