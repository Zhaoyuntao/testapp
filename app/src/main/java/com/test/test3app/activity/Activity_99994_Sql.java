package com.test.test3app.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.test.test3app.R;
import com.test.test3app.mention.ValueSafeTransfer;
import com.test.test3app.sql.DBTaskState;
import com.test.test3app.sql.FTSDataHelper;
import com.test.test3app.sql.OnDBTaskStateChangeListener;
import com.test.test3app.sql.TDBMonitor;
import com.test.test3app.sql.ZEntry;
import com.test.test3app.sql.ZSqlHelper;

import java.util.List;

import base.ui.BaseActivity;
import im.turbo.utils.log.S;

public class Activity_99994_Sql extends BaseActivity {
    ZSqlHelper zSqlHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_main_99994_sql);

        TextView allResult = findViewById(R.id.view_result_all);

        TextView likeResultShort = findViewById(R.id.view_insert_result_table_short);
        TextView likeResultLong = findViewById(R.id.view_insert_result_table_long);
        TextView likeResultMix = findViewById(R.id.view_insert_result_table_mix);
        TextView insertTableShort = findViewById(R.id.view_insert_table_short);
        TextView insertTableLong = findViewById(R.id.view_insert_table_long);
        TextView insertTableMix = findViewById(R.id.view_insert_table_mix);
        TextView like = findViewById(R.id.view_search_like);

        TextView ftsResultShort = findViewById(R.id.view_insert_result_fts_short);
        TextView ftsResultLong = findViewById(R.id.view_insert_result_fts_long);
        TextView ftsResultMix = findViewById(R.id.view_insert_result_fts_mix);
        TextView insertFtsShort = findViewById(R.id.view_insert_fts_short);
        TextView insertFtsLong = findViewById(R.id.view_insert_fts_long);
        TextView insertFtsMix = findViewById(R.id.view_insert_fts_mix);
        TextView fts = findViewById(R.id.view_search_fts);

        EditText editText = findViewById(R.id.search_content);

        zSqlHelper = new ZSqlHelper(this, "ZEntry.db");
        TDBMonitor.addListener(new OnDBTaskStateChangeListener() {
            @Override
            public void onDump(@NonNull List<DBTaskState> tags, @NonNull String log) {
//                S.s("log:" + log);
                for (DBTaskState state : tags) {
                    S.s("[" + state.getTag() + "]  " + state.getCost() + " ms, " + state.getLog());
                }
                runOnUiThread(() -> allResult.append(log));
            }

            @Override
            public void onWaiting(@NonNull DBTaskState dbTaskState) {
//                S.s("[" + dbTaskState + "] waiting: " + DBOperationUtils.toString(dbTaskState.getOp()));
            }

            @Override
            public void onStartOP(@NonNull DBTaskState dbTaskState) {
//                S.s("[" + dbTaskState + "] start op: " + DBOperationUtils.toString(dbTaskState.getOp()));
            }

            @Override
            public void onEndOP(@NonNull DBTaskState dbTaskState) {
//                S.s("[" + dbTaskState + "] end op: " + DBOperationUtils.toString(dbTaskState.getOp()) + ", waiting cost:" + dbTaskState.getWaitingCost() + " ms, op cost:" + dbTaskState.getOPCost() + " ms");
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = editText.getText();
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                String searchContent = text.toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<ZEntry> result1 = zSqlHelper.search(ZEntry.TABLE_NAME, ZEntry.TABLE_NAME, searchContent, true);
                        S.s(ZEntry.TABLE_NAME + "--------------------------------------------------[count: " + result1.size() + "]");
//                        ValueSafeTransfer.iterateAll(result1, (position, zEntry) -> S.s("(" + position + ")[" + searchContent + "]:" + zEntry.content));
                        List<ZEntry> result2 = zSqlHelper.search(ZEntry.TABLE_NAME2, ZEntry.TABLE_NAME2, searchContent, true);
                        S.s(ZEntry.TABLE_NAME2 + "--------------------------------------------------[count: " + result2.size() + "]");
                        List<ZEntry> result3 = zSqlHelper.search(ZEntry.TABLE_NAME3, ZEntry.TABLE_NAME3, searchContent, true);
                        S.s(ZEntry.TABLE_NAME3 + "--------------------------------------------------[count: " + result3.size() + "]");
//                        ValueSafeTransfer.iterateAll(result2, (position, zEntry) -> S.s("(" + position + ")[" + searchContent + "]:" + zEntry.content));
                    }
                }).start();
            }
        });
        fts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = editText.getText();
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                String searchContent = text.toString();
                allResult.setText("");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        List<ZEntry> result1 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS1, ZEntry.TABLE_NAME_FTS1, searchContent, true);
//                        S.s(ZEntry.TABLE_NAME_FTS1 + "--------------------------------------------------[count: " + result1.size() + "]");
//                        List<ZEntry> result2 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS2, ZEntry.TABLE_NAME_FTS2, searchContent, true);
//                        S.s(ZEntry.TABLE_NAME_FTS2 + "--------------------------------------------------[count: " + result2.size() + "]");
                        List<ZEntry> result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, searchContent, true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "좋", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "좋*", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "좋은", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "*은", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "좋은 아", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "좋은 아침", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "좋은 아침이", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "좋은 아침이에", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "좋은 아침이에요", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "은 아침이에요", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "아침이에요", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "침이에요", true, 1000);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "이에요", true, 1000);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "에요", true, 1000);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "요", true, 1000);
//
//
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "W*", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "What", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "What*", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "Whats", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "Whats that", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "Whats that means", true, -1);
//
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "你", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "你*", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "如果", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "如果*", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "如果根据", true, 1000);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "如果根据*", true, 1000);
//
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "h*", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "hell", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "*e*", true, 10);
                        ValueSafeTransfer.iterateAll(result3, (position, zEntry) -> S.s("(" + position + ")[" + searchContent + "]:   content:" + zEntry.content));
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "e* l* l*", true, 10);
                        ValueSafeTransfer.iterateAll(result3, (position, zEntry) -> S.s("(" + position + ")[" + searchContent + "]:   content:" + zEntry.content));

//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "H* e*", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "ض", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "ص", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "ث", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "ف", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "ع", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "根*", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "撒", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "旷", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "根", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "思", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "根*", true, -1);
                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "果", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "是什么", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "序", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "你", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "你*", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "如果", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "如果*", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "如果根据", true, -1);
//                        result3 = zSqlHelper.fts(ZEntry.TABLE_NAME_FTS3, ZEntry.TABLE_NAME_FTS3, "如果根据*", true, -1);
//                        S.s(ZEntry.TABLE_NAME_FTS3 + "--------------------------------------------------[count: " + result3.size() + "]");

//                        ValueSafeTransfer.iterateAll(result1, (position, zEntry) -> S.s("(" + position + ")[" + searchContent + "]:   content:" + zEntry.content + "  content2:" + zEntry.content2));
//                        ValueSafeTransfer.iterateAll(result2, (position, zEntry) -> S.s("(" + position + ")[" + searchContent + "]:   content:" + zEntry.content + "  content2:" + zEntry.content2));
//                        ValueSafeTransfer.iterateAll(result3, (position, zEntry) -> S.s("(" + position + ")[" + searchContent + "]:   content:" + zEntry.content));
                    }
                }).start();
            }
        });

        insertTableShort.setOnClickListener(new View.OnClickListener() {
            private final Switcher insert = new Switcher();

            @Override
            public void onClick(View v) {
                if (!insert.open) {
                    insert.open = true;
                    autoAdd(zSqlHelper, false, false, ZEntry.TABLE_NAME, likeResultShort, insert);
                } else {
                    insert.open = false;
                }
            }
        });
        insertTableLong.setOnClickListener(new View.OnClickListener() {
            private final Switcher insert = new Switcher();

            @Override
            public void onClick(View v) {
                if (!insert.open) {
                    insert.open = true;
                    autoAdd(zSqlHelper, true, false, ZEntry.TABLE_NAME2, likeResultLong, insert);
                } else {
                    insert.open = false;
                }
            }
        });
        insertTableMix.setOnClickListener(new View.OnClickListener() {
            private final Switcher insert = new Switcher();

            @Override
            public void onClick(View v) {
                if (!insert.open) {
                    insert.open = true;
                    autoAdd(zSqlHelper, null, false, ZEntry.TABLE_NAME3, likeResultMix, insert);
                } else {
                    insert.open = false;
                }
            }
        });
        insertFtsShort.setOnClickListener(new View.OnClickListener() {
            private final Switcher insert = new Switcher();

            @Override
            public void onClick(View v) {
                if (!insert.open) {
                    insert.open = true;
                    autoAdd(zSqlHelper, false, true, ZEntry.TABLE_NAME_FTS1, ftsResultShort, insert);
                } else {
                    insert.open = false;
                }
            }
        });
        insertFtsLong.setOnClickListener(new View.OnClickListener() {
            private final Switcher insert = new Switcher();

            @Override
            public void onClick(View v) {
                if (!insert.open) {
                    insert.open = true;
                    autoAdd(zSqlHelper, true, true, ZEntry.TABLE_NAME_FTS2, ftsResultLong, insert);
                } else {
                    insert.open = false;
                }
            }
        });
        insertFtsMix.setOnClickListener(new View.OnClickListener() {
            private final Switcher insert = new Switcher();

            @Override
            public void onClick(View v) {
                if (!insert.open) {
                    insert.open = true;
                    autoAdd(zSqlHelper, null, true, ZEntry.TABLE_NAME_FTS3, ftsResultMix, insert);
                } else {
                    insert.open = false;
                }
            }
        });

        getCount(zSqlHelper, ZEntry.TABLE_NAME_FTS1, ftsResultShort, insertFtsShort);
        getCount(zSqlHelper, ZEntry.TABLE_NAME_FTS2, ftsResultLong, insertFtsLong);
        getCount(zSqlHelper, ZEntry.TABLE_NAME_FTS3, ftsResultMix, insertFtsMix);
    }

    private void getCount(ZSqlHelper zSqlHelper, String table, TextView messageView, TextView insertView) {
        insertView.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                runOnUiThread(() -> messageView.setText("Start get count..."));
                long count = zSqlHelper.ftsCount(table, table);
                String info = "Count : " + count + " cost:" + (System.currentTimeMillis() - time) + " ms";
                runOnUiThread(() -> {
                    messageView.setText(info);
                    insertView.setEnabled(true);
                });
            }
        }).start();
    }

    private void autoAdd(ZSqlHelper zSqlHelper, Boolean longData, boolean fts, String table, TextView messageView, Switcher switcher) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                runOnUiThread(() -> messageView.setText("Start get count..."));
                long i = zSqlHelper.ftsCount(table, table);
                while (switcher.open && i++ < 10000) {
                    ZEntry zEntry = new ZEntry();
                    zEntry.name = "Name";
                    if (longData == null) {
                        zEntry.content = FTSDataHelper.getRandomData();
                    } else {
                        zEntry.content = FTSDataHelper.getRandomData(longData);
                    }
                    zEntry.content2 = FTSDataHelper.getRandomData(false);
                    zEntry.time = time + 1;
                    zEntry.time2 = time + 1;
                    if (fts) {
                        zSqlHelper.insertFts(table, table, zEntry, false);
                    } else {
                        zSqlHelper.insert(table, table, zEntry, false);
                    }
                    String info = "Inserting:" + i + " cost:" + (System.currentTimeMillis() - time) + " ms";
                    runOnUiThread(() -> messageView.setText(info));
                }
                switcher.open = false;
                long count = zSqlHelper.ftsCount(table, table);
                String info = "Inserting ended: " + count + " cost:" + (System.currentTimeMillis() - time) + " ms";
                runOnUiThread(() -> messageView.setText(info));
            }
        }).start();
    }

    public class Switcher {
        boolean open;
    }

    @Override
    protected void onDestroy() {
        zSqlHelper.close();
        super.onDestroy();
    }
}
