package im.turbo.basetools.selector;

import android.os.Bundle;

/**
 * created by zhaoyuntao
 * on 20/09/2020
 * description:
 */
public class DiffHelper {
    public static Bundle getPayload(String key, String value) {
        Bundle payload = new Bundle();
        payload.putString(key, value);
        return payload;
    }

    public static Bundle getPayload(String key, int value) {
        Bundle payload = new Bundle();
        payload.putInt(key, value);
        return payload;
    }

    public static Bundle getPayload(String key, long value) {
        Bundle payload = new Bundle();
        payload.putLong(key, value);
        return payload;
    }

    public static Bundle getPayload(String key, double value) {
        Bundle payload = new Bundle();
        payload.putDouble(key, value);
        return payload;
    }

    public static Bundle getPayload(String key, boolean value) {
        Bundle payload = new Bundle();
        payload.putBoolean(key, value);
        return payload;
    }

    public static Bundle addPayload(Bundle bundle, String key, String o) {
        bundle.putString(key, o);
        return bundle;
    }

    public static Bundle addPayload(Bundle bundle, String key, int o) {
        bundle.putInt(key, o);
        return bundle;
    }

    public static Bundle addPayload(Bundle bundle, String key, long o) {
        bundle.putLong(key, o);
        return bundle;
    }

    public static Bundle addPayload(Bundle bundle, String key, double o) {
        bundle.putDouble(key, o);
        return bundle;
    }

    public static Bundle addPayload(Bundle bundle, String key, boolean o) {
        bundle.putBoolean(key, o);
        return bundle;
    }
}
