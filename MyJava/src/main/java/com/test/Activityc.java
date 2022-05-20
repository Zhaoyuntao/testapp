package com.test;

import com.zhaoyuntao.myjava.S;

/**
 * created by zhaoyuntao
 * on 18/05/2022
 * description:
 */
class Activityc {
    public Context context;
    private Callback callback;

    Activityc() {
        context = new Context();
    }

    public void show() {
        Tool.process(getCallback());
    }

    private Callback getCallback() {
        if (callback == null) {
            callback = new Callback() {
                @Override
                public void onSuccess() {
                    S.s("=====>");
                }
            };
        }
        return callback;
    }

    public void destroy() {
        callback = null;
    }
}
