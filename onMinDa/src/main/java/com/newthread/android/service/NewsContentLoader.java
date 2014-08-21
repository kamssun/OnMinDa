package com.newthread.android.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;

import com.newthread.android.bean.NewsContentVo;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.util.Logger;

/**
 * 新闻内容加载,同时，实现图片与文本内容的缓存
 * 注： 目前默认只加载一张图片的情况
 * @author _sush
 */
public class NewsContentLoader {
	private NewsContentVo vo; 
	private String url;
	private Document doc;
	private static final boolean DEBUG = false;
	
	public NewsContentLoader(Context context, NewsContentVo vo, String url) {
		this.vo = vo;
		
		if (!DEBUG) {
			this.url = url;
		} else {
			this.url = "http://news.scuec.edu.cn/xww/?view-7099.htm";
		}
	}
	
	// 装载Vo
	public int loadContent() {
		String imageUrl = null;
		try {
			doc = Jsoup.parse(new URL(url), 20 * 1000);
//			System.out.print(doc.toString());
		} catch (MalformedURLException e) {
			Logger.i("MalformedURLException", "MalformedURLException");
			e.printStackTrace();
			return HandleMessage.NO_CONTENT;
		} catch (IllegalArgumentException e) {
			Logger.i("IllegalArgumentException", "IllegalArgumentException");
			e.printStackTrace();
			return HandleMessage.NO_CONTENT;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Logger.i("ClientProtocolException", "QUERY_ERROR");
			return HandleMessage.QUERY_ERROR;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			Logger.i("SocketTimeoutException", "QUERY_ERROR");
			return HandleMessage.QUERY_ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			Logger.i("IOException", "QUERY_ERROR");
			return HandleMessage.QUERY_ERROR;
		} catch (Exception e) {
			Logger.i("Exception", "QUERY_ERROR: " + e.toString());
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		}
		
		try {
			// 标题
			// ////////////////可以不获取，由Item传入 ///////////////////////
			Element e0 = doc.getElementsByClass("essayhead").first();
			vo.setTitle(e0.getElementsByTag("a").get(2).text());
//			Logger.i("NewsContentLoader", "title: " + e0.getElementsByTag("a").get(2).text());

			// 文章内容
			Elements es1 = doc.getElementsByClass("essaycontent").first().getElementsByTag("P");
			StringBuilder sb = new StringBuilder();
			int i;
			for (i = 0; i < es1.size() - 2; i++) {
				// ImageUrl:
				if (es1.get(i).text() == null || es1.get(i).text().equals("")) {
//					Log.i("tag", " + es1.get(i).text() == null, " + i + ", " + es1.get(i).getElementsByTag("IMG").first().attr("src"));
					imageUrl = es1.get(i).getElementsByTag("IMG").first().attr("src");
					Logger.i("get_url", imageUrl);
					i++;
				} else {
					sb.append("      " + es1.get(i).text() + "\n\n");
				}
			}
			vo.setContent(sb.toString());
			
			// 图片链接
			if (imageUrl != null) {
				vo.setImageUrl(imageUrl);
			}

			// 来源及编辑
			vo.setSource(es1.get(es1.size() - 2).text().substring(1, es1.get(es1.size() - 2).text().length() - 1));
			Logger.i("NewsContentLoader", "Source: "+ es1.get(es1.size() - 2).text().substring(1, es1.get(es1.size() - 2).text().length() - 1));

		} catch (Exception e) {
			Logger.i("NewsContentLoader" + "_loadContent", e.toString());
			return HandleMessage.QUERY_ERROR;
		}
		
		return HandleMessage.QUERY_SUCCESS;
	}
	
}
