package base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.doctor.mylibrary.R;
import com.zhaoyuntao.androidutils.tools.SP;

/**
 * created by zhaoyuntao
 * on 2019-10-25
 * description:
 */
public class BaseActivity extends AppCompatActivity {
    private SP sp = new SP();
    private TextView titleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context context) {
        changeDark(sp.getBoolean(context, "night", false));
        super.attachBaseContext(context);
    }

    private void changeDark(boolean night) {
        if (night) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    final public void setContentView(int layoutResID) {
        super.setContentView(R.layout.base_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setContentInsetsRelative(0, 0);
        setSupportActionBar(toolbar);
        int customToolbarLayoutRes = getCustomToolbarLayoutRes();
        if (customToolbarLayoutRes != -1) {
            getSupportActionBar().setCustomView(LayoutInflater.from(this).inflate(customToolbarLayoutRes, toolbar, false), new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            getSupportActionBar().setDisplayShowCustomEnabled(true);
        }
        initDefaultCustomView();
        FrameLayout viewGroup = findViewById(R.id.child_container);
        View child = LayoutInflater.from(activity()).inflate(layoutResID, null);
        viewGroup.addView(child, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    private void initDefaultCustomView() {
        //Switch
        ZSwitchButton switchCompat = findViewById(R.id.dark_mode_switch);
        if (switchCompat == null) {
            return;
        }
        titleView = findViewById(R.id.title_toolbar);
        setTitle(getClass().getSimpleName());
        switchCompat.setClickable(false);
        switchCompat.setChecked(AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO);
        switchCompat.setOnCheckedChangeListener(new ZSwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ZSwitchButton buttonView, boolean isChecked) {
                changeTheme(isChecked);
            }
        });
    }

    final protected void changeTheme(boolean isDark) {
        if (isDark && AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            return;
        }
        changeDark(isDark);
        sp.write("night", isDark, activity());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    final protected void updateToolBarMenu() {
        invalidateOptionsMenu();
    }

    protected int getCustomToolbarLayoutRes() {
        return R.layout.base_custom_toolbar;
    }

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

    @Override
    public void setTitle(CharSequence title) {
        if (titleView != null) {
            titleView.setText(title);
        }
    }
}
