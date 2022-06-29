package com.test.test3app.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.test.test3app.R;
import com.test.test3app.pay.PaymentCell;

public class Activity_6_pay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity6_pay);

        PaymentCell paymentCell1 = findViewById(R.id.pay1);
        paymentCell1.setTitle("Recharge");
        paymentCell1.addDetail("Payment Method", "PY Trade");
        paymentCell1.addDetail("Order NO.", "FABX26238943247937");
        paymentCell1.addDetail("Time", "11/22/2019 8:45AM");

        PaymentCell paymentCell2 = findViewById(R.id.pay2);
        paymentCell2.setContentType(PaymentCell.TYPE_TEXT);
        paymentCell2.setHeadText("Payment password changed successfully.");
        paymentCell2.setTitle("Security");
        paymentCell2.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell2.addDetail("Note", "A apple a day keeps\nthe doctors away");
        paymentCell2.addDetail("Order NO.", "FABX26238943247937");

        PaymentCell paymentCell3 = findViewById(R.id.pay3);
        paymentCell3.setTitle("Recharge");
        paymentCell3.addDetail("Payment Method", "PY Trade");
        paymentCell3.addDetail("Order NO.", "FABX26238943247937");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
        paymentCell3.addDetail("Time", "11/22/2019 8:45AM");
    }
}
