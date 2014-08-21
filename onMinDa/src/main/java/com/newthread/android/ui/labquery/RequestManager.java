package com.newthread.android.ui.labquery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.newthread.android.util.AndroidDB;

import android.annotation.SuppressLint;
import android.util.Log;

public class RequestManager {

	public static final String LOGINURL = "http://labsystem.scuec.edu.cn/login.php";
	public static final String QueryURL = "http://labsystem.scuec.edu.cn/labcoursearrange2_student.php?labcourse=DXXY-312";
	private HttpClient httpclient = new DefaultHttpClient();

	private static class RequestManagerContainer {
		private static RequestManager instance = new RequestManager();
	}

	public static RequestManager getInstance() {
		return RequestManagerContainer.instance;
	}

	private RequestManager() {
	}

	public synchronized void startRequest(
			final OnRequestResponseListener listener,
			final LoginRequestApi Loginrequest) {
		/**
		 * 执行请求等操作
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Post(Loginrequest, listener);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// post
	public void Post(LoginRequestApi Loginrequest,
			OnRequestResponseListener listener) throws ClientProtocolException,
			IOException {
		HttpPost post = new HttpPost(LOGINURL);
		// 对传递的参数进行封装
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("myid", Loginrequest.getAccount()));
		params.add(new BasicNameValuePair("mypasswd", Loginrequest
				.getPassword()));
		params.add(new BasicNameValuePair("mytype", "student"));
		// 设置请求参数
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);// 连接超时

		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				30000);// 请求超时
		HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);// 设置字符集
		post.setEntity(entity); // 设置参数实体
		// 发生post请求
		HttpResponse response = httpclient.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			String loginresult = EntityUtils.toString(response.getEntity(),
					"UTF-8");
			Log.v("LoginResult", loginresult);
			if (loginresult.length() > 60) {
				Log.v("登陆", "登陆成功");
				Get(listener);
			} else {
				Log.v("登陆", "登陆失败");
				listener.onRequestApiError(0, null);
			}
		}
	}

	private String parse_login_result(String result) {
		Document doc = Jsoup.parse(result);
		Elements AlrtErrTxts = doc.getElementsByClass("AlrtErrTxt");
		String str = "";
		if (AlrtErrTxts.hasText()) {
			str = AlrtErrTxts.get(0).text();
		}
		return str;
	}

	// Get
	private void Get(OnRequestResponseListener listener) throws ParseException,
			IOException {
		HttpConnectionParams
				.setConnectionTimeout(httpclient.getParams(), 20000); // 设置连接超时
		HttpConnectionParams.setSoTimeout(httpclient.getParams(), 20000); // 设置数据读取时间超时
		ConnManagerParams.setTimeout(httpclient.getParams(), 20000); // 设置从连接池中取连接超时
		HttpGet httpget = new HttpGet(QueryURL); // 获取请求
		HttpResponse response = httpclient.execute(httpget); // 执行请求，获取响应结果
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
			String result = EntityUtils.toString(response.getEntity(), "gbk");
			Log.v("QuertResult", result);
			parse(result, listener);
		}
	}

	// 解析实验数据
	@SuppressLint("NewApi")
	private void parse(String result, OnRequestResponseListener listener) {
		Document doc = Jsoup.parse(result);
		Element ele = doc.getElementsByTag("tbody").first()
				.getElementsByTag("td").first();
		Log.v("parse", ele.text());
		Elements eles = doc.getElementsByAttribute("bgcolor");
		AndroidDB.deleteLab(LabLogin_activity.mcontext);
		for (Element e : eles) {
			if (e.getElementsByTag("td").get(8).text().trim().length() > 2) {
				LabDetail item = new LabDetail();
				if (eles.get(0) == e) {
					String LabName = e.getElementsByTag("td").get(1).text()
							.trim();
					Log.v("string", LabName);
					item.setLabName(LabName.substring(LabName.indexOf("）") + 1));
					Log.v("LabName", item.getLabName());
					item.setLabLocation(e.getElementsByTag("td").get(7).text()
							.trim());
					item.setWeekNum(e.getElementsByTag("td").get(4).text()
							.trim().substring(0, 2));
					item.setWeek("周"
							+ e.getElementsByTag("td").get(5).text().trim());
					item.setTime(e.getElementsByTag("td").get(6).text());
					item.setTeacherName(e.getElementsByTag("td").get(8).text()
							.trim());
					item.setScore(e.getElementsByTag("td").get(9).text().trim());
					Log.v("score", e.getElementsByTag("td").get(9).text()
							.trim());
				} else {
					String LabName = e.getElementsByTag("td").get(0).text()
							.trim();
					item.setLabName(LabName.substring(LabName.indexOf("）") + 1));
					Log.v("LabName", item.getLabName());
					item.setLabLocation(e.getElementsByTag("td").get(6).text()
							.trim());
					item.setWeekNum(e.getElementsByTag("td").get(3).text()
							.trim().substring(0, 2));
					item.setWeek("周"
							+ e.getElementsByTag("td").get(4).text().trim());
					item.setTime(e.getElementsByTag("td").get(5).text());
					item.setTeacherName(e.getElementsByTag("td").get(7).text()
							.trim());
					item.setScore(e.getElementsByTag("td").get(8).text().trim());
					Log.v("score", e.getElementsByTag("td").get(8).text()
							.trim());
				}
				AndroidDB.addLab(LabLogin_activity.mcontext, item);
				if (item.getLabLocation().isEmpty()) {
				}
			} else {
				Log.v("没有选该实验", "NO SELECTING");
			}
		}
		listener.onRequestSuccess(0, null);
	}
}
