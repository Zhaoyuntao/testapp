package im.turbo.baseui.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.doctor.mylibrary.R;

import java.util.ArrayList;
import java.util.List;

import im.turbo.baseui.base.bridgefragment.BridgeCallback;

/**
 * created by zhaoyuntao
 * on 27/06/2022
 * description:
 */
public class PermissionBridgeActivity extends AppCompatActivity {

    @Permission
    private static String[] permissions;
    private static boolean showDialog;
    @StringRes
    private static int requestStringRes;
    @StringRes
    private static int guideStringRes;
    @DrawableRes
    private static int drawableRes;
    @Nullable
    private static PermissionResult permissionResult;
    @Nullable
    private static InstallResult installResult;
    private static boolean isPermission;
    private static boolean isInstall;

    private final int requestCodePermissionRequest = 1255;
    private PermissionDialog dialog;
    private boolean openedSystemSettingPage;

    static void requestPermissions(Context context, @Permission String[] permissions, @Nullable PermissionResult permissionResult) {
        requestPermissions(context, permissions, false, 0, 0, permissionResult);
    }

    static void requestPermissions(Context context, @Permission String[] permissions, boolean showDialog, @StringRes int requestStringRes, @StringRes int guideStringRes, @Nullable PermissionResult permissionResult) {
        PermissionBridgeActivity.isPermission = true;
        PermissionBridgeActivity.permissions = permissions;
        PermissionBridgeActivity.showDialog = showDialog;
        PermissionBridgeActivity.requestStringRes = requestStringRes;
        PermissionBridgeActivity.guideStringRes = guideStringRes;
        PermissionBridgeActivity.permissionResult = permissionResult;

        Intent intent = new Intent(context, PermissionBridgeActivity.class);
        IntentUtils.startActivity(context, intent);
    }

    static void requestInstall(Context context, InstallResult installResult) {
        requestInstall(context, false, 0, 0, installResult);
    }

    static void requestInstall(Context context, boolean showDialog, int requestStringRes, int drawableRes, InstallResult installResult) {
        PermissionBridgeActivity.isInstall = true;
        PermissionBridgeActivity.showDialog = showDialog;
        PermissionBridgeActivity.requestStringRes = requestStringRes;
        PermissionBridgeActivity.drawableRes = drawableRes;
        PermissionBridgeActivity.installResult = installResult;

        Intent intent = new Intent(context, PermissionBridgeActivity.class);
        IntentUtils.startActivity(context, intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_transparent);

        if (savedInstanceState != null) return;

        if (isPermission) {
            performRequestPermissions();
        } else if (isInstall) {
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
            dialog = new PermissionDialog(this)
                    .setPermissionIcon(PermissionCore.getPermissionDrawable(permissions))
                    .setTouchOutsideCancel(false)
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
                    .setCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            if (permissionResult != null) {
                                permissionResult.onCanceled(permissions);
                            }
                            finish();
                        }
                    });
            dialog.show();
        } else {
            _requestPermissionInner();
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
                    PermissionCore.setAlwaysDeniedPermissionBefore(this, deniedPermissions, true);
                    permissionResult.onDenied(permissions);
                } else {
                    PermissionCore.setAlwaysDeniedPermissionBefore(this, permissions, false);
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
            int permissionCount = permissions.length;
            List<String> deniedPermissions = new ArrayList<>(permissionCount);
            for (int i = 0; i < permissionCount; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (permissionResult != null) {
                if (deniedPermissions.size() > 0) {
                    PermissionCore.setAlwaysDeniedPermissionBefore(this, deniedPermissions.toArray(new String[0]), true);
                    permissionResult.onDenied(permissions);
                } else {
                    PermissionCore.setAlwaysDeniedPermissionBefore(this, permissions, false);
                    permissionResult.onGranted(permissions);
                }
            }
        } else {
            if (permissionResult != null) {
                permissionResult.onDenied(permissions);
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
                dialog = new PermissionDialog(this)
                        .setPermissionIcon(drawableRes)
                        .setTouchOutsideCancel(false)
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
                        .setCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                if (installResult != null) {
                                    installResult.onCanceled();
                                }
                                finish();
                            }
                        });
                dialog.show();
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
        if (dialog != null) {
            dialog.cancel();
        }
        openedSystemSettingPage = false;
        isPermission = false;
        isInstall = false;
        permissions = null;
        showDialog = false;
        requestStringRes = 0;
        guideStringRes = 0;
        permissionResult = null;
        drawableRes = 0;
        installResult = null;
        super.finish();
    }
}
