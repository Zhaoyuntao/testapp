package com.test.test3app.activity;

import android.os.Bundle;
import android.view.View;

import com.test.test3app.BaseActivity;
import com.test.test3app.R;
import com.test.test3app.wallpaper.AnimateImageView;


public class MainActivity_9991_animateimageview extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_9991_animateimageview);

        AnimateImageView imageView1 = findViewById(R.id.animateimage1);
        AnimateImageView imageView2 = findViewById(R.id.animateimage2);
        AnimateImageView imageView3 = findViewById(R.id.animateimage3);

        findViewById(R.id.setreceived).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.setImageResource(R.drawable.svg_message_send_success_received);
                imageView2.setImageResource(R.drawable.svg_message_send_success_received);
                imageView3.setImageResource(R.drawable.svg_message_send_success_received);
            }
        });

        findViewById(R.id.setread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.setImageResource(R.drawable.svg_message_send_success_received_and_read);
                imageView2.setImageResource(R.drawable.svg_message_send_success_received_and_read);
                imageView3.setImageResource(R.drawable.svg_message_send_success_received_and_read);
            }
        });

    }
}
