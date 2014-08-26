package com.newthread.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2014/8/26.
 */
public class CouserReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.newthread.android.action.CourserRemind")) {
            Log.v("0000", "Course BroadCast");
        }
    }
}
