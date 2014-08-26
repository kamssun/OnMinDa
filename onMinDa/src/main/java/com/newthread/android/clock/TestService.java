package com.newthread.android.clock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 测试时，需要在manifest里面声明
 */
public class TestService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("0000","TestService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
