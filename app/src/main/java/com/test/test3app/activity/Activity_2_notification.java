package com.test.test3app.activity;


import android.annotation.TargetApi;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.PostProcessor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.IconCompat;

import com.test.test3app.R;
import com.test.test3app.notification.TNotificationHelper;
import com.test.test3app.notification.TNotificationImageProvider;
import com.test.test3app.notification.TNotificationItem;
import com.test.test3app.notification.actions.ReadAction;
import com.test.test3app.notification.actions.ReadCallback;
import com.test.test3app.notification.actions.ReplyAction;
import com.test.test3app.notification.actions.ReplyCallback;
import com.test.test3app.notification.constant.NotificationGroupNotifyId;
import com.test.test3app.notification.constant.NotificationGroupKey;
import com.test.test3app.notification.summary.NotificationSummary;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import base.ui.BaseActivity;
import im.turbo.basetools.file.FileUtil;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionResult;
import im.turbo.baseui.permission.PermissionUtils;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;
import im.turbo.utils.log.S;

public class Activity_2_notification extends BaseActivity {

    private int i = 0;
    private SafeRunnable safeRunnable;
    private String path = "/storage/emulated/0/Pictures/Telegram/IMG_20230814_151433_136.jpg";
    private String pathOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.start_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        safeRunnable = new SafeRunnable(activity()) {
            @Override
            protected void runSafely() {
                showNotification();
                ThreadPool.runUiDelayed(i < 10 ? 200 : 5000, safeRunnable);
            }
        };
        ThreadPool.runUi(safeRunnable);
    }

    private void debug() {
        PermissionUtils.requestPermission(activity(), new PermissionResult() {
            @Override
            public void onGranted(@NonNull String[] grantedPermissions) {
                pathOut = getFilesDir() + "/" + "hello.jpg";
                File out = new File(pathOut);
                S.s("before copy, exists:" + out.exists());
                if (!out.exists()) {
                    try {
                        FileUtil.copyFile(new File(path), out);
                    } catch (IOException e) {
                        S.e(e);
                    }
                    S.s("after copy, exists:" + out.exists());
                }
                showNotification();
            }
        }, Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE);
    }

    protected void showNotification() {
        i++;
        if (i > 100) {
            S.s(":" + TNotificationHelper.getInstance().getMessageCount());
            return;
        }
        PermissionUtils.requestPermission(activity(), new PermissionResult() {
            @Override
            public void onGranted(@NonNull String[] grantedPermissions) {
                TNotificationItem item = new TNotificationItem(10086);
                item.setGroupKey(NotificationGroupKey.CHAT, NotificationGroupNotifyId.CHAT)
                        .setPersonName("person1", "person1", pathOut == null ? IconCompat.createWithBitmap(loadRoundAvatar(R.drawable.anime_girl)) : IconCompat.createWithBitmap(loadRoundAvatar(new File(pathOut))))
                        .setConversationTitle("GroupXX")
                        .setTime(System.currentTimeMillis())
                        .setUuid("111111111" + i)
//                        .setRecall(i == 5)
                        .setUri(pathOut == null ? null : getUri())
                        .setText("Photo" + i)
                        .setActions(new ReplyAction("Reply", R.drawable.chat_reply, "input something", new ReplyCallback() {
                                    @Override
                                    public void onInputReply(@NonNull TNotificationItem item, CharSequence replyContent) {
                                        S.s("onInputReply GroupNotifyId:" + item.getNotifyIdGroup() + "  NotifyId:" + item.getNotifyId() + " replyContent:" + replyContent);
                                    }
                                }),
                                new ReadAction("Mark as Read", R.drawable.svg_message_send_success_received_and_read, new ReadCallback() {
                                    @Override
                                    public void onClickRead(@NonNull TNotificationItem item) {
                                        S.s("onClickRead GroupNotifyId:" + item.getNotifyIdGroup() + "  NotifyId:" + item.getNotifyId());
                                    }
                                })
                        )
                        .setSummary(new NotificationSummary() {
                            @Override
                            public CharSequence onGetSummary(@NonNull TNotificationItem item, int notificationCount, int messageCount) {
                                S.s("int notificationCount, int messageCount:" + notificationCount + " " + messageCount);
                                return messageCount + " messages from " + notificationCount + " Chats";
                            }

                            @DrawableRes
                            @Override
                            public int getSummaryIcon(@NonNull TNotificationItem item) {
                                return R.drawable.anime_girl;
                            }
                        });
                TNotificationHelper.getInstance().show(item);
                TNotificationHelper.getInstance().show(new TNotificationItem(10087)
                        .setGroupKey(NotificationGroupKey.CHAT, NotificationGroupNotifyId.CHAT)
                        .setUuid("22222222" + i)
                        .setPersonName("person2", "person2", IconCompat.createWithBitmap(loadRoundAvatar(R.drawable.anime_girl)))
                        .setTime(System.currentTimeMillis())
                        .setText("Text" + i)
                        .setSummary(new NotificationSummary() {
                            @Override
                            public CharSequence onGetSummary(@NonNull TNotificationItem item, int notificationCount, int messageCount) {
                                S.s("int notificationCount, int messageCount:" + notificationCount + " " + messageCount);
                                return messageCount + " messages from " + notificationCount + " Chats";
                            }

                            @DrawableRes
                            @Override
                            public int getSummaryIcon(@NonNull TNotificationItem item) {
                                return R.drawable.anime_girl;
                            }
                        })
                );
            }
        }, Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE);
    }

    private Uri getImage() {
        String[] what = new String[]{MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED};

        Calendar calendar = Calendar.getInstance();
        calendar.set(2008, 10, 22);
//        String[] args = {new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.getTime())};
        Cursor cursor = getApplication().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        what,
                        null,
                        null, null);
        Uri imageUri = null;
        if (cursor.moveToNext()) {
            int value = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
            if (value >= 0) {
                imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(value));
            }
        }
        cursor.close();
        return imageUri;
    }

    @TargetApi(Build.VERSION_CODES.P)
    public static Bitmap loadRoundAvatar(File avatar) {
        try {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(avatar), new ImageDecoder.OnHeaderDecodedListener() {
                @Override
                public void onHeaderDecoded(@NonNull ImageDecoder decoder, @NonNull ImageDecoder.ImageInfo info, @NonNull ImageDecoder.Source src) {
                    decoder.setPostProcessor(new PostProcessor() {
                        @Override
                        public int onPostProcess(@NonNull Canvas canvas) {
                            Path path = new Path();
                            path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
                            int width = canvas.getWidth();
                            int height = canvas.getHeight();
                            float radius = Math.min(width, height) / 2f;
                            float xCenter = width / 2f;
                            float yCenter = height / 2f;
                            path.addRoundRect(xCenter - radius, yCenter - radius, xCenter + radius, yCenter + radius, radius, radius, Path.Direction.CW);
                            Paint paint = new Paint();
                            paint.setAntiAlias(true);
                            paint.setColor(Color.TRANSPARENT);
                            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                            canvas.drawPath(path, paint);
                            return PixelFormat.TRANSLUCENT;
                        }
                    });
                }
            });
        } catch (Throwable ignore) {
            return null;
        }
    }

    public Bitmap loadRoundAvatar(int resourceId) {
        return loadRoundAvatar(BitmapFactory.decodeResource(getResources(), resourceId));
    }

    public Bitmap loadRoundAvatar(Bitmap avatar) {
        Bitmap output = Bitmap.createBitmap(avatar.getWidth(), avatar.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, avatar.getWidth(), avatar.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(avatar.getWidth() / 2f, avatar.getHeight() / 2f, Math.min(avatar.getWidth(), avatar.getHeight()) / 2f, paint);
//        paint.setColor(color);
//        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(avatar.getWidth() / 2f, avatar.getHeight() / 2f, Math.min(avatar.getWidth(), avatar.getHeight()) / 2f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(avatar, rect, rect, paint);
        paint.setXfermode(null);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    private Uri getUri() {
        Uri.Builder _uri = new Uri.Builder()
                .scheme("content")
                .authority(TNotificationImageProvider.getAuthority())
                .appendQueryParameter("final_path", pathOut);
        return _uri.build();
    }
}
