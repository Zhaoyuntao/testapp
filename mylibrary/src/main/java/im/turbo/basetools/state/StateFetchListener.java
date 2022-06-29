package im.turbo.basetools.state;

import androidx.annotation.NonNull;

/**
 * created by zhaoyuntao
 * on 26/05/2022
 * description:
 */
public interface StateFetchListener {
    void onFetched(@NonNull String stateTag);
}
