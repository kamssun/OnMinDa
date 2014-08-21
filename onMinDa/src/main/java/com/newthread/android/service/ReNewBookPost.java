package com.newthread.android.service;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.newthread.android.global.HandleMessage;

import android.os.Handler;
import android.os.Message;

public class ReNewBookPost {
	private DefaultHttpClient client;
	private String strResult;
	private Handler handler;

	public ReNewBookPost(DefaultHttpClient client, Handler handler) {
		super();
		this.client = client;
		this.handler = handler;
	}

	public void RenewBook(final String bar_code, final String check) {
		Thread reNewBookThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = "http://coin.lib.scuec.edu.cn/reader/ajax_renew.php?bar_code="
						+ bar_code
						+ "&check="
						+ check
						+ "&time="
						+ System.currentTimeMillis();
				HttpGet RB_get = new HttpGet(url);
				HttpResponse httpResponse;
				try {
					httpResponse = client.execute(RB_get);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						strResult = EntityUtils.toString(
								httpResponse.getEntity(), "UTF-8").trim();
						if (!strResult.contains("invalid call")) {
							strResult = strResult.substring(
									strResult.indexOf(">") + 1,
									strResult.indexOf("</font>"));
						} else {
							strResult = "书的信息错误";
						}
					} else {
						strResult = "网络错误";
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					strResult = "网络错误";
				} catch (IOException e) {
					e.printStackTrace();
					strResult = "网络错误";
				}
				Message msg = new Message();
				msg.what = HandleMessage.BORROW_FINISH;
				msg.obj = strResult;
				handler.sendMessage(msg);
			}
		});
		reNewBookThread.start();
		while (true) {
			if (!reNewBookThread.isAlive()) {
				break;
			}
		}
	}
}
