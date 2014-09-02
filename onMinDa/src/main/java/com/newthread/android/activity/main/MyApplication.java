package com.newthread.android.activity.main;

import android.app.Application;
import com.newthread.android.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplication extends Application {
    private static MyApplication instance;
    private  Map<String, Object> temps;

    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 用于临时存放东西的方法
     *
     * @param name
     * @param obj
     */
    public  void putThing(String name, Object obj) {
        temps.put(name, obj);
    }

    /**
     * r取出临时存放的东西，会自动remove掉
     *
     * @param name
     */
    public  <T> T getThing(String name) {
        return (T) temps.remove(name);

    }

    @Override

    public void onCreate() {
        super.onCreate();
        instance=this;
        temps = new HashMap<String, Object>();
        CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
    }
}
