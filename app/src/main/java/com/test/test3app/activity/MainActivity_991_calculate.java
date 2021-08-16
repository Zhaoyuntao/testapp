package com.test.test3app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.S;

public class MainActivity_991_calculate extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calcalate);

        TextView textViewAll = findViewById(R.id.textview_all);
        TextView textView1 = findViewById(R.id.textview_1);
        TextView textView2 = findViewById(R.id.textview_2);
        TextView textView3 = findViewById(R.id.textview_3);
        TextView textView4 = findViewById(R.id.textview_4);
        TextView textView5 = findViewById(R.id.textview_5);
        TextView textView6 = findViewById(R.id.textview_6);

        EditText editTextAll = findViewById(R.id.edittext_all);
        EditText editText1 = findViewById(R.id.edittext_1);
        EditText editText2 = findViewById(R.id.edittext_2);
        EditText editText3 = findViewById(R.id.edittext_3);
        EditText editText4 = findViewById(R.id.edittext_4);
        EditText editText5 = findViewById(R.id.edittext_5);
        EditText editText6 = findViewById(R.id.edittext_6);

        editTextAll.setText("105000");
        editText1.setText("1.1764");
        editText2.setText("1.0526");
        editText3.setText("0.9368");
        editText4.setText("0.9368");
        editText5.setText("0.8987");
        editText6.setText("0.8987");

        TextView resultView = findViewById(R.id.result);

        findViewById(R.id.button_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextAll.setText("105000");
                editText1.setText("1.1764");
                editText2.setText("1.0526");
                editText3.setText("0.9368");
                editText4.setText("0.9368");
                editText5.setText("0.8987");
                editText6.setText("0.8987");
            }
        });
        findViewById(R.id.button_clearall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
                editText5.setText("");
                editText6.setText("");
            }
        });

        findViewById(R.id.button_calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float all = getNumber(editTextAll) / 13f;
                float a1 = getNumber(editText1);
                float a2 = getNumber(editText2);
                float a3 = getNumber(editText3);
                float a4 = getNumber(editText4);
                float a5 = getNumber(editText5);
                float a6 = getNumber(editText6);

                float weight_5 = a1 + a2 + a3 + a4 + a5;
                float weight_6 = a1 + a2 + a3 + a4 + a5 + a6;
                S.s("weight_5:" + weight_5 + " weight_6:" + weight_6);
                String result;
                if (weight_5 > 0) {
                    int m1_5p = Math.round(all * (a1 / weight_5));
                    int m2_5p = Math.round(all * (a2 / weight_5));
                    int m3_5p = Math.round(all * (a3 / weight_5));
                    int m4_5p = Math.round(all * (a4 / weight_5));
                    int m5_5p = Math.round(all * (a5 / weight_5));

                    result = "5人:"
                            + "\nme:\nYear:  " + m1_5p * 13 + "      month:  " + (m1_5p)
                            + "\nchunlin:\nYear:  " + m2_5p * 13 + "      month:  " + (m2_5p)
                            + "\ntianning:\nYear:  " + m3_5p * 13 + "      month:  " + (m3_5p)
                            + "\nhongzhan:\nYear:  " + m4_5p * 13 + "      month:  " + (m4_5p)
                            + "\nyiming:\nYear:  " + m5_5p * 13 + "      month:  " + (m5_5p)
                            + "\nall：" + (m1_5p * 13 + m2_5p * 13 + m3_5p * 13 + m4_5p * 13 + m5_5p * 13) + "      " + (m1_5p + m2_5p + m3_5p + m4_5p + m5_5p)
                    ;
                } else {
                    result = "5人:\nweight is 0";
                }
                String result2;
                if (weight_6 > 0) {
                    int m1_6p = Math.round(all * (a1 / weight_6));
                    int m2_6p = Math.round(all * (a2 / weight_6));
                    int m3_6p = Math.round(all * (a3 / weight_6));
                    int m4_6p = Math.round(all * (a4 / weight_6));
                    int m5_6p = Math.round(all * (a5 / weight_6));
                    int m6_6p = Math.round(all * (a6 / weight_6));

                    result2 = "6人:"
                            + "\nme:\nYear:  " + m1_6p * 13 + "      month:  " + (m1_6p)
                            + "\nchunlin:\nYear:  " + m2_6p * 13 + "      month:  " + (m2_6p)
                            + "\ntianning:\nYear:  " + m3_6p * 13 + "      month:  " + (m3_6p)
                            + "\nhongzhan:\nYear:  " + m4_6p * 13 + "      month:  " + (m4_6p)
                            + "\nyiming:\nYear:  " + m5_6p * 13 + "      month:  " + (m5_6p)
                            + "\n6:\nYear:  " + m6_6p * 13 + "      month:  " + (m6_6p)
                            + "\nall：" + (m1_6p * 13 + m2_6p * 13 + m3_6p * 13 + m4_6p * 13 + m5_6p * 13 + m6_6p * 13) + "      " + (m1_6p + m2_6p + m3_6p + m4_6p + m5_6p + m6_6p)
                    ;
                } else {
                    result2 = "6人:\nweight is 0";
                }

                resultView.setText(
                        result + "\n\n" + result2
                );
            }
        });
    }

    private float getNumber(EditText editText) {
        if (editText == null || TextUtils.isEmpty(editText.getText())) {
            return 0;
        }
        return Float.parseFloat(editText.getText().toString());
    }
}