package com.newthread.android.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.widget.ListView;
import com.newthread.android.bean.NewsContentVo;
import com.newthread.android.bean.NewsListItem;
import com.newthread.android.global.HandleMessage;

public class NewsListQuery {
    private Document doc;
    
    private ArrayList<NewsListItem> list;
	private int currentRefreshPosition = 0;		// 需要更新的pager位置
	private boolean isFirstLoad; 

	private static final int PAGE_SIZE = 10;	// 每次加载item数量
    public static final String focusUrl = "http://news.scuec.edu.cn/xww/?class-focusNews.htm";		// 民大要闻
	public static final String zongheUrl = "http://news.scuec.edu.cn/xww/?class-zonghexinwen.htm";	// 综合新闻
	public static final String academyUrl = "http://news.scuec.edu.cn/xww/?class-academyNews.htm";	// 院系风采
	private String url = "http://news.scuec.edu.cn/xww/?class-focusNews.htm";	// 要查询的URL
	
	public NewsListQuery(ArrayList<NewsListItem> list, int currentRefreshPosition) {
		this.list = list;
		this.currentRefreshPosition = currentRefreshPosition;
	}
	
	public NewsListQuery(ArrayList<NewsListItem> list, int currentRefreshPosition, String url) {
		this.list = list;
		this.currentRefreshPosition = currentRefreshPosition;
		this.url = url;
	}
	
	// 解析新闻列表
	public int query() {
		if (0 == currentRefreshPosition) {
			url = focusUrl;
		} else if (1 == currentRefreshPosition) {
			url = zongheUrl;
		} else if (2 == currentRefreshPosition) {
			url = academyUrl;
		}
		
		// 新闻列表的解析
		return parseNewsList();
	}
	
	// 解析新闻列表
	public int queryMore() {
		// 新闻列表的解析
		return parseNewsList();
	}
	
	// 新闻列表的解析
	public int parseNewsList() {
		///////////////////////////////////////////////////////////////////
		// 将获取新闻列表字符串的过程放到Activity中， 并加上相应的异常处理 /////////////////////
		try {
			doc = Jsoup.parse(new URL(url), 30 * 1000);
			
//			System.out.print(doc.toString());
		}catch (MalformedURLException e) {
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
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		}
		
		///////////////////////////////////////////////////////////////////
		// 如果Document生成错误，直接return
		try {
			Element e0 = doc.getElementsByClass("econtentl").first().getElementsByTag("ul").first();
			
			if (e0 == null) {
				System.out.println("e0 == null");
			}
			
			Elements es0 = e0.getElementsByTag("li");
			
			for (int i = 0; i < es0.size(); i++) {
				NewsListItem item = new NewsListItem();
				
				// 标题
				item.setTitle(es0.get(i).getElementsByTag("a").first().text());
				// 时间
				item.setTime(es0.get(i).getElementsByTag("em").first().text().substring(1, es0.get(i).getElementsByTag("em").first().text().length() - 1));
				// 摘要
				item.setDigest(e0.getElementsByTag("span").get(i).text());
				// 链接
				item.setHref("http://news.scuec.edu.cn/xww/" + es0.get(i).getElementsByTag("a").first().attr("href").substring(2));
				
				// 新闻类型
				if (currentRefreshPosition == 0) {
					item.setType("1");
				} else if (currentRefreshPosition == 1) {
					item.setType("2");
				} else if (currentRefreshPosition == 2) {
					item.setType("3");
				} else {
					item.setType("0");
				}
//				System.out.println("title: " + es0.get(i).getElementsByTag("a").first().text());
//				System.out.println("digest: " + e0.getElementsByTag("span").first().text());
//				System.out.println("time: " + es0.get(i).getElementsByTag("em").first().text().substring(1, es0.get(i).getElementsByTag("em").first().text().length() - 1));
//				System.out.println("href: " + "http://news.scuec.edu.cn/xww/?" + es0.get(i).getElementsByTag("a").first().attr("href").substring(2));
				// Add item
				list.add(item);
			}
			
			//////////////////////////////////////////////////////////////////////////
			//////// 下一页链接为   http://news.scuec.edu.cn/xww/?class-focusNews-page-2。。。。
		} catch (Exception e) {
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		}
		
		return HandleMessage.QUERY_SUCCESS;
	}
}
