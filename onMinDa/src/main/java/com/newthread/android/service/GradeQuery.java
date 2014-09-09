package com.newthread.android.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.util.Log;
import com.newthread.android.bean.GradeInfo;
import com.newthread.android.bean.GradeQueryVo;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.util.StringUtils;

/**
 * 成绩查询DAO
 * @author sushun
 */
public class GradeQuery {
	private HttpClient httpClient;
	private static final String URL1 = "http://ids.scuec.edu.cn/amserver/UI/Login?goto=http://my.scuec.edu.cn/index.portal"; // 个人图书馆URL
	private static final String URL2 = "http://ssfw.scuec.edu.cn/ssfw/j_spring_ids_security_check"; // 个人图书馆URL
	private static final String URL3 = "http://ssfw.scuec.edu.cn/ssfw/pkgl/kcbxx/4/2013-2014-1.do"; // 课程表
	private static final String URL4 = "http://ssfw.scuec.edu.cn/ssfw/zhcx/cjxx.do"; // 成绩查询
	
	private static final boolean Debug = true;

	private Set<String> CourseIDSet;
	private ArrayList<GradeInfo> list;
	private GradeQueryVo vo;
	
	private int result = HandleMessage.QUERY_SUCCESS;
	
	private int TIMEOUT_CONNECT = 20 * 1000;
	private int TIMEOUT_SOCKET = 20 * 1000;
	
	public GradeQuery(ArrayList<GradeInfo> list, GradeQueryVo vo) {
		this.list = list;
		this.vo = vo;
		CourseIDSet = new HashSet<String>();
	}

	public int query() {
		// 登录民大认证平台
		result = connectPost(URL1);	
		
		if (result != HandleMessage.QUERY_SUCCESS) {
			return result;
		}
		
		// 登录教务系统师生端
		result = connectGet(URL2);		
		
		if (result != HandleMessage.QUERY_SUCCESS) {
			return result;
		}
		
		// 成绩查询
		return connectGradePost(URL4);
	}
	// 解析
	private int parse(String result) {
		Document doc = Jsoup.parse(result);
		
		Elements es = doc.select("tr.t_con");
		
		System.out.println("t_con_size:  " + es.size());
		
		try {
			for (int i = 0; i < es.size(); i++) {
				Element e = es.get(i);
				
				Elements es1 = e.getElementsByTag("td");
				
				System.out.println("----------------------------------------------");
				for (int m = 0 ; m < es1.size(); m++) {
					System.out.println(es1.get(m).text());
				}
				
				// 判断是否为课程成绩
				String temp = es1.get(1).text();
				if (temp.contains("学期") || temp.contains("学年")) {
					// 是否添加
					String courseID = es1.get(2).text().trim();
					if ( !haveAdd(courseID) ) {
						// 未添加
						addCourseID(courseID);
					} else {
						// 已添加
						continue;
					}
					
					GradeInfo item = new GradeInfo();
					
					String yearAndSemester = es1.get(1).text();
					// 学年
					item.setYear(yearAndSemester.substring(0, 9));		
					
					// 学期
					if (yearAndSemester.contains("一")) {
						item.setSemesterNum(1);
					} else {
						item.setSemesterNum(2);
					}
					
					item.setCourseID(es1.get(2).text().trim());				// 课程号
					item.setCourseName(es1.get(3).text().trim());			// 课程名
					item.setCourseType(es1.get(4).text().trim());			// 课程类型 
					item.setCourseProperty(es1.get(5).text().trim());		// 课程性质
					
					System.out.println("isNUM:    " + isNum(es1.get(6).text().trim().split(" ")[0]));

					// 学分
					if (isNum(es1.get(6).text().trim().split(" ")[0])) {
						// 如果是数字
						float credit = Float.parseFloat(es1.get(6).text().trim().split(" ")[0]);
						item.setCourseCredit(credit);
					} else {
						System.out.println("**credit:  	" + es1.get(6).text().trim().substring(0, 1) + "." + es1.get(6).text().trim().substring(2, 3));
					
						float credit = Float.parseFloat(es1.get(6).text().trim().substring(0, 1) + "." + es1.get(6).text().trim().substring(2, 3));
						item.setCourseCredit(credit);
					}
					
					item.setCourseGrade(es1.get(7).text().trim());	// 分数
					
					item.setStudyType(es1.get(8).text().trim());	// 修读类型
					
					item.setSelected(true);			// 默认已被选
				
					list.add(item);
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		}
		
		for (int i = 0; i < list.size(); i++) {
			System.out.println("************************************************");
			System.out.println("getCourseName   " + list.get(i).getCourseName());
			System.out.println("getCourseProperty   " + list.get(i).getCourseProperty());
			System.out.println("getCourseGrade  " + list.get(i).getCourseGrade());
			System.out.println("getCourseCredit   " + list.get(i).getCourseCredit());
			
			System.out.println("getCourseType   " + list.get(i).getCourseType());
			System.out.println("getCourseID   " + list.get(i).getCourseID());
			System.out.println("getCourseCredit   " + list.get(i).getCourseCredit());
			System.out.println("getYear   " + list.get(i).getYear());
			System.out.println("getSemesterNum   " + list.get(i).getSemesterNum());
			System.out.println("getStudyType   " + list.get(i).getStudyType());			
		}
		
		return HandleMessage.QUERY_SUCCESS;
	}
	
	// 判断是否是数字
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	
	// 添加课程号，用来添加课程信息
	private void addCourseID(String courseID) {
		if ( !StringUtils.isEmpty(courseID) ) {
			CourseIDSet.add(courseID);
		}
	}
	
	// 判断课程是否存在
	private boolean haveAdd(String courseID) {
		if (CourseIDSet.contains(courseID)) {
			return true;
		}

		return false;
	}

	// Post: 用来登录统一认证平台
	private int connectPost(String url) {
		// 设置超时
		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_CONNECT);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
		
		httpClient = new DefaultHttpClient(); // 新建HttpClient对象
		HttpPost httpPost = new HttpPost(url); // 新建HttpPost对象

		List<NameValuePair> params = new ArrayList<NameValuePair>(); // 使用NameValuePair来保存要传递的Post参数
		params.add(new BasicNameValuePair("IDToken1", vo.getAccount().trim())); // 添加要传递的参数
		params.add(new BasicNameValuePair("IDToken2", vo.getPassword().trim()));

		try {
			HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8); // 设置字符集
			httpPost.setEntity(entity); // 设置参数实体
			HttpResponse httpResp = httpClient.execute(httpPost); // 获取HttpResponse实例

			if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
				String result = EntityUtils.toString(httpResp.getEntity(),
						"UTF-8");

				return HandleMessage.QUERY_SUCCESS;
				// System.out.println(result);
			} else {
				// 响应未通过
				System.out.println(httpResp.getStatusLine().getStatusCode() + "");
				return HandleMessage.QUERY_ERROR;
			}
		} catch (MalformedURLException e) {
			Log.i("MalformedURLException", "MalformedURLException");
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		} catch (IllegalArgumentException e) {
			Log.i("IllegalArgumentException", "IllegalArgumentException");
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.i("ClientProtocolException", "QUERY_ERROR");
			return HandleMessage.QUERY_ERROR;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			Log.i("SocketTimeoutException", "QUERY_ERROR");
			return HandleMessage.QUERY_ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("IOException", "QUERY_ERROR");
			return HandleMessage.QUERY_ERROR;
		} catch (Exception e) {
			Log.i("Exception-connectPost", "QUERY_ERROR: " + e.toString());
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		}
	}

	// Get
	private int connectGet(String url) {
		HttpConnectionParams
				.setConnectionTimeout(httpClient.getParams(), 10000); // 设置连接超时
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10000); // 设置数据读取时间超时
		ConnManagerParams.setTimeout(httpClient.getParams(), 10000); // 设置从连接池中取连接超时

		HttpGet httpget = new HttpGet(url); // 获取请求

		try {
			HttpResponse response = httpClient.execute(httpget); // 执行请求，获取响应结果
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
				String result = EntityUtils.toString(response.getEntity(), "UTF-8");

				System.out.println("____________________________________________");
				if (url.equals(URL4) && !StringUtils.isEmpty(result)) {
					System.out.println(result);
					
					return parse(result);
				}
				
				return HandleMessage.QUERY_SUCCESS;
			} else {
				// 响应未通过
				System.out.println("connect: "
						+ response.getStatusLine().getStatusCode());
				return HandleMessage.QUERY_ERROR;
			}
		} catch (MalformedURLException e) {
			Log.i("MalformedURLException", "MalformedURLException");
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		} catch (IllegalArgumentException e) {
			Log.i("IllegalArgumentException", "IllegalArgumentException");
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.i("ClientProtocolException", "QUERY_ERROR");
			return HandleMessage.QUERY_ERROR;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			Log.i("SocketTimeoutException", "QUERY_ERROR");
			return HandleMessage.QUERY_ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("IOException", "QUERY_ERROR");
			return HandleMessage.QUERY_ERROR;
		} catch (Exception e) {
			Log.i("Exception——connect", "QUERY_ERROR: " + e.toString());
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		}
	}
	
	// Post: 查询成绩
	private int connectGradePost(String url) {
		HttpPost httpPost = new HttpPost(url); // 新建HttpPost对象

		List<NameValuePair> params = new ArrayList<NameValuePair>(); // 使用NameValuePair来保存要传递的Post参数
		params.add(new BasicNameValuePair("qXndm_all", vo.getYear())); 	// 学年
		params.add(new BasicNameValuePair("qXqdm_all", vo.getSemester()));	// 学期
		params.add(new BasicNameValuePair("qKclbdm", ""));	// 课程类别
		params.add(new BasicNameValuePair("qKcxzdm", ""));	// 课程性质
		
		try {
			HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8); // 设置字符集
			httpPost.setEntity(entity); // 设置参数实体
			HttpResponse httpResp = httpClient.execute(httpPost); // 获取HttpResponse实例

			if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
				String result = EntityUtils.toString(httpResp.getEntity(),
						"UTF-8");
				if (url.equals(URL4) && !StringUtils.isEmpty(result)) {
					System.out.println(result);
					
					return parse(result);
				}
			} else {
				// 响应未通过
				System.out.println(httpResp.getStatusLine().getStatusCode() + "");
				return HandleMessage.QUERY_ERROR;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return HandleMessage.QUERY_ERROR;
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
		
		return HandleMessage.QUERY_ERROR;
	}
}
