package com.newthread.android.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.newthread.android.bean.LibraryQueryItemInfo;
import com.newthread.android.bean.LibraryQueryListReturnPara;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.util.Logger;

import android.content.Context;

public class LibraryQuery {
	private String url;
	private Context con;
	private LibraryQueryListReturnPara returnPara;
	private List<LibraryQueryItemInfo> list;
	
	private static final int TIMEOUTMILLIS = 30000;
	private boolean DEBUG = true;
	
	public LibraryQuery(Context con, String url, List<LibraryQueryItemInfo> list, LibraryQueryListReturnPara retrunPara) {
		this.con = con;
		this.list = list;
		this.returnPara = retrunPara;
		if (DEBUG) {
			this.url = url;
			Logger.i("URL:", url);
//				this.url = "http://coin.lib.scuec.edu.cn/opac/openlink.php?location=ALL&title="+ new String("java".getBytes(),"ISO-8859-1") +"&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&with_ebook=&with_ebook=";
		} else {
			this.url = url;
//				this.url = "http://coin.lib.scuec.edu.cn/opac/openlink.php?location=ALL&title="+ new String(para.getTitle().trim().getBytes(),"ISO-8859-1") +"&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&with_ebook=&with_ebook=";
		}
	}

	public int query() {
		try {
			Document doc = null;
			if (url != null) {
				doc = Jsoup.parse(new URL(url), TIMEOUTMILLIS);
				Logger.i("doc.tostring", "" + doc.toString());
			} else {
				Logger.i("Exception", "Doc == null");
				return HandleMessage.NO_CONTENT;
			}

			if (doc != null) {
//				Elements es = doc.getElementsByClass("book_list_info");
				Elements es = doc.select("li.book_list_info");
				Logger.i("quer-es.size", "" + es.size());
				
				String num;
				try {
					num = doc.select("strong.red").first().text();
					returnPara.setTotalSize(num);
					System.out.println("Total:" + num);
				} catch (NullPointerException e) {
					Logger.i("Exception", "total == 0");
					e.toString();
					returnPara.setTotalSize("0");
					System.out.println("total == 0");
					return HandleMessage.NO_CONTENT;
				} catch (Exception e) {
					e.toString();
					Logger.i("getTotalsize", "" + e.toString());

					return HandleMessage.NO_CONTENT;
				}
				
//				if (Integer.parseInt(num) > 20) {
//					String nextPageHref;
//					if (true ) {
//						// ��һҳ
//						nextPageHref = doc.select("span.num_prev").first().getElementsByTag("a").first().attr("href");
//						
//					} else {
//						
//					}
//					Logger.i("nexthref", "" + nextPageHref);
//					returnPara.setNextPageHref(nextPageHref);
//				}
				
				for (Element e : es) {
					LibraryQueryItemInfo item = new LibraryQueryItemInfo();
					
					// 标题
					String title = e.getElementsByTag("a").first().text();
					int s1 = title.indexOf(".");
					item.setTitle(title.substring(s1 + 1));
					System.out.println("" + title.substring(s1 + 1));

					// ID
					String id = e.getElementsByTag("h3").first().text();
					int s2 = id.indexOf(" ");
					int end2 = id.length();
					String ID = id.subSequence(s2, end2).toString();
					item.setId(ID);
					System.out.println("id: " + ID);

					// 作者以及出版社
					Element e3 = e.getElementsByTag("p").first();
					int s3 = e3.getElementsByTag("span").text().length();
					int end3 = e3.text().length();
					///////////////BUG////////////////////
					if (end3 - 7 <= s3) {
						end3+=7;
					}
					String autherAndPubl = e3.text().substring(s3, end3 - 7);
					///////////////////////////////////
					item.setAuther(autherAndPubl);
					System.out.println("autherAndPubl:" + autherAndPubl);

					// 链接
					item.setLink("http://coin.lib.scuec.edu.cn/opac/" + e.getElementsByTag("a").first().attr("href"));
					System.out.println("link" + "http://coin.lib.scuec.edu.cn/opac/" + e.getElementsByTag("a").first().attr("href"));
					
					// ��ȡ�ݲ���ɽ���
					System.out.println("" + e.getElementsByTag("p").first().getElementsByTag("span").text());
					
					list.add(item);
				}

			}
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

		return HandleMessage.QUERY_SUCCESS;
	}
}
