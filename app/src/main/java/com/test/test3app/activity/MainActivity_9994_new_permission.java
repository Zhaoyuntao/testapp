package com.test.test3app.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.T;

import im.turbo.baseui.permission.tp.InstallResult;
import im.turbo.baseui.permission.tp.Permission;
import im.turbo.baseui.permission.tp.PermissionResult;
import im.turbo.baseui.permission.tp.PermissionUtils;


public class MainActivity_9994_new_permission extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_9994_new_permission);

        findViewById(R.id.text_tp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.requestPermission(activity(), R.string.string_abc, R.string.string_abc, new PermissionResult() {
                    @Override
                    public void onGranted(@NonNull String[] grantedPermissions) {
                        T.t(activity(), "Granted");
                    }

                    @Override
                    public void onDenied(@NonNull String[] deniedPermissions) {
                        T.t(activity(), "Denied");
                    }

                    @Override
                    public void onCanceled(@NonNull String[] permissions) {
                        T.t(activity(), "Canceled");
                    }
                }, Permission.READ_EXTERNAL_STORAGE);
            }
        });
        findViewById(R.id.text_ip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.requestInstallPermission(activity(), R.string.string_abc, R.drawable.a2, new InstallResult() {
                    @Override
                    public void onRequestInstallResult(boolean result) {
                        T.t(activity(), "result:" + result);
                    }
                });
            }
        });
    }

}
