package im.turbo.baseui.base.bridgefragment;

import android.content.Intent;

/**
 * created by zhaoyuntao
 * on 28/02/2022
 * description:
 */
public interface BridgeCallback {
    void onActivityResult(int resultCode, Intent data);
}
