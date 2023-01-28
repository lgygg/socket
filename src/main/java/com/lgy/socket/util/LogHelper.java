package com.lgy.socket.util;

import android.util.Log;

public class LogHelper {
    public static final String TAG = "lgy_debug";
    private static final boolean DEBUG = true;

    public static void d(String msg) {
        if (DEBUG)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (DEBUG)
            Log.e(TAG, msg);
    }
}
