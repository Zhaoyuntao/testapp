/**
 * SharedPreference.java
 * com.jamin.aabill.sharepref
 *
 * Function�?TODO
 *
 *   ver     date      		author
 * ???????????????????????????????????????????????????
 *   		 2012-8-22 		wangjieming
 *
 * Copyright (c) 2012, TNT All Rights Reserved.
 */
package im.turbo.baseui.permissionstore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import im.turbo.utils.ResourceUtils;


/**
 * ClassName:SharedPreference
 *
 * SharedPreference for app permission,
 *
 * store query permission times,
 *
 * -1 for denied not show
 */
public class PermissionSharedPref {

	private SharedPreferences mSharedPreferences = null;

	private volatile static PermissionSharedPref sharePref = null;

	public static PermissionSharedPref getInstance(Context ctx) {
		if (sharePref == null) {
			synchronized (PermissionSharedPref.class) {
				if (sharePref == null) {
					sharePref = new PermissionSharedPref(ResourceUtils.getApplication());
				}
			}
		}
		return sharePref;
	}

	private PermissionSharedPref(Context ctx) {
		//todo update to mmkv
		mSharedPreferences = ctx.getSharedPreferences("turbo-permission", Context.MODE_PRIVATE);
	}

	public boolean isFirstRequest(String permission) {
		return mSharedPreferences.getInt(permission, 0) == 0;
	}

	public boolean isDeniedNotShow(String permission) {
		return mSharedPreferences.getInt(permission, 0) == -1;
	}

	public void deniedNotShow(String permission) {
		int count = mSharedPreferences.getInt(permission, 0);
		if (count == -1) return;
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putInt(permission, -1);
		mEditor.apply();
	}

	public void increaseCount(String permission) {
		int count = mSharedPreferences.getInt(permission, 0);
		if (count == -1) return;
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putInt(permission, count + 1);
		mEditor.apply();
	}

	public void granted(String permission) {
		int count = mSharedPreferences.getInt(permission, 0);
		if (count == 0) return;
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putInt(permission, 1);
		mEditor.apply();
	}
}
