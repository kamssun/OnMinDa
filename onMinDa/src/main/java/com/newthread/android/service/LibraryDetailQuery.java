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
import com.newthread.android.bean.LibraryDetailBookInfo;
import com.newthread.android.bean.LibraryDetailListItemInfo;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.util.StringUtils;

public class LibraryDetailQuery {
	private String url;
	private LibraryDetailBookInfo book;

	private static final int TIMEOUTMILLIS = 30 * 1000;
	private boolean DEBUG = true;

	public LibraryDetailQuery(String url, LibraryDetailBookInfo book) {
		this.url = url;
		this.book = book;
		if (DEBUG) {
		} else {
			this.url = url;
		}
	}

	public int query() {
		try {
			Document doc = null;
			if (url != null) {
				doc = Jsoup.parse(new URL(url), TIMEOUTMILLIS);
			} else {
				return HandleMessage.NO_CONTENT;
			}

			// 装填数据
			if (doc != null) {
				Elements es0 = doc.getElementsByClass("booklist");
				for (Element e : es0) {
					String temp = e.getElementsByTag("dt").first().text();
					if (temp.equals("题名/责任者:")) {
						System.out.println("题名: " + e.getElementsByTag("a").first().text());
						System.out.println("作者: " + e.getElementsByTag("dd").first().text().substring(e.getElementsByTag("a").first().text().length() + 1) );
						book.setTitle(e.getElementsByTag("a").first().text());
						book.setAuther(e.getElementsByTag("dd").first().text().substring(e.getElementsByTag("a").first().text().length() + 1));
					}
					if (temp.equals("出版发行项:")) {
						String publisher = e.getElementsByTag("dd").first().text();
						System.out.println("提要文摘附注:" + e.getElementsByTag("dd").first().text());
						if (!StringUtils.isEmpty(publisher)) {
							book.setPublisher(publisher);
						} else {
							book.setPublisher("无");
						}
					}
					if (temp.equals("提要文摘附注:")) {
						System.out.println("提要文摘附注:" + e.getElementsByTag("dd").first().text());
						String digest = e.getElementsByTag("dd").first().text();
						if (!StringUtils.isEmpty(digest)) {
							book.setDigest(digest);
						} else {
							book.setDigest("无");
						}
					}
					if (temp.equals("内容简介:")) {
						System.out.println("内容简介：" + e.getElementsByTag("dd").first().text());
						String intro = e.getElementsByTag("dd").first().text();
						if (!StringUtils.isEmpty(intro)) {
							book.setIntro(intro);
						} else {
							book.setDigest("无");
						}
					}
				}

				Elements es1 = doc.getElementsByClass("whitetext");
				for (int i = 0; i < es1.size(); i++) {
					LibraryDetailListItemInfo item = new LibraryDetailListItemInfo();
					
					String id = es1.get(i).select("[width=10%]").text();
					String location = es1.get(i).select("[width=25%]").first().text();
					String status = es1.get(i).select("[width=25%]").get(1).text();
					
					if (!StringUtils.isEmpty(id)) item.setId(id); else item.setId("无");
					if (!StringUtils.isEmpty(location)) item.setLocation(location); else item.setLocation("无");
					if (!StringUtils.isEmpty(status)) item.setStatus(status); else item.setStatus("该书目前不在书架");
					
					book.getList().add(item);
					
					System.out.println("id: " + id);
					System.out.println("location: " + location);
					System.out.println("status: " + status);
				}

				// 资源链接
				Element e2 = doc.getElementsByClass("sharing_zy").first();
				Elements es2 = e2.getElementsByTag("a");
				String doubanHref = es2.first().attr("href");  		// 豆瓣
				// String dangdangHref = e.get(2).attr("href");		// 当当
				System.out.println("href: " + doubanHref);
				if (!StringUtils.isEmpty(doubanHref)) {
					book.setDouBanHref(doubanHref);
				} else {
					book.setDouBanHref("www.douban.com");
				}
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
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		}

		return HandleMessage.QUERY_SUCCESS;
	}

}
