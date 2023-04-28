package com.test.test3app.sql;

/**
 * created by zhaoyuntao
 * on 27/04/2023
 */
public class DBOperationUtils {
    public static String toString(@DBOperationCode int dbOperation) {
        switch (dbOperation) {
            case DBOperationCode.INSERT:
                return "Insert";
            case DBOperationCode.SELECT:
                return "Select";
            case DBOperationCode.UPDATE:
                return "Update";
            case DBOperationCode.REPLACE:
                return "Replace";
            case DBOperationCode.DELETE:
                return "Delete";
            default:
                return "Unknown";
        }
    }
}
