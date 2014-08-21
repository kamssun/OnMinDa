package com.newthread.android.ui.labquery;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public abstract class OnRequestResponseListener {
	 protected Context context = new Application();
	
	 public abstract void onRequestSuccess(long requestId, String result);
	 
	 public abstract void onRequestApiError(long requestId, String result);
	 
	 public void onRequestNetError(long requestId, String result) {
		 Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
	 }
}
