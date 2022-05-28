package im.turbo.baseui.permission.tp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.test3app.R;
import com.test.test3app.lifecircle.LifeCycleHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * created by zhaoyuntao
 * on 2019-11-07
 * description:
 * Don't change access.
 */
public class PermissionBridgeFragment extends Fragment {
    private static final String TAG = "BridgeFragment";

    private PermissionResult permissionResult;
    private InstallResult installResult;
    private final int requestCode = 1255;
    private PermissionDialog dialog;

    static void requestPermissions(Context context, @Permission String[] permissions, PermissionResult permissionResult) {
        requestPermissions(context, permissions, false, -1, -1, permissionResult);
    }

    static void requestPermissions(Context context, @Permission String[] permissions, boolean showDialog, @StringRes int requestStringRes, @StringRes int guideStringRes, PermissionResult permissionResult) {
        PermissionBridgeFragment bridgeFragment = createFragment(context);
        if (bridgeFragment == null) {
            return;
        }
        bridgeFragment.permissionResult = permissionResult;
        permissions = PermissionCore.filterPermissions(permissions);
        if (PermissionCore.getDeniedPermissions(context, permissions).length == 0) {
            permissionResult.onGranted(permissions);
            return;
        }
        boolean deniedForever = PermissionCore.isDeniedForever(context, permissions);

        if (showDialog) {
            String[] finalPermissions = permissions;
            bridgeFragment.dialog = new PermissionDialog(context)
                    .setPermissionIcon(PermissionCore.getPermissionDrawable(permissions))
                    .setTouchOutsideCancel(false)
                    .setMessage(deniedForever ? guideStringRes : requestStringRes)
                    .setButtonRight(deniedForever ? R.string.permission_settings_open : R.string.permission_continue, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (deniedForever) {
                                PermissionCore.openPermissionSettingPage(context);
                            } else {
                                bridgeFragment.request(finalPermissions);
                            }
                        }
                    })
                    .setButtonLeft(R.string.permission_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            permissionResult.onCanceled(finalPermissions);
                        }
                    });
            bridgeFragment.dialog.show();
        } else {
            bridgeFragment.request(permissions);
        }
    }

    private static PermissionBridgeFragment createFragment(Context context) {
        Activity activityTmp = LifeCycleHelper.findActivity(context);
        if (!(activityTmp instanceof AppCompatActivity)) {
            return null;
        }
        AppCompatActivity activity = (AppCompatActivity) activityTmp;
        PermissionBridgeFragment bridgeFragment = (PermissionBridgeFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if (bridgeFragment == null) {
            bridgeFragment = new PermissionBridgeFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().add(bridgeFragment, TAG).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return bridgeFragment;
    }

    private void request(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @Permission String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionBridgeFragment.this.requestCode && permissionResult != null) {
            int permissionCount = permissions.length;
            List<String> deniedPermissions = new ArrayList<>(permissionCount);
            for (int i = 0; i < permissionCount; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (permissionResult != null) {
                if (deniedPermissions.size() > 0) {
                    PermissionCore.setAlwaysDeniedPermissionBefore(requireContext(), deniedPermissions.toArray(new String[0]), true);
                    permissionResult.onDenied(permissions);
                } else {
                    PermissionCore.setAlwaysDeniedPermissionBefore(requireContext(), permissions, false);
                    permissionResult.onGranted(permissions);
                }
            }
        }
        permissionResult = null;
    }


    static void requestInstall(Context context, InstallResult installResult) {
        requestInstall(context, false, -1, -1, installResult);
    }

    static void requestInstall(Context context, boolean showDialog, @StringRes int requestStringRes, @DrawableRes int drawableRes, @Nullable InstallResult installResult) {
        if (PermissionCore.canRequestPackageInstalls(context)) {
            if (installResult != null) {
                installResult.onRequestInstallResult(true);
            }
        } else {
            PermissionBridgeFragment bridgeFragment = createFragment(context);
            if (bridgeFragment == null) {
                return;
            }
            bridgeFragment.installResult = installResult;
            if (showDialog) {
                bridgeFragment.dialog = new PermissionDialog(context)
                        .setPermissionIcon(drawableRes)
                        .setTouchOutsideCancel(false)
                        .setMessage(requestStringRes)
                        .setButtonRight(R.string.permission_continue, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bridgeFragment._requestInstall();
                            }
                        })
                        .setButtonLeft(R.string.permission_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (installResult != null) {
                                    installResult.onCanceled();
                                }
                            }
                        });
                bridgeFragment.dialog.show();
            } else {
                bridgeFragment._requestInstall();
            }
        }
    }

    private void _requestInstall() {
        Intent manageIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        manageIntent.setData(Uri.fromParts("package", requireContext().getPackageName(), null));
        startActivityForResult(manageIntent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PermissionBridgeFragment.this.requestCode && permissionResult != null) {
            installResult.onRequestInstallResult(PermissionCore.canRequestPackageInstalls(requireContext()));
        }
        installResult = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (dialog != null) {
            dialog.cancel();
        }
        this.permissionResult = null;
        this.installResult = null;
    }
}
