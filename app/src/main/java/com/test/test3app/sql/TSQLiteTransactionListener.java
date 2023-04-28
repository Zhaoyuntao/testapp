package com.test.test3app.sql;

import android.database.sqlite.SQLiteTransactionListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 28/04/2023
 */
public class TSQLiteTransactionListener implements SQLiteTransactionListener {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS", Locale.ENGLISH);
    private final DBTaskState state;
    private List<DBTaskState> statesWhenStartWaiting;
    private boolean dump;

    public TSQLiteTransactionListener(@NonNull DBTaskState state) {
        this.state = state;
    }

    public void setStatesWhenStartWaiting(@Nullable List<DBTaskState> statesWhenStartWaiting) {
        this.statesWhenStartWaiting = statesWhenStartWaiting;
    }

    public TSQLiteTransactionListener setDump(boolean dump) {
        this.dump = dump;
        return this;
    }

    @Override
    public void onBegin() {
        if (dump) {
            S.s("begin");
        }
        state.startOP();
        if (dump) {
            TDBStateNotifier.getInstance().notifyStartOP(state);
        }
    }

    @Override
    public void onCommit() {
        if (dump) {
            S.s("onCommit");
        }
        state.stop();
        processDumpEnd();
    }

    @Override
    public void onRollback() {
        if (dump) {
            S.s("onRollback");
        }
        state.stop();
        processDumpEnd();
    }

    private void processDumpEnd() {

        if (dump) {
            TDBStateNotifier.getInstance().notifyEnded(state);
            if (statesWhenStartWaiting != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (DBTaskState otherState : statesWhenStartWaiting) {
                    stringBuilder.append("[").append(otherState.getTag()).append("]").append(DBOperationUtils.toString(otherState.getOp()));
                    if (otherState.isWaiting()) {
                        stringBuilder.append(" [waiting]");
                    } else if (otherState.isInOPProcessing()) {
                        stringBuilder.append(" [processing]");
                    } else if (otherState.isEnd()) {
                        stringBuilder.append(" [ended]");
                    }
                    stringBuilder.append(" startWaiting: ").append(formatTime(otherState.getStartWaitingTime()))
                            .append(" startOP: ").append(formatTime(otherState.getStartOPTime()))
                            .append(" end: ").append(formatTime(otherState.getEndOPTime()))
                            .append(" waiting: ").append(otherState.getWaitingCost())
                            .append(" cost: ").append(otherState.getOPCost())
                            .append(" ms\n");
                }
                S.s(stringBuilder.toString());
                TDBStateNotifier.getInstance().notifyDumpStates(statesWhenStartWaiting, stringBuilder.toString());
            }
        }
    }

    public static String formatTime(long time) {
        return dateFormat.format(time) + "(" + time + ")";
    }
}