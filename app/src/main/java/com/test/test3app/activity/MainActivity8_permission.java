package com.test.test3app.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.S;


public class MainActivity8_permission extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity8_permission);
        button = findViewById(R.id.permission);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("1");
//Note: I have placed this code in onResume for demostration purpose. Be careful when you use it in
                // production code
                if (ContextCompat.checkSelfPermission(MainActivity8_permission.this, Manifest.permission.READ_CALENDAR) != PackageManager
                        .PERMISSION_GRANTED) {
                    //You can show permission rationale if shouldShowRequestPermissionRationale() returns true.
                    //I will skip it for this demo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (neverAskAgainSelected(MainActivity8_permission.this, Manifest.permission.READ_CALENDAR)) {
                            displayNeverAskAgainDialog();
                        } else {
                            ActivityCompat.requestPermissions(MainActivity8_permission.this, new String[]{Manifest.permission.READ_CALENDAR},
                                    SEND_SMS_PERMISSION_REQUEST_CODE);
                        }
                    }

                }
            }
        });
        final TextView mTextView = findViewById(R.id.s1);

//        mTextView.setRtlSupport(true);
//        mTextView.setTextColor(getResources().getColorNormal(R.color.yc_color_000000_CB));
//        float size = getResources().getDimension(R.dimen.dimen_17sp);
//        mTextView.setTextSizeDp(TypedValue.COMPLEX_UNIT_PX, size);
//        mTextView.setEmojiconSize(B.dip2px(this,42));
//        mTextView.setAutoLinkMask(Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);

        EditText emojiconEditText = findViewById(R.id.s2);
        emojiconEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextView.setText(s);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean neverAskAgainSelected(final AppCompatActivity activity, final String permission) {
        final boolean prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission);
        final boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);
        S.s("prevShouldShowStatus:" + prevShouldShowStatus + "  currShouldShowStatus:" + currShouldShowStatus);
        return prevShouldShowStatus != currShouldShowStatus;
    }

    public static void setShouldShowStatus(final Context context, final String permission) {
        SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = genPrefs.edit();
        editor.putBoolean(permission, true);
        editor.commit();
    }

    public static boolean getRatinaleDisplayStatus(final Context context, final String permission) {
        SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        return genPrefs.getBoolean(permission, false);
    }

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1000;
    private static final String TAG = "TEST";

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need to send SMS for performing necessary task. Please permit the permission through "
                + "Settings screen.\n\nSelect Permissions -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (SEND_SMS_PERMISSION_REQUEST_CODE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                S.s("Permission granted successfully");
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_LONG).show();
            } else {
                S.s("failed");
                setShouldShowStatus(this, Manifest.permission.READ_CALENDAR);
            }
        }
    }
}
