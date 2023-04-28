package com.test.test3app.sql;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 27/04/2023
 */
@IntDef({
        DBOperationCode.INSERT,
        DBOperationCode.SELECT,
        DBOperationCode.DELETE,
        DBOperationCode.REPLACE,
        DBOperationCode.UPDATE,
})
public @interface DBOperationCode {
    int INSERT = 1;
    int SELECT = 2;
    int DELETE = 3;
    int REPLACE = 4;
    int UPDATE = 5;
}
