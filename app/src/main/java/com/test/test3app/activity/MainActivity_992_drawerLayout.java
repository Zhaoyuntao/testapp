package com.test.test3app.activity;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.drawerlayout.DrawerAdapter;
import com.test.test3app.threadpool.ResourceUtils;
import com.zhaoyuntao.androidutils.permission.PermissionSettings;
import com.zhaoyuntao.androidutils.permission.runtime.Permission;
import com.zhaoyuntao.androidutils.tools.S;
import com.zhaoyuntao.androidutils.tools.ZP;
import com.zhaoyuntao.androidutils.tools.thread.TP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MainActivity_992_drawerLayout extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_draw_layout);

        DrawerLayout drawerLayout = findViewById(R.id.drawer);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new DrawerAdapter());

        ZP.p(activity(), new ZP.RequestResult() {
            @Override
            public void onGranted(List<String> permissions) {
                TP.runOnPool(new Runnable() {
                    @Override
                    public void run() {
                        sss();
                    }
                });
            }

            @Override
            public void onDenied(List<String> permissions) {

            }

            @Override
            public void onDeniedNotAsk(PermissionSettings permissionSettings) {

            }
        }, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
    }

    public void sss() {
        File[] files = ResourceUtils.getApplicationContext().getExternalMediaDirs();
        File publicDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
        if (publicDir != null) {
            S.s("files:" + Arrays.toString(files) + "  dir:" + publicDir);
            String filename = "abcd.txt";
            File file = new File(files[0], filename);
            S.s("file:" + file.getAbsolutePath());
            if (!file.exists()) {
                S.s("file not exists");
                try {
                    boolean result = file.createNewFile();
                    S.s("create file result:" + result);
                } catch (IOException e) {
                    e.printStackTrace();
                    S.e("create file failed:" + Log.getStackTraceString(e));
                }
            } else {
                S.s("file exists");
            }
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                outputStream.write("hello".getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                byte[] a = new byte[inputStream.available()];
                inputStream.read(a);
                S.s("read:[" + new String(a) + "]");
            } catch (IOException e) {
                S.e(Log.getStackTraceString(e));
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            File fileCopy = new File(publicDir, filename);
            if(!fileCopy.exists()){
                try {
                    fileCopy.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    S.e(Log.getStackTraceString(e));
                }
            }
            S.s("fileCopy:"+fileCopy.exists() );
            copyFile(file, fileCopy);
            S.s("copy finish:" + fileCopy);
            FileInputStream inputStreamCopy = null;
            try {
                inputStreamCopy = new FileInputStream(fileCopy);
                byte[] a = new byte[inputStreamCopy.available()];
                inputStreamCopy.read(a);
                S.s("read copy:[" + new String(a) + "]");
            } catch (IOException e) {
                S.e(Log.getStackTraceString(e));
                e.printStackTrace();
            } finally {
                if (inputStreamCopy != null) {
                    try {
                        inputStreamCopy.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            S.e("files is null");
        }
    }

    public void ssss() {
        File dir = new File("/storage/emulated/0/Android/media/com.test.test3app/");
        if (dir.exists()) {
            S.s("files:" + dir);
            File file = new File(dir, "abcd.txt");
            S.s("file:" + file.getAbsolutePath());
            if (!file.exists()) {
                S.s("file not exists");
                try {
                    boolean result = file.createNewFile();
                    S.s("create file result:" + result);
                } catch (IOException e) {
                    e.printStackTrace();
                    S.e("create file failed:" + Log.getStackTraceString(e));
                }
            } else {
                S.s("file exists");
            }
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                outputStream.write("hello".getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                byte[] a = new byte[inputStream.available()];
                inputStream.read(a);
                S.s("read:[" + new String(a) + "]");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            S.e("files is null");
        }
    }

    public static void copyFile(@NonNull File sourceFile, @NonNull File targetFile) {
        InputStream sourceStream = null;
        OutputStream targetStream = null;
        try {
            sourceStream = new BufferedInputStream(new FileInputStream(sourceFile));
            targetStream = new BufferedOutputStream(new FileOutputStream(targetFile));
            copyStream(sourceStream, targetStream);
        } catch (Throwable e) {
            S.e(Log.getStackTraceString(e));
        } finally {
            silentlyClose(sourceStream);
            silentlyClose(targetStream);
        }
    }

    public static long copyStream(@NonNull InputStream source, @NonNull OutputStream target) throws IOException {
        byte[] buffer = new byte[1024 * 8];
        int read;
        long totalBytes = 0;
        do {
            read = source.read(buffer, 0, buffer.length);
            if (read > 0) {
                target.write(buffer, 0, read);
                totalBytes += read;
            }
        } while (read > 0);
        return totalBytes;
    }

    public static void silentlyClose(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable e) {
            }
        }
    }
}