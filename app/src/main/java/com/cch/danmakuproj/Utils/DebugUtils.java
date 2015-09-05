package com.cch.danmakuproj.Utils;

import android.util.Log;

/**
 * Created by 晨晖 on 2015-09-04.
 */
public class DebugUtils {
    long startTime;
    long endTime;

    public void startMethodDebug() {
        startTime = System.currentTimeMillis();
    }

    public void endMethodDebug() {
        endTime = System.currentTimeMillis();
        Log.e("DEBUG","Method Time:" + (endTime - startTime) + "MS");
    }
}
