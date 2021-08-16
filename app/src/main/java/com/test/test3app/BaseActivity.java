package com.test.test3app;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

/**
 * created by zhaoyuntao
 * on 2019-10-25
 * description:
 */
public class BaseActivity extends AppCompatActivity {

    protected Context activity() {
        return this;
    }

    protected void goToActivity(Class<?> tClass) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent);
    }
}
