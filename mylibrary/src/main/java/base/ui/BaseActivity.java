package base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;

import com.doctor.mylibrary.R;
import com.zhaoyuntao.androidutils.tools.SP;

/**
 * created by zhaoyuntao
 * on 2019-10-25
 * description:
 */
public class BaseActivity extends AppCompatActivity {
    private SP sp = new SP();

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
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.base_activity);
        FrameLayout viewGroup = findViewById(R.id.child_container);
        View child = LayoutInflater.from(activity()).inflate(layoutResID, null);
        viewGroup.addView(child, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        //Switch
        ZSwitchButton switchCompat = findViewById(R.id.dark_mode_switch);
        switchCompat.setClickable(false);
        switchCompat.setChecked(AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO);
        switchCompat.setOnCheckedChangeListener(new ZSwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ZSwitchButton buttonView, boolean isChecked) {
                changeDark(isChecked);
                sp.write("night", isChecked, activity());
            }
        });
        //Arrow
        AppCompatImageView arrowView = findViewById(R.id.arrow_back);
        arrowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
}
