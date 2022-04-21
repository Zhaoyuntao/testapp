package com.test.test3app.threadpool;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileFilter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


/**
 * Time: 2019/9/15
 * Author Hay
 */
public class ThreadManager {

    private static final int CALCULATOR_MAX_ALIVE_TIME = 5_000;
    private static final int DB_MAX_ALIVE_TIME = 15_000;
    private static final int IO_MAX_TIME_MS = 30_000;
    private static final int NETWORK_MAX_TIME_MS = 60_000;
    private static final int FILE_TRANSFER_MAX_TIME_MS = 60_000 * 10;
    private static final int LEGACY_MAX_TIME_MS = 60_000;

    private final MyThreadPoolExecutor calculator;
    private final MyThreadPoolExecutor db;

    private final MyThreadPoolExecutor io;
    //    private final MyThreadPoolExecutor network;
//    private final MyThreadPoolExecutor fileUpload;
//    private final MyThreadPoolExecutor fileDownload;
    private static int numOfCpuCores = -1;

    private static final ThreadManager instance = new ThreadManager();

    public static ThreadManager getInstance() {
        return instance;
    }


    public MyThreadPoolExecutor getCalculator() {
        return calculator;
    }


    public MyThreadPoolExecutor getDb() {
        return db;
    }

    MyThreadPoolExecutor getIo() {
        return io;
    }


    private ThreadManager() {
        int cpuCoreSize = getNumCores();
        if (cpuCoreSize <= 1) {
            cpuCoreSize = 2;//保证低配设备线程池至少有2个核心池
        }

        boolean lowDevice = cpuCoreSize <= 2;
        int coreSize = 0;//低配设备核心池数量为cpuCoreSize，高配设备为4
        //计算任务线程池，大小为coreSize+1
        int maxPoolSizeFactor = Math.max(coreSize, cpuCoreSize);//比coreSize大
        calculator = createPool("TPCalculator", Thread.NORM_PRIORITY, 1, maxPoolSizeFactor, maxPoolSizeFactor);
        //db和io线程池大小一致，只是db线程优先级比io线程池高，核心数为coreSize，最大核心数为coreSize*2
        db = createPool("TPDb", Thread.NORM_PRIORITY, coreSize, maxPoolSizeFactor * 2, maxPoolSizeFactor);
        io = createPool("TPIo", Thread.NORM_PRIORITY - 1, coreSize, maxPoolSizeFactor * 2, maxPoolSizeFactor);
    }

    private static MyThreadPoolExecutor createFixedPool(@NonNull String name, int priority,
                                                        int poolSize) {
        return new MyThreadPoolExecutor(name, priority,
                poolSize, poolSize, 30L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }


    private static MyThreadPoolExecutor createPool(@NonNull String name, int priority,
                                                   int coreSize, int maxSize, int fallbackPoolSize) {
        MyThreadPoolExecutor fallbackExecutor = createFixedPool(name + "Fallback",
                priority, fallbackPoolSize);
        fallbackExecutor.allowCoreThreadTimeOut(true);
        return new MyThreadPoolExecutor(name, priority, coreSize, maxSize,
                60L, TimeUnit.SECONDS, fallbackExecutor);
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        if (numOfCpuCores > 0) {
            return numOfCpuCores;
        }

        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            numOfCpuCores = files.length > 0 ? files.length : 1;
        } catch (Throwable e) {
            numOfCpuCores = -1;
        }

        if (numOfCpuCores <= 0) {
            numOfCpuCores = Runtime.getRuntime().availableProcessors();
        }

        if (numOfCpuCores <= 0) {
            //Default to return 1 core
            numOfCpuCores = 1;
        }

        return numOfCpuCores;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("calculator ActiveCount = ");
        builder.append(calculator.getActiveCount());
        builder.append("\n");
        builder.append("calculator PoolSize = ");
        builder.append(calculator.getPoolSize());
        builder.append("\n");
        builder.append("calculator CompletedTaskCount = ");
        builder.append(calculator.getCompletedTaskCount());
        builder.append("\n");
        builder.append("-----------------------------\n");
        builder.append("db ActiveCount = ");
        builder.append(db.getActiveCount());
        builder.append("\n");
        builder.append("db PoolSize = ");
        builder.append(db.getPoolSize());
        builder.append("\n");
        builder.append("db CompletedTaskCount = ");
        builder.append(db.getCompletedTaskCount());
        builder.append("\n");
        builder.append("-----------------------------\n");
        builder.append("io ActiveCount = ");
        builder.append(io.getActiveCount());
        builder.append("\n");
        builder.append("io PoolSize = ");
        builder.append(io.getPoolSize());
        builder.append("\n");
        builder.append("io CompletedTaskCount = ");
        builder.append(io.getCompletedTaskCount());
        builder.append("\n");
        builder.append("-----------------------------\n");
        builder.append("\n\n\n\n");
        return builder.toString();
    }
}
