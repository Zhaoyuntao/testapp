package com.test.test3app.activity;


import android.annotation.TargetApi;
import android.app.Notification;
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
import android.os.Parcel;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.test.test3app.R;
import com.test.test3app.notification.TNotificationHelper;
import com.test.test3app.notification.TNotificationItem;
import com.test.test3app.notification.constant.NotificationGroupTag;
import com.zhaoyuntao.androidutils.tools.T;

import java.io.File;
import java.util.Calendar;

import base.ui.BaseActivity;
import im.turbo.basetools.file.FileUtil;
import im.turbo.basetools.file.SelectFileUtils;
import im.turbo.baseui.permission.Permission;
import im.turbo.baseui.permission.PermissionResult;
import im.turbo.baseui.permission.PermissionUtils;
import im.turbo.utils.log.S;

public class Activity_2_notification extends BaseActivity {

    public static String ChannelId = "myNotification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.start_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PermissionUtils.requestPermission(v.getContext(), new PermissionResult() {
//                    @Override
//                    public void onGranted(@NonNull String[] grantedPermissions) {
////                        showNotification();
//                    }
//                }, Permission.NOTIFICATION_PERMISSION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(activity(),ChannelId)
                        .setSmallIcon(R.drawable.b_app_logo_white)
                        .setContentTitle("??")
                        .setContentText("??")
                        .setPriority(NotificationCompat.PRIORITY_MIN);
                    NotificationManagerCompat   manager = NotificationManagerCompat.from(activity());
                    manager.notify("hello", 0, builder.build());
            }
        });
        findViewById(R.id.update_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.requestPermission(v.getContext(), new PermissionResult() {
                    @Override
                    public void onGranted(@NonNull String[] grantedPermissions) {
                        copyFile();
                    }
                }, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
            }
        });
    }

    private String pathOut;

    private void copyFile() {
        pathOut = getFilesDir() + "/" + "hello.jpg";
        File out = new File(pathOut);
        S.s("before copy, exists:" + out.exists());
        if (!out.exists()) {
            SelectFileUtils.selectFile(activity(), new SelectFileUtils.FileGetter() {
                @Override
                public void whenGetFile(Uri uri) {
                    S.s("path:" + uri);
                    FileUtil.copyFileFromUri(uri, activity(), pathOut);
                    T.t(activity(), "Copied file:" + pathOut);
                }
            });
            S.s("after copy, exists:" + out.exists());
        }
    }

    int i = 0;

    protected void showNotification() {
        TNotificationHelper.getInstance().post(new TNotificationItem(ChannelId, "10086", 123).setGroupTag(NotificationGroupTag.CHAT)
                .setPerson("person1", "person1",
                        IconCompat.createWithBitmap(loadRoundAvatar(R.drawable.anime_girl)),
                        IconCompat.createWithAdaptiveBitmap(loadRoundAvatar(R.drawable.anime_girl))
                )
                .setConversationTitle("GroupXX")
                .setTime(System.currentTimeMillis())
                .setUuid("111111111")
                .setText(String.valueOf(i++))
        );
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

}
