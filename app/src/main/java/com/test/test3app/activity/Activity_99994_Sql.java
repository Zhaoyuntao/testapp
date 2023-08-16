package com.test.test3app.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.test.test3app.R;
import com.test.test3app.sql.DBOperationUtils;
import com.test.test3app.sql.DBTaskState;
import com.test.test3app.sql.OnDBTaskStateChangeListener;
import com.test.test3app.sql.TDBMonitor;
import com.test.test3app.sql.ZEntry;
import com.test.test3app.sql.ZSqlHelper;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import base.ui.BaseActivity;

public class Activity_99994_Sql extends BaseActivity {
    ZSqlHelper zSqlHelper;
    ZSqlHelper zSqlHelper2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_main_99994_sql);
        TextView messageViewDump_db1 = findViewById(R.id.message_view_dump_db1);
        TextView messageViewDump = findViewById(R.id.message_view_dump_db2);
        TextView messageView = findViewById(R.id.message_view);
        TextView searchView = findViewById(R.id.text_view_search);

        TextView messageViewInsert_db1_table1 = findViewById(R.id.message_view_auto_insert);
        TextView messageViewInsert_db1_table2 = findViewById(R.id.message_view_auto_insert2);
        TextView messageViewInsert_db2_table1 = findViewById(R.id.message_view_auto_insert3);

        TextView messageViewSelect_db1_table1 = findViewById(R.id.message_view_auto_select);
        TextView messageViewSelect_db1_table1_ = findViewById(R.id.message_view_auto_select2);
        TextView messageViewSelect_db1_table2 = findViewById(R.id.message_view_auto_select3);
        TextView messageViewSelect_db2_table1 = findViewById(R.id.message_view_auto_select4);


        TextView select_db1_table1 = findViewById(R.id.text_view_auto_search);
        TextView select_db1_table1_ = findViewById(R.id.text_view_auto_search_table1_2);
        TextView select_db1_table2 = findViewById(R.id.text_view_auto_search_table2);
        TextView select_db2_table1 = findViewById(R.id.text_view_auto_search_table3);

        TextView insert_db1_table1 = findViewById(R.id.text_view_auto_insert);
        TextView insert_db1_table2 = findViewById(R.id.text_view_auto_insert2);
        TextView insert_db2_table1 = findViewById(R.id.text_view_auto_insert3);

        zSqlHelper = new ZSqlHelper(this, "ZEntry.db");
        zSqlHelper2 = new ZSqlHelper(this, "ZEntry2.db");
        TDBMonitor.addListener(new OnDBTaskStateChangeListener() {
            @Override
            public void onDump(@NonNull List<DBTaskState> tags, @NonNull String log) {
                runOnUiThread(() -> messageViewDump.append(log));
            }

            @Override
            public void onWaiting(@NonNull DBTaskState dbTaskState) {
                S.s("[" + dbTaskState + "] waiting: " + DBOperationUtils.toString(dbTaskState.getOp()));
            }

            @Override
            public void onStartOP(@NonNull DBTaskState dbTaskState) {
                S.s("[" + dbTaskState + "] start op: " + DBOperationUtils.toString(dbTaskState.getOp()));
            }

            @Override
            public void onEndOP(@NonNull DBTaskState dbTaskState) {
                S.s("[" + dbTaskState + "] end op: " + DBOperationUtils.toString(dbTaskState.getOp()) + ", waiting cost:" + dbTaskState.getWaitingCost() + " ms, op cost:" + dbTaskState.getOPCost() + " ms");
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageViewDump.setText("");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> map = new LinkedHashMap<>();
                        map.put(ZEntry.COLUMN_NAME_TITLE, "Hello");
                        List<ZEntry> result1 = zSqlHelper.select("S_A", ZEntry.TABLE_NAME, map, true);
                        map.put(ZEntry.COLUMN_NAME_SUBTITLE, "subHello");
                        List<ZEntry> result2 = zSqlHelper.select("S_B", ZEntry.TABLE_NAME, map, true);
                        List<ZEntry> result3 = zSqlHelper2.select("S_C", ZEntry.TABLE_NAME, null, false);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageView.setText("db1 table1 size:" + result1.size()
                                        + "\ndb1 table2 size:" + result2.size()
                                        + "\ndb2 table1 size:" + result3.size()
                                );
                            }
                        });
                    }
                }).start();
            }
        });

        insert_db1_table1.setOnClickListener(new View.OnClickListener() {
            private final Switcher insert = new Switcher();

            @Override
            public void onClick(View v) {
                if (!insert.open) {
                    insert.open = true;
                    autoAdd(zSqlHelper, "i_a", ZEntry.TABLE_NAME, messageViewInsert_db1_table1, insert);
                } else {
                    insert.open = false;
                }
            }
        });
        insert_db1_table2.setOnClickListener(new View.OnClickListener() {
            private final Switcher insert = new Switcher();

            @Override
            public void onClick(View v) {
                if (!insert.open) {
                    insert.open = true;
                    autoAdd(zSqlHelper, "i_b", ZEntry.TABLE_NAME2, messageViewInsert_db1_table2, insert);
                } else {
                    insert.open = false;
                }
            }
        });
        insert_db2_table1.setOnClickListener(new View.OnClickListener() {
            private final Switcher insert = new Switcher();

            @Override
            public void onClick(View v) {
                if (!insert.open) {
                    insert.open = true;
                    autoAdd(zSqlHelper2, "i_c", ZEntry.TABLE_NAME, messageViewInsert_db2_table1, insert);
                } else {
                    insert.open = false;
                }
            }
        });
        select_db1_table1.setOnClickListener(new View.OnClickListener() {
            private final Switcher search = new Switcher();

            @Override
            public void onClick(View v) {
                if (!search.open) {
                    search.open = true;
                    autoSearch(zSqlHelper, "s_a", ZEntry.TABLE_NAME, messageViewSelect_db1_table1, search);
                } else {
                    search.open = false;
                }
            }
        });
        select_db1_table1_.setOnClickListener(new View.OnClickListener() {
            private final Switcher search = new Switcher();

            @Override
            public void onClick(View v) {
                if (!search.open) {
                    search.open = true;
                    autoSearch(zSqlHelper, "s_b", ZEntry.TABLE_NAME, messageViewSelect_db1_table1_, search);
                } else {
                    search.open = false;
                }
            }
        });
        select_db1_table2.setOnClickListener(new View.OnClickListener() {
            private final Switcher search = new Switcher();

            @Override
            public void onClick(View v) {
                if (!search.open) {
                    search.open = true;
                    autoSearch(zSqlHelper, "s_c", ZEntry.TABLE_NAME2, messageViewSelect_db1_table2, search);
                } else {
                    search.open = false;
                }
            }
        });
        select_db2_table1.setOnClickListener(new View.OnClickListener() {
            private final Switcher search = new Switcher();

            @Override
            public void onClick(View v) {
                if (!search.open) {
                    search.open = true;
                    autoSearch(zSqlHelper2, "s_d", ZEntry.TABLE_NAME, messageViewSelect_db2_table1, search);
                } else {
                    search.open = false;
                }
            }
        });
    }

    private void autoAdd(ZSqlHelper zSqlHelper, String tag, String table, TextView messageView, Switcher switcher) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                int i = 0;
                while (switcher.open && i++ < 50000) {
                    ZEntry zEntry = new ZEntry();
                    zEntry.title = "Hello";
                    zEntry.subtitle = "subHello";
                    zEntry.time = time++;
                    zEntry.time2 = time++;
                    zSqlHelper.insert(tag, table, zEntry, false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageView.setText("inserting:" + zEntry);
                        }
                    });
                } runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageView.setText("auto inserting ended");
                    }
                });
            }
        }).start();
    }

    private void autoSearch(ZSqlHelper zSqlHelper, String tag, String table, TextView messageView, Switcher switcher) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (switcher.open) {
                    long timeStart = SystemClock.elapsedRealtime();
                    List<ZEntry> result = zSqlHelper.select(tag, table, null, false);
                    long timeEnd = SystemClock.elapsedRealtime(); runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageView.setText("size:" + result.size() + " \ncost: " + (timeEnd - timeStart));
                        }
                    });
                } runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageView.setText("");
                    }
                });
            }
        }).start();
    }

    public class Switcher {
        boolean open;
    }

    @Override
    protected void onDestroy() {
        zSqlHelper.close();
        zSqlHelper2.close();
        super.onDestroy();
    }
}
