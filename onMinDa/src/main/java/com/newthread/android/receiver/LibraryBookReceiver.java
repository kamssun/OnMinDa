package com.newthread.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.newthread.android.bean.CurrentBorrowItem;
import com.newthread.android.manager.LibraryBookRemindManger;

/**
 * Created by Administrator on 2014/8/26.
 */
public class LibraryBookReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.newthread.android.action.LibraryBookRunOut")) {
            CurrentBorrowItem currentBorrowItem = new CurrentBorrowItem();
            currentBorrowItem.setBookName("金品梅");
            LibraryBookRemindManger.getInstance(context).creatNotiforcation(currentBorrowItem);
        }
    }
}
