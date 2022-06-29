package im.turbo.baseui.base.bridgefragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;


/**
 * created by zhaoyuntao
 * on 2019-11-07
 * description:
 */
public class BridgeFragment extends Fragment implements BridgeInterface {
    private static final String TAG = "BridgeFragment";
    private final int requestCode = 4242;
    private BridgeCallback callBack;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BridgeFragment.this.requestCode && callBack != null) {
            callBack.onActivityResult(resultCode, data);
        }
    }

    //bridge fragment
    public static BridgeFragment getFragment(Activity activity) {
        if (activity == null) {
            throw new RuntimeException("activity state error!!!:" + activity);
        }
        BridgeFragment shareFragment = (BridgeFragment) activity.getFragmentManager().findFragmentByTag(TAG);
        if (shareFragment == null) {
            shareFragment = new BridgeFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
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
