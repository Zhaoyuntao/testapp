package com.test.test3app.threadpool;

import android.util.Log;

import com.test.test3app.BuildConfig;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Time: 2019/9/15
 * Author Hay
 */
public class ThreadManager {
    private static final String TAG = "ThreadManager";
    private final static EasyThread io;
    private final static EasyThread scheduleIo;
    private final static EasyThread cache;
    private final static EasyThread calculator;
    private final static EasyThread file;
    private final static EasyThread dataBase;
    private final static DefaultCallback callback = BuildConfig.DEBUG ? new DefaultCallback() : null;

    public static EasyThread getIO() {
        return io;
    }

    public static EasyThread getScheduleIo() {
        return scheduleIo;
    }

    public static EasyThread getCache() {
        return cache;
    }

    public static EasyThread getCalculator() {
        return calculator;
    }

    public static EasyThread getFile() {
        return file;
    }

    /**
     * Only use for db init
     * @return
     */
    public static EasyThread getDataBase() {
        return dataBase;
    }

    public static EasyThread newSingle(String name) {
        return EasyThread.Builder.createSingle().setName(name).setCallback(callback).build();
    }

    static {
        int CPU_CORES=DevicesUtils.getNumCores();
        int coreSize = DevicesUtils.lowPhysicalMemoryDevices() ? CPU_CORES + 1 : CPU_CORES * 2;
        io = EasyThread.Builder.createFixed(coreSize * 3).setName("IO").setPriority(7).setCallback(callback).build();
        cache = EasyThread.Builder.createCacheable().setName("cache").setCallback(callback).build();
        calculator = EasyThread.Builder.createFixed(coreSize + 1).setName("calculator").setPriority(Thread.MAX_PRIORITY).setCallback(callback).build();
        file = EasyThread.Builder.createFixed(4).setName("file").setPriority(3).setCallback(callback).build();
        dataBase = EasyThread.Builder.createFixed(6).setName("dataBase").setPriority(Thread.MAX_PRIORITY).setCallback(callback).build();
        scheduleIo = EasyThread.Builder.createScheduled(coreSize * 3).setName("scheduleIo").setPriority(7).setCallback(callback).build();
    }

    private static class DefaultCallback implements Callback {

        @Override
        public void onError(String threadName, Throwable t) {
        }

        @Override
        public void onCompleted(String threadName) {
            showActvieCount("onCompleted");
        }

        @Override
        public void onStart(String threadName) {

        }
    }

    private static void showActvieCount(String tag) {
        if (BuildConfig.DEBUG) {
            String tmp = TAG + " " + tag;
            boolean isApp = ProcessUtils.getProcessName().equals(ResourceUtils.getApplicationContext());
            Log.i(tmp, "io thread active count:" + getActiveCount(io) + "  " + getPoolSize(io) + " " + isApp);
            Log.i(tmp, "scheduleIo thread active count:" + getActiveCount(scheduleIo) + "  " + getPoolSize(scheduleIo) + " " + isApp);
            Log.i(tmp, "cache thread active count:" + getActiveCount(cache) + "  " + getPoolSize(cache) + " " + isApp);
            Log.i(tmp, "calculator thread active count:" + getActiveCount(calculator) + "  " + getPoolSize(calculator) + " " + isApp);
            Log.i(tmp, "file thread active count:" + getActiveCount(file) + "  " + getPoolSize(file) + " " + isApp);
            Log.i(tmp, "dataBase thread active count:" + getActiveCount(dataBase) + "  " + getPoolSize(dataBase) + " " + isApp);
        }
    }

    private static int getActiveCount(EasyThread easyThread) {
        return ((ThreadPoolExecutor) easyThread.getExecutor()).getActiveCount();
    }

    private static int getPoolSize(EasyThread easyThread) {
        return ((ThreadPoolExecutor) easyThread.getExecutor()).getPoolSize();
    }
}
