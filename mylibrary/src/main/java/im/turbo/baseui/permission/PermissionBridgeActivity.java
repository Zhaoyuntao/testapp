package im.turbo.baseui.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.doctor.mylibrary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import im.turbo.baseui.base.bridgefragment.BridgeCallback;

/**
 * created by zhaoyuntao
 * on 27/06/2022
 * description:
 */
public class PermissionBridgeActivity extends AppCompatActivity {
    private final static Map<Long, PermissionResult> permissionResults = new ConcurrentHashMap<>(5);
    private final static Map<Long, InstallResult> installResults = new ConcurrentHashMap<>(5);
    private final static String KEY_TYPE = "requestType";
    private final static String KEY_PERMISSIONS = "permissions";
    private final static String KEY_SHOW_DIALOG = "showDialog";
    private final static String KEY_REQUEST_STRING_RES = "requestStringRes";
    private final static String KEY_GUIDE_STRING_RES = "guideStringRes";
    private final static String KEY_DRAWABLE_RES = "drawableRes";
    private final static String KEY_TIME = "time";
    private final static int isPermission = 0;
    private final static int isInstall = 1;

    private long time;
    @Permission
    private String[] permissions;
    private boolean showDialog;
    @StringRes
    private int requestStringRes;
    @StringRes
    private int guideStringRes;
    @DrawableRes
    private int drawableRes;

    private final int requestCodePermissionRequest = 1255;
    private boolean openedSystemSettingPage;
    private PermissionResult permissionResult;
    private InstallResult installResult;
    private int type;

    static void requestPermissions(Context context, @Permission String[] permissions, @Nullable PermissionResult permissionResult) {
        requestPermissions(context, permissions, false, 0, 0, permissionResult);
    }

    static void requestPermissions(Context context, @Permission String[] permissions, boolean showDialog, @StringRes int requestStringRes, @StringRes int guideStringRes, @Nullable PermissionResult permissionResult) {
        if (PermissionCore.getDeniedPermissions(context, permissions).length == 0) {
            if (permissionResult != null) {
                permissionResult.onGranted(permissions);
            }
        }else{
            boolean deniedForever = PermissionCore.isDeniedForever(context, permissions);
            if (deniedForever && !showDialog) {
                if (permissionResult != null) {
                    permissionResult.onDenied(permissions, true);
                }
                return;
            }

            long timeNow = SystemClock.elapsedRealtime();
            if (permissionResult != null) {
                PermissionBridgeActivity.permissionResults.put(timeNow, permissionResult);
            }

            Intent intent = new Intent(context, PermissionBridgeActivity.class);
            intent.putExtra(KEY_TYPE, isPermission);
            intent.putExtra(KEY_PERMISSIONS, permissions);
            intent.putExtra(KEY_SHOW_DIALOG, showDialog);
            intent.putExtra(KEY_REQUEST_STRING_RES, requestStringRes);
            intent.putExtra(KEY_GUIDE_STRING_RES, guideStringRes);
            intent.putExtra(KEY_TIME, timeNow);
            IntentUtils.startActivity(context, intent);
        }
    }

    static void requestInstall(Context context, InstallResult installResult) {
        requestInstall(context, false, 0, 0, installResult);
    }

    static void requestInstall(Context context, boolean showDialog, int requestStringRes, int drawableRes, InstallResult installResult) {
        long timeNow = SystemClock.elapsedRealtime();
        PermissionBridgeActivity.installResults.put(timeNow, installResult);

        Intent intent = new Intent(context, PermissionBridgeActivity.class);
        intent.putExtra(KEY_TYPE, isInstall);
        intent.putExtra(KEY_SHOW_DIALOG, showDialog);
        intent.putExtra(KEY_REQUEST_STRING_RES, requestStringRes);
        intent.putExtra(KEY_DRAWABLE_RES, drawableRes);
        intent.putExtra(KEY_TIME, timeNow);
        IntentUtils.startActivity(context, intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);

        if (savedInstanceState != null) return;

        Intent intent = getIntent();
        time = intent.getLongExtra(KEY_TIME, -1);
        showDialog = intent.getBooleanExtra(KEY_SHOW_DIALOG, false);
        requestStringRes = intent.getIntExtra(KEY_REQUEST_STRING_RES, 0);
        type = getIntent().getIntExtra(KEY_TYPE, -1);
        if (isPermission == type) {
            permissionResult = permissionResults.get(time);
            guideStringRes = intent.getIntExtra(KEY_GUIDE_STRING_RES, 0);
            permissions = intent.getStringArrayExtra(KEY_PERMISSIONS);
            performRequestPermissions();
        } else if (isInstall == type) {
            installResult = installResults.get(time);
            drawableRes = intent.getIntExtra(KEY_DRAWABLE_RES, 0);
            performRequestInstall();
        } else {
            finish();
        }
    }

    private void performRequestPermissions() {
        permissions = PermissionCore.filterPermissions(permissions);
        if (PermissionCore.getDeniedPermissions(this, permissions).length == 0) {
            if (permissionResult != null) {
                permissionResult.onGranted(permissions);
            }
            finish();
            return;
        }
        boolean deniedForever = PermissionCore.isDeniedForever(this, permissions);
        if (showDialog) {
            hideDialogFragment();
            PermissionDialog dialog = new PermissionDialog()
                    .setPermissionIcon(PermissionCore.getPermissionDrawable(permissions))
                    .setMessage(deniedForever ? guideStringRes : requestStringRes)
                    .setButtonRight(deniedForever ? R.string.permission_settings_open : R.string.permission_continue, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (deniedForever) {
                                openPermissionSettingPage();
                            } else {
                                _requestPermissionInner();
                            }
                        }
                    })
                    .setButtonLeft(R.string.permission_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (permissionResult != null) {
                                permissionResult.onCanceled(permissions);
                            }
                            finish();
                        }
                    })
                    .setOnDismissListener(new PermissionDialog.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            if (permissionResult != null) {
                                permissionResult.onCanceled(permissions);
                                finish();
                            }
                        }
                    });
            dialog.show(getSupportFragmentManager(), "dialog");
        } else {
            _requestPermissionInner();
        }
    }

    private void hideDialogFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment pre = fragmentManager.findFragmentByTag("dialog");
        if (pre instanceof DialogFragment) {
            ((DialogFragment) pre).dismiss();
        }
    }

    private void openPermissionSettingPage() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri packageURI = Uri.fromParts("package", getPackageName(), null);
        intent.setData(packageURI);
        startActivity(intent);
        openedSystemSettingPage = true;
        if (installResult != null) {
            installResult.onOpenedSystemSettingPage();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (openedSystemSettingPage) {
            @Permission
            String[] deniedPermissions = PermissionCore.getDeniedPermissions(this, permissions);
            if (permissionResult != null) {
                if (deniedPermissions.length > 0) {
                    permissionResult.onDenied(permissions, PermissionCore.isDeniedForever(this, deniedPermissions));
                } else {
                    permissionResult.onGranted(permissions);
                }
            }
            finish();
        }
    }

    private void _requestPermissionInner() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCodePermissionRequest);
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCodePermissionRequest);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @Permission String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.requestCodePermissionRequest) {
            PermissionCore.setPermissionRequested(this, permissions);
            int permissionCount = permissions.length;
            List<String> deniedPermissions = new ArrayList<>(permissionCount);
            for (int i = 0; i < permissionCount; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (permissionResult != null) {
                if (deniedPermissions.size() > 0) {
                    permissionResult.onDenied(permissions, PermissionCore.isDeniedForever(this, deniedPermissions.toArray(new String[0])));
                } else {
                    permissionResult.onGranted(permissions);
                }
            }
        } else {
            if (permissionResult != null) {
                permissionResult.onDenied(permissions, PermissionCore.isDeniedForever(this, permissions));
            }
        }
        finish();
    }

    private void performRequestInstall() {
        if (PermissionCore.canRequestPackageInstalls(this)) {
            if (installResult != null) {
                installResult.onRequestInstallResult(true);
            }
            finish();
        } else {
            if (showDialog) {
                hideDialogFragment();
                PermissionDialog dialog = new PermissionDialog()
                        .setPermissionIcon(drawableRes)
                        .setMessage(requestStringRes)
                        .setButtonRight(R.string.permission_continue, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                _requestInstallInner();
                            }
                        })
                        .setButtonLeft(R.string.permission_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (installResult != null) {
                                    installResult.onCanceled();
                                }
                                finish();
                            }
                        })
                        .setOnDismissListener(new PermissionDialog.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                if (permissionResult != null) {
                                    permissionResult.onCanceled(permissions);
                                }
                                finish();
                            }
                        });
                dialog.show(getSupportFragmentManager(), "dialog");
            } else {
                _requestInstallInner();
            }
        }
    }

    private void _requestInstallInner() {
        Intent manageIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        manageIntent.setData(Uri.fromParts("package", getPackageName(), null));
        IntentUtils.startActivityForResult(this, manageIntent, new BridgeCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == RESULT_OK) {
                    if (installResult != null) {
                        installResult.onRequestInstallResult(PermissionCore.canRequestPackageInstalls(PermissionBridgeActivity.this));
                    }
                } else {
                    if (installResult != null) {
                        installResult.onCanceled();
                    }
                }
                finish();
            }
        });
    }

    @Override
    public void finish() {
        S.sd("finish");
        if (isPermission == type) {
            permissionResults.remove(time);
        } else if (isInstall == type) {
            installResults.remove(time);
        }
        super.finish();
        // Reset the animation to avoid flickering.
        overridePendingTransition(0, 0);
    }
}
