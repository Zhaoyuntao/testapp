package com.test.test3app.activity;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;
import androidx.core.graphics.drawable.IconCompat;

import com.test.test3app.R;
import com.test.test3app.notification.MyNotificationReceiver;
import com.zhaoyuntao.androidutils.tools.T;

import java.util.UUID;

import base.ui.BaseActivity;

public class Activity_2_notification extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        NotificationManagerCompat manager = NotificationManagerCompat.from(activity());


        findViewById(R.id.start_notification).setOnClickListener(new View.OnClickListener() {
            int code=1;
            @Override
            public void onClick(View v) {
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                String uuid = UUID.randomUUID().toString();
                NotificationChannelCompat channel = new NotificationChannelCompat.Builder(uuid, NotificationManagerCompat.IMPORTANCE_DEFAULT)
                        .setName("helloChannelName")
                        .setVibrationEnabled(true).setDescription("Hello123456").build();
                manager.createNotificationChannel(channel);

                PendingIntent pendingIntent;
                Intent intent = new Intent(activity(), MyNotificationReceiver.class);
                intent.putExtra("hello","hello2");
                intent.setAction(MyNotificationReceiver.action);
//                    PendingIntent.getBroadcast(activity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getBroadcast(activity(),
                            code++,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                } else {
                    pendingIntent = PendingIntent.getBroadcast(activity(),
                            1,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                }

                Person person = new Person.Builder()
                        .setName("Abc")
                        .setIcon(IconCompat.createWithResource(activity(),R.drawable.icon_del))
//                .setBot(true)
                        .setKey(System.currentTimeMillis() + "")
                        .setImportant(true)
                        .build();

                NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(person);
                messagingStyle.addMessage("tickerText", System.currentTimeMillis(), (Person) null);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(activity(), uuid)
                        .setSmallIcon(R.drawable.icon_love)
                        .setTicker("tickerText")
                        .setWhen(System.currentTimeMillis())
                        .setContentText("Content text")
                        .setContentTitle("Content title")
                        .setAutoCancel(true)
                        .setGroup("hello")
                        .setStyle(messagingStyle)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                            .setContentIntent(PendingIntent.getActivity(activity(), 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT))
                        .addAction(
                                new NotificationCompat.Action.Builder(R.drawable.icon_del, "MyAction", pendingIntent)
                                        .addRemoteInput(new RemoteInput.Builder("HELLO_KEY").setLabel("hello label").build())
                                        .setAllowGeneratedReplies(false)
                                        .setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_REPLY)
                                        .build()
                        )
                        .setColor(Color.YELLOW)
                        .setPriority(NotificationCompat.PRIORITY_MAX);


//                manager.cancel(1);
                manager.notify("hello", 1,  builder.build());
                T.t(activity(), "hello");
//                }
            }
        });
    }
}
