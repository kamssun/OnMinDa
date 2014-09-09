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
import org.jsoup.select.Elements;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.adapter.CurrentBorrowAdapter;
import com.newthread.android.bean.CurrentBorrowItem;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.service.ReNewBookPost;
import com.newthread.android.util.AndroidDB;
import com.newthread.android.util.MyPreferenceManager;
import com.newthread.android.util.StringUtils;

public class LibraryCurrentBorrowActivity extends SherlockActivity {
	private HttpClient httpClient;
	private ListView listView;
	private ProgressBar mProgressBar;
	private String account;
	private String password;
	private String accountType;
	private ReNewBookPost rnbp;

	private ArrayList<CurrentBorrowItem> list;
	private ArrayList<CurrentBorrowItem> currentBorrowList;
	private int result = -1; // 登录结果

	private static final String perLibraryUrl = "http://coin.lib.scuec.edu.cn/reader/redr_verify.php"; // 个人图书馆URL
	private static final String CURRENT_BORROW_URL = "http://coin.lib.scuec.edu.cn/reader/book_lst.php"; // 当前借阅URL

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_borrow);

		initData();
		initView();
	}

	private void initData() {
		MyPreferenceManager.init(getApplicationContext());
		account = MyPreferenceManager.getString("library_account", "");
		password = MyPreferenceManager.getString("library_password", "");
		accountType = MyPreferenceManager.getString("library_login_type", "");
		list = AndroidDB.queryCurrentBorrow(getApplicationContext());
	}

	private void initView() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("当前借阅");

		mProgressBar = (ProgressBar) this.findViewById(R.id.loading);
		listView = (ListView) this.findViewById(android.R.id.list);
		listView.setAdapter(new CurrentBorrowAdapter(getApplicationContext(),
				list));
		if (!canUpdate()) {
			showLoginDialog();
		} else {

			performUpdate();
		}
		// listView.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View v, int position,
		// long arg3) {
		// }
		// });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("刷新").setIcon(R.drawable.ic_refresh)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	// 刷新没用
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("刷新")) {
			// 刷新当前借阅信息
			if (!canUpdate()) {
				showLoginDialog();
			}

			performUpdate();
		}

		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 判断是否可刷新
	private boolean canUpdate() {
		if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(password)) {
			return true;
		}
		return false;
	}

	// 登录对话框
	protected void showLoginDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("请重新登录图书馆");
		builder.setTitle("提示");

		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent _intent = new Intent(LibraryCurrentBorrowActivity.this,
						LibraryLoginActivity.class);
				startActivity(_intent);
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mProgressBar.setVisibility(View.GONE);
			switch (msg.what) {
			case HandleMessage.QUERY_SUCCESS:
				Toast.makeText(getApplicationContext(), "刷新成功,触摸可续借",
						Toast.LENGTH_SHORT).show();

				// 删除旧数据
				AndroidDB.deleteCurrentBorrowAll(getApplicationContext());
				// 存储数据
				AndroidDB.addCurrentBorrowList(getApplicationContext(),
						currentBorrowList);
				rnbp = new ReNewBookPost((DefaultHttpClient) httpClient,
						handler);
				listView.setAdapter(new CurrentBorrowAdapter(
						getApplicationContext(), currentBorrowList));
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						CurrentBorrowItem choosebook = currentBorrowList
								.get(arg2);
						rnbp.RenewBook(choosebook.getRenNewBook_str1(),
								choosebook.getRenNewBook_str2());
					}
				});
				break;
			case HandleMessage.BORROW_FINISH:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case HandleMessage.QUERY_ERROR:
				// 查询失败
				Toast.makeText(getApplicationContext(), R.string.net_error,
						Toast.LENGTH_SHORT).show();
				break;
			case HandleMessage.NO_CONTENT:
				// 无信息
				Toast.makeText(getApplicationContext(), "请输入正确的图书馆账号和密码",
						Toast.LENGTH_SHORT).show();
				break;
			case HandleMessage.CURRENT_BORROW_NULL:
				Toast.makeText(getApplicationContext(), "当前借阅为空",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	// 刷新过程
	private void performUpdate() {
		mProgressBar.setVisibility(View.VISIBLE);

		new Thread(new Runnable() {

			@Override
			public void run() {
				connectPost(perLibraryUrl);

				handler.sendEmptyMessage(result);
			}

		}).start();
	}

	// 登录个人图书馆，并解析个人信息及当前借阅信息
	private int connectPost(String url) {
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
				String result = EntityUtils.toString(httpResp.getEntity(),
						"UTF-8");

				return getInfo(result);
			} else {
				// 响应未通过
				result = HandleMessage.QUERY_ERROR;
				System.out.println(httpResp.getStatusLine().getStatusCode()
						+ "");
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
		return result;
	}

	// 解析个人基本信息、 借阅信息
	public int getInfo(String str) {
		currentBorrowList = new ArrayList<CurrentBorrowItem>();

		Document doc = null;

		// 当前借阅href
		HttpPost httpPost = new HttpPost(CURRENT_BORROW_URL);
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
				String currentStr = EntityUtils.toString(httpResp.getEntity(),
						"UTF-8");
				doc = Jsoup.parse(currentStr);
				Elements es3 = doc.getElementsByTag("table").first()
						.getElementsByTag("tr");

				for (int i = 1; i < es3.size(); i++) {
					CurrentBorrowItem item = new CurrentBorrowItem();
					item.setBookName(es3.get(i).select("[width=35%]").text());
					item.setCanRenew(es3.get(i).select("[width=6%]").text()
							.equals("1") ? false : true);
					item.setOverTime(es3.get(i).select("[width=13%]").get(1)
							.text());

					currentBorrowList.add(item);
					System.out.println("title: "
							+ es3.get(i).select("[width=35%]").text());
					System.out.println("canRenew: "
							+ es3.get(i).select("[width=6%]").text());
					System.out.println("overTime: "
							+ es3.get(i).select("[width=13%]").get(1).text());

					String book_str = es3.get(i).select("[width=5%]")
							.toString();
					book_str = book_str.substring(book_str
							.indexOf("getInLib('") + 10);
					String strs[] = book_str.split("','");
					item.setRenNewBook_str1(strs[0]);
					item.setRenNewBook_str2(strs[1]);
				}

				result = HandleMessage.QUERY_SUCCESS;
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
				// 错误地方
				result = HandleMessage.CURRENT_BORROW_NULL;
				e.printStackTrace();
			}

		} else {
			// 响应未通过
			result = HandleMessage.QUERY_ERROR;
			System.out.println(httpResp.getStatusLine().getStatusCode() + "");
		}

		return result;
	}
}
