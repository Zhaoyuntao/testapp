package im.turbo.baseui.base.bridgefragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


/**
 * created by zhaoyuntao
 * on 2019-11-07
 * description:
 */
public class BridgeFragmentx extends Fragment implements BridgeInterface {
    private static final String TAG = "AvoidActivityResultFragment";
    private final int requestCode = 4242;
    private BridgeCallback callBack;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BridgeFragmentx.this.requestCode && callBack != null) {
            callBack.onActivityResult(resultCode, data);
        }
    }

    public static BridgeFragmentx getFragment(AppCompatActivity activity) {
        if (activity == null) {
            throw new RuntimeException("activity state error!!!:" + activity);
        }
        BridgeFragmentx shareFragment = (BridgeFragmentx) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if (shareFragment == null) {
            shareFragment = new BridgeFragmentx();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().add(shareFragment, TAG).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return shareFragment;
    }

    @Override
    public void startActivityForResult(Intent intent, BridgeCallback callBack) {
        this.callBack = callBack;
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, BridgeCallback callBack, Bundle bundle) {
        this.callBack = callBack;
        startActivityForResult(intent, requestCode, bundle);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callBack = null;
    }
}
