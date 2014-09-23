package com.newthread.android.ui.library;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.newthread.android.R;
import com.newthread.android.bean.CurrentBorrowItem;
import com.newthread.android.bean.LibraryCurrentBorrow;
import com.newthread.android.bean.PersonalInfo;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.util.AndroidDB;
import com.newthread.android.util.MyPreferenceManager;
import com.newthread.android.util.StringUtils;

public class LibraryLoginActivity extends Activity {
	private RadioGroup radioGroup;
	private RadioButton radioSID, radioBarID;
	private Document doc;
	private HttpClient httpClient;
	private int result = -1;		// 登录结果 
	private ProgressDialog mProgressDialog;
	private EditText mAccount;
	private EditText mPassword;
	private TextView mLogin;
	private PersonalInfo personalInfo;
	private ArrayList<CurrentBorrowItem> currentBorrowList;
	
	private String account;
	private String password;
	private String accountType;		// 帐号类型
	private long lastClickTime = 0;
	private static final String perLibraryUrl = "http://coin.lib.scuec.edu.cn/reader/redr_verify.php";	// 个人图书馆URL
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_login);
        
        initData();
        initView();
    }
	
	private void initData() {
		personalInfo = new PersonalInfo();		
	}

	private void initView() {
		radioGroup = (RadioGroup) this.findViewById(R.id.role_switch);
		radioSID = (RadioButton) this.findViewById(R.id.sid);
		radioBarID = (RadioButton) this.findViewById(R.id.barid);
		
		mAccount = (EditText) this.findViewById(R.id.library_login_account);
		mPassword = (EditText) this.findViewById(R.id.library_login_password);
		mLogin = (TextView) this.findViewById(R.id.library_login_btn);
		
		mLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (System.currentTimeMillis() - lastClickTime < 3000) {
					return;
				}
				lastClickTime = System.currentTimeMillis();
				
				if (!StringUtils.isEmpty(mAccount.getText().toString().trim()) 
					&& !StringUtils.isEmpty(mPassword.getText().toString().trim())) {
					account = mAccount.getText().toString().trim();
					password = mPassword.getText().toString().trim();
					
					// Login...
					performLogin();
				} else {
					Toast.makeText(getApplicationContext(), "请输入图书馆账号和密码", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		accountType = "cert_no";
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (radioSID.getId() == checkedId) {
					// 学号/证件号登录
					accountType = "cert_no";
				}
				if (radioBarID.getId() == checkedId) {
					// 条码号登录
					accountType = "bar_no";
				}
			}
			
			
		});
		
		// 获取历史登录类型
		MyPreferenceManager.init(getApplicationContext());
		// 判断是否首次登录
		if ( !MyPreferenceManager.getBoolean("library_isFirstLogin", true) ) {
			mAccount.setText(MyPreferenceManager.getString("library_account", "").trim());
			mPassword.setText(MyPreferenceManager.getString("library_password", ""));
			if (MyPreferenceManager.getString("library_login_type", "").equals("cert_no")) {
				radioSID.setChecked(true);
				accountType = "cert_no";
			} else {
				radioBarID.setChecked(true);
				accountType = "bar_no";
			}
		}
	}

	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mProgressDialog.dismiss();
			switch(msg.what) {
				case HandleMessage.QUERY_SUCCESS:
					Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
					
					// 查询成功
					MyPreferenceManager.init(getApplicationContext());
					// 保存账号密码信息
					MyPreferenceManager.commitString("library_account", account);
					MyPreferenceManager.commitString("library_password", password);
					MyPreferenceManager.commitString("library_login_type", accountType);
					MyPreferenceManager.commitBoolean("library_isFirstLogin", false);
					
					// 删除旧数据
					AndroidDB.deleteCurrentBorrowAll(getApplicationContext());
					// 存储新数据
					AndroidDB.addCurrentBorrowList(getApplicationContext(), currentBorrowList);
					AndroidDB.addPersonalInfo(getApplicationContext(), personalInfo);
					// 跳转
					Intent _intent = new Intent(LibraryLoginActivity.this, MyLibraryActivity.class);
					startActivity(_intent);
					LibraryLoginActivity.this.finish();
				break;
				case HandleMessage.QUERY_ERROR:
					// 查询失败
					Toast.makeText(getApplicationContext(), R.string.net_error, Toast.LENGTH_SHORT).show();
				break;
				case HandleMessage.NO_CONTENT:
					// 无信息
					Toast.makeText(getApplicationContext(), "请输入正确的图书馆账号和密码", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

	// 登录过程
	private void performLogin() {
		mProgressDialog = new ProgressDialog(LibraryLoginActivity.this);
		mProgressDialog.setMessage("正在登录...");
		mProgressDialog.show();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				connectPost(perLibraryUrl);
				handler.sendEmptyMessage(result);
			}
			
		}).start();
	}
	
	// 登录个人图书馆，并解析个人信息及当前借阅信息
	private void connectPost(String url) {
		httpClient = new DefaultHttpClient(); // 新建HttpClient对象
		HttpPost httpPost = new HttpPost(url); // 新建HttpPost对象
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(); // 使用NameValuePair来保存要传递的Post参数
		params.add(new BasicNameValuePair("number", account)); // 添加要传递的参数
		params.add(new BasicNameValuePair("passwd", password));
		params.add(new BasicNameValuePair("select", accountType));

		try {
			HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8); // 设置字符集
			httpPost.setEntity(entity); // 设置参数实体
			HttpResponse httpResp = httpClient.execute(httpPost); // 获取HttpResponse实例
			if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
				String httpresult = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
				if(parse_libraryresut(httpresult)!=14){
				System.out.println(httpresult);
				getPersonalInfo(httpresult);
				}else{
					result=HandleMessage.NO_CONTENT;
					return;
				}
			} else {
				// 响应未通过
				result = HandleMessage.QUERY_ERROR;
				System.out.println(httpResp.getStatusLine().getStatusCode() + "");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			result = HandleMessage.NO_CONTENT;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			result = HandleMessage.NO_CONTENT;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = HandleMessage.QUERY_ERROR;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			result = HandleMessage.QUERY_ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			result = HandleMessage.QUERY_ERROR;
		} catch (Exception e) {
			result = HandleMessage.QUERY_ERROR;
			e.printStackTrace();
		}
	}
	
	private int parse_libraryresut(String library_result) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(library_result);
		return doc.select("div").size();
	}

	// 解析个人基本信息、 借阅信息
	public LibraryCurrentBorrow getPersonalInfo(String str) {
		try {
			currentBorrowList = new ArrayList<CurrentBorrowItem>();
			
			doc = Jsoup.parse(str);
			
			Element e0 = doc.getElementsByClass("mylib_msg").first();
			System.out.println("5天： " + e0.getElementsByTag("a").first().text());
			
			// 个人信息
			Elements es = doc.getElementById("mylib_info").getElementsByTag("TR");
			Elements es1 = es.first().getElementsByTag("TD");
			personalInfo.setName(es1.get(1).text().substring(3));
			personalInfo.setBar(es1.get(3).text().substring(4));
			personalInfo.setSid(es1.get(2).text().substring(5));
			
			Elements es11 = es.get(6).getElementsByTag("TD");
			personalInfo.setCollege(es11.get(0).text().substring(5));
			personalInfo.setGender(es11.get(3).text().substring(3));
			
			Elements es111 = es.get(3).getElementsByTag("TD");
			personalInfo.setBorrowNum(es111.get(2).text().substring(5));
			
			System.out.println("姓名： " + es1.get(1).text().substring(3));
			System.out.println("学号：  " + es1.get(2).text().substring(5));
			System.out.println("借阅证号： " + es1.get(3).text().substring(4));
			System.out.println("college： " + es11.get(0).text().substring(5));
			System.out.println("借阅量： " + es111.get(2).text().substring(5));
		} catch (Exception e) {
			e.printStackTrace();
			result = HandleMessage.NO_CONTENT;
		}
		
		// 当前借阅href
		HttpPost httpPost = new HttpPost("http://coin.lib.scuec.edu.cn/reader/book_lst.php");
		HttpResponse httpResp = null;
		try {
			httpResp = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			result = HandleMessage.QUERY_ERROR;
			e.printStackTrace();
		} catch (Exception e) {
			result = HandleMessage.QUERY_ERROR;
			e.printStackTrace();
		}

		if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
			try {
				String currentStr = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
				
				doc = Jsoup.parse(currentStr);
				
				Elements es3 = doc.getElementsByTag("table").first().getElementsByTag("tr");
				
				for (int i = 1; i < es3.size(); i++) {
					CurrentBorrowItem item = new CurrentBorrowItem();
					item.setBookName(es3.get(i).select("[width=35%]").text());
					item.setCanRenew(es3.get(i).select("[width=6%]").text().equals("1") ? false : true);
					item.setOverTime(es3.get(i).select("[width=13%]").get(1).text());
					
					currentBorrowList.add(item);
				}
				
			}  catch (MalformedURLException e) {
				e.printStackTrace();
				result = HandleMessage.NO_CONTENT;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				result = HandleMessage.NO_CONTENT;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				result = HandleMessage.QUERY_ERROR;
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				result = HandleMessage.QUERY_ERROR;
			} catch (IOException e) {
				e.printStackTrace();
				result = HandleMessage.QUERY_ERROR;
			} catch (Exception e) {
				result = HandleMessage.NO_CONTENT;
				e.printStackTrace();
			}
		} else {
			// 响应未通过
			result = HandleMessage.QUERY_ERROR;
			System.out.println(httpResp.getStatusLine().getStatusCode()	+ "");
		}
		
		result = HandleMessage.QUERY_SUCCESS;
		
		return null;
	}
}
