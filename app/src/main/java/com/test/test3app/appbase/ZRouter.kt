package com.test.test3app.appbase

import android.app.Activity
import android.view.View
import com.test.test3app.activity.MainActivity1_unknown

/**
 * created by zhaoyuntao
 * on 05/04/2022
 * description:
 */
public data class ZRouter(var name:String, var activity: Class<out Activity>) {
}