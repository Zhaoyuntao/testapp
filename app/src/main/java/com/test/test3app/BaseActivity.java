package com.test.test3app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * created by zhaoyuntao
 * on 2019-10-25
 * description:
 */
public class BaseActivity extends AppCompatActivity {

    protected Activity activity() {
        return this;
    }

    protected void goToActivity(Class<?> tClass) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent);
    }

    protected void goToActivity(Class<?> tClass, Bundle bundle) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent, bundle);
    }
}
