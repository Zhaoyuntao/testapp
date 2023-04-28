package com.test.test3app.sql;

import androidx.annotation.NonNull;

/**
 * created by zhaoyuntao
 * on 27/04/2023
 */
public class DBTaskState {
    private final String tag;
    @DBOperationCode
    private final int op;
    private long startWaitingTime;
    private long startOPTime;
    private long endOPTime;

    DBTaskState(@NonNull String tag, @DBOperationCode int op) {
        this.tag = tag;
        this.op = op;
    }

    void startWaiting() {
        startWaitingTime = System.currentTimeMillis();
    }

    void startOP() {
        startOPTime = System.currentTimeMillis();
    }

    void stop() {
        endOPTime = System.currentTimeMillis();
    }

    public long getStartOPTime() {
        return startOPTime;
    }

    public long getEndOPTime() {
        return endOPTime;
    }

    public long getStartWaitingTime() {
        return startWaitingTime;
    }

    public long getOPCost() {
        return endOPTime > 0 ? endOPTime - startOPTime : -1;
    }

    public long getWaitingCost() {
        return startOPTime > 0 ? startOPTime - startWaitingTime : -1;
    }

    public long getCost() {
        return endOPTime > 0 ? endOPTime - startWaitingTime : -1;
    }

    public int getOp() {
        return op;
    }

    public String getTag() {
        return tag;
    }

    public boolean isEnd() {
        return endOPTime > 0;
    }

    public boolean isWaiting() {
        return startOPTime <= 0 && startWaitingTime > 0;
    }

    public boolean isInOPProcessing() {
        return startOPTime > 0 && endOPTime <= 0;
    }

    @Override
    public String toString() {
        return "[" + tag + "-" + DBOperationUtils.toString(op) + "]";
    }
}
