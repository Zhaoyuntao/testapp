package com.test.test3app.threadpool;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import com.zhaoyuntao.androidutils.tools.S;

import java.io.IOException;
import java.util.List;


public class ProcessUtils {
    static String sMyProcessName = null;
    static String sMyProcessTag = null;

    public static String getProcessName(Context appCxt) {
        if (sMyProcessName == null) {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) appCxt.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
            if (runningAppProcesses == null) {
                return "unknown";
            }
            for (RunningAppProcessInfo appProcess : runningAppProcesses) {
                if (appProcess.pid == pid) {
                    sMyProcessName = appProcess.processName;
                    return sMyProcessName;
                }
            }
        }

        return sMyProcessName == null ? "unknown" : sMyProcessName;
    }

    public static String getProcessName() {
        if (sMyProcessName != null) {
            return sMyProcessName;
        }

        final Context appCxt = Utilities.getApplicationContext();
        if (appCxt == null) {
            return "unknown";
        }
       return getProcessName(appCxt);
    }


    private static Process execRuntimeProcess(String command) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            S.e( "[ForceStop] exec Runtime command:" + command + ", IOException" + e);
            e.printStackTrace();
        }
        S.e( "[ForceStop] exec Runtime command:" + command + ", Process:" + p);
        return p;
    }

}
