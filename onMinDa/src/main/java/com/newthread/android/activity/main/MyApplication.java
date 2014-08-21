package com.newthread.android.activity.main;

import android.app.Application;
import com.newthread.android.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
    }
}
