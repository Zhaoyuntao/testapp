package com.test.test3app.notification;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;

import im.turbo.utils.ResourceUtils;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 14/08/2023
 */
public class TNotificationImageProvider extends ContentProvider {
    private static String authority;

    public static String getAuthority() {
        if (authority == null) {
            authority = ResourceUtils.getApplication().getPackageName() + ".notification_image_provider";
        }
        return authority;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        S.s("uri:"+uri+"  mode:"+mode);
        String finalPath = uri.getQueryParameter("final_path");
        File finalFile = new File(finalPath);
        if (!finalFile.exists()) {
            throw new SecurityException("trying to read file not exists:" + uri);
        }
        return ParcelFileDescriptor.open(finalFile, ParcelFileDescriptor.MODE_READ_ONLY);
    }
}
