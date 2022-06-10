package im.turbo.baseui.base.bridgefragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import im.turbo.baseui.lifecircle.LifeCycleHelper;

/**
 * created by zhaoyuntao
 * on 28/02/2022
 * description:
 */
public interface BridgeInterface {
    void startActivityForResult(Intent intent, BridgeCallback callBack);

    void startActivityForResult(Intent intent, BridgeCallback callBack, Bundle bundle);

    static BridgeInterface getFragment(Context context) {
        Activity activity = LifeCycleHelper.findActivity(context);
        if (activity instanceof AppCompatActivity) {
            return BridgeFragmentx.getFragment((AppCompatActivity) activity);
        } else {
            return BridgeFragment.getFragment(activity);
        }
    }
}
