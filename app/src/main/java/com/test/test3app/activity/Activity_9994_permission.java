package com.test.test3app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.test.test3app.R;
import im.turbo.utils.log.S;

import base.ui.BaseActivity;
import im.turbo.baseui.permission.InstallResult;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionResult;
import im.turbo.baseui.permission.PermissionUtils;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.ResourceUtils;


public class Activity_9994_permission extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_9994_new_permission);

        TextView permission = findViewById(R.id.request_permission);
        TextView permissionDialog = findViewById(R.id.request_permission_dialog);
        TextView install = findViewById(R.id.request_install);
        TextView installDialog = findViewById(R.id.request_install_dialog);
        permission.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PermissionUtils.requestPermission(ResourceUtils.getApplicationContext(),
                        new PermissionResult() {
                            @Override
                            public void onGranted(@NonNull String[] grantedPermissions) {
                                postText(permission, "permission", "Granted");
                            }

                            @Override
                            public void onDenied(@NonNull String[] deniedPermissions, boolean deniedForever) {
                                postText(permission, "permission", "Denied:" + deniedForever);
                            }

                            @Override
                            public void onCanceled(@NonNull String[] permissions) {
                                postText(permission, "permission", "Canceled");
                            }
                        }, Permission.READ_EXTERNAL_STORAGE);
            }
        });
        permissionDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PermissionUtils.requestPermission(ResourceUtils.getApplicationContext(),
                        R.string.string_abc, R.string.string_abc,
                        new PermissionResult() {
                            @Override
                            public void onGranted(@NonNull String[] grantedPermissions) {
                                postText(permissionDialog, "dialog", "Granted");
                            }

                            @Override
                            public void onDenied(@NonNull String[] deniedPermissions, boolean deniedForever) {
                                postText(permissionDialog, "dialog", "Denied:" + deniedForever);
                            }

                            @Override
                            public void onCanceled(@NonNull String[] permissions) {
                                postText(permissionDialog, "dialog", "Canceled");
                            }
                        }, Permission.READ_EXTERNAL_STORAGE);
            }
        });
        install.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PermissionUtils.requestInstallPermission(ResourceUtils.getApplicationContext(), new InstallResult() {
                    @Override
                    public void onRequestInstallResult(boolean result) {
                        postText(install, "install", "" + result);
                    }

                    @Override
                    public void onCanceled() {
                        postText(install, "install", "Canceled");
                    }
                });
            }
        });
        installDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PermissionUtils.requestInstallPermission(ResourceUtils.getApplicationContext(), R.string.string_abc, R.drawable.a2, new InstallResult() {
                    @Override
                    public void onRequestInstallResult(boolean result) {
                        postText(installDialog, "installDialog", "" + result);
                    }

                    @Override
                    public void onCanceled() {
                        postText(installDialog, "installDialog", "Canceled");
                    }
                });
            }
        });
    }

    private void postText(TextView textView, String title, String text) {
        S.s(text + " ");
        textView.setText(text);
        ThreadPool.runUiDelayed(1000, new SafeRunnable(textView) {
            @Override
            protected void runSafely() {
                textView.setText(title);
            }
        });
    }
}
