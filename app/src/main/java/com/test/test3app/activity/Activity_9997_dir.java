package com.test.test3app.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;

import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.S;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import base.ui.BaseActivity;
import base.ui.AutoSizeTextView;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionResult;
import im.turbo.baseui.permission.PermissionUtils;

public class Activity_9997_dir extends BaseActivity {
    private List<FD> list = new ArrayList<>();
    private String fileName = "Hello.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_9997_dir);

        list.clear();
        addFileDir("getCacheDir", getCacheDir());
        addFileDir("getFilesDir", getFilesDir());
        addFileDir("getExternalCacheDir", getExternalCacheDir());
        addFileDir("getExternalFilesDir", getExternalFilesDir(null));
        addFileDir("getExternalFilesDirDCIM", getExternalFilesDir(Environment.DIRECTORY_DCIM));
        addFileDir("getExternalStorageDirectory", Environment.getExternalStorageDirectory());

        AutoSizeTextView textView1 = findViewById(R.id.title1);
        findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.requestPermission(activity(), new PermissionResult() {
                    @Override
                    public void onGranted(@NonNull String[] grantedPermissions) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (FD fd : list) {
                            stringBuilder.append("\n\n[").append(fd.name).append("]: ").append(fd.dir.getAbsolutePath()).append("\n").append(readFrom(fd));
                        }
                        textView1.setText(stringBuilder.toString());
                    }
                }, Permission.READ_EXTERNAL_STORAGE);
            }
        });
        findViewById(R.id.write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.requestPermission(activity(), new PermissionResult() {
                    @Override
                    public void onGranted(@NonNull String[] grantedPermissions) {
                        for (FD fd : list) {
                            writeTo(fd);
                        }
                    }
                }, Permission.WRITE_EXTERNAL_STORAGE);
            }
        });
    }

    private void writeTo(FD fd) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(fd.dir, fileName));
            fileWriter.write("Hello world!!!");
            S.s("write success:" + fd.dir.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String readFrom(FD fd) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(new File(fd.dir, fileName));
            StringBuilder stringBuilder = new StringBuilder();
            int c = 0;
            while ((c = fileReader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void addFileDir(String name, File file) {
        list.add(new FD(name, file));
    }

    public static class FD {
        String name;
        File dir;

        public FD(String name, File dir) {
            this.name = name;
            this.dir = dir;
        }
    }
}
