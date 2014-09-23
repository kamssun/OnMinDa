package com.newthread.android.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.newthread.android.bean.LostListItem;
import com.newthread.android.global.HandleMessage;

public class LostMainQuery {
	private List<LostListItem> list;

	public LostMainQuery(Context con, List<LostListItem> list) {
		this.list = list;
	}

	public int query(String url) {
		HttpGet httpRequest = new HttpGet(url);
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
				if (str != null) {
					return parseData(str);
				}
			} else {
				return HandleMessage.QUERY_ERROR;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return HandleMessage.NO_CONTENT;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return HandleMessage.NO_CONTENT;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		} catch (Exception e) {
			return HandleMessage.QUERY_ERROR;
		}

		return HandleMessage.QUERY_SUCCESS;
	}
	
	// 解析
	private int parseData(String str) {
		try {
			JSONObject object = new JSONObject(str);
			
			JSONObject object2 = object.getJSONObject("Head");
			if (object2.getString("Code").equals("0") && object2.getString("Msg").equals("Success")) {
				JSONObject object3 = object.getJSONObject("Body");
				JSONArray ja = object3.getJSONArray("Data");
				
				int i = 0;
				for (; i < ja.length(); i++) {
					LostListItem item = new LostListItem();
					JSONObject object4 = ja.getJSONObject(i);
					
					item.setName(object4.getString("name"));
					item.setPlace("捡取地点:" + object4.getString("place"));
					item.setTime("捡取时间:" + object4.getString("time"));
					item.setDescription("物品描述");
					item.setContactOfQQ("793767468");
					item.setContactOfPhone("13277063978");
					item.setContactOfMail("sushun001@mail.com");
					
					list.add(item);
					
					System.out.println("name:   " +object4.getString("name"));
					System.out.println("place:   " +object4.getString("place"));
				}
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		}
		
		return HandleMessage.QUERY_SUCCESS;
	}
}
