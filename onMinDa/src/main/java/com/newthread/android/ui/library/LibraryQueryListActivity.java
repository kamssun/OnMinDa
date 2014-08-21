package com.newthread.android.ui.library;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.adapter.LibraryQueryListAdapter;
import com.newthread.android.bean.LibraryQueryItemInfo;
import com.newthread.android.bean.LibraryQueryListReturnPara;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.service.LibraryQuery;
import com.newthread.android.util.CroutonUtil;
import com.newthread.android.util.Logger;

public class LibraryQueryListActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener {
	private ActionBar ab;
	private ProgressBar progressBar;
	private ProgressBar footer_progressBar;
	private View list_footer_view;
	private TextView prompt;
	private TextView footer_text;
	private String totalSize;
	private String keyStr;
	private LibraryQueryListReturnPara returnPara;
	private List<LibraryQueryItemInfo> list;
	private ListView listView;
	private LibraryQueryListAdapter adapter;

	private boolean isFirstLoad; 

	private static final boolean DEBUG = false;
	
	private String [] types = {"全部","可借"};
	private boolean isOnlylendable = false;		// 是否只可借阅
	private static final int PAGE_MAX_NUM = 20;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_query_list);

		initData();
		initView();
	}

	// 初始化数据
	private void initData() {
		
	}

	// 初始化界面
	public void initView() {
		ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("查询列表");
		
		// Spinner
        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> typeList = ArrayAdapter.createFromResource(context, R.array.types, R.layout.sherlock_spinner_item);
        typeList.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(typeList, this);

		totalSize = "0";
		keyStr = this.getIntent().getStringExtra("key");

		listView = (ListView) findViewById(R.id.library_listview);
		progressBar = (ProgressBar) this.findViewById(R.id.library_list_loading);
		prompt = (TextView) this.findViewById(R.id.library_list_prompt);
		list_footer_view = this.getLayoutInflater().inflate(R.layout.listview_footer, null);
		footer_text = (TextView) list_footer_view.findViewById(R.id.footer_text);
		footer_progressBar = (ProgressBar) list_footer_view.findViewById(R.id.footer_brogress_bar);
		View footer_view = list_footer_view.findViewById(R.id.listview_footer);

		footer_text.setText("加载更多");
		footer_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 加载更多
				getMoreItem(isOnlylendable);
			}
		});

		listView.addFooterView(list_footer_view);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long arg3) {
//				Toast.makeText(getApplicationContext(),
//						"" + position + " || " + list.size(), Toast.LENGTH_LONG)
//						.show();
//				System.out.println("" + list.get(position).getLink());

				// 跳转到图书详情
				Intent _intent = new Intent(getApplicationContext(), LibraryDetailActivity.class);
				_intent.putExtra("href", list.get(position).getLink().trim());
				_intent.putExtra("title", list.get(position).getTitle().trim());
				startActivity(_intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}
		});
		
		list = new ArrayList<LibraryQueryItemInfo>();
	}
	
	// Spinner监听
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if (0 == itemPosition) {
			isOnlylendable = false;
		} else {
			isOnlylendable = true;
		}
		// 清空当前List
		if (list != null && list.size() > 0) { 
			list.clear();
			adapter.notifyDataSetChanged();
		}
		
		// 重新查找
		performQuery(isOnlylendable);
//		listView.removeFooterView(list_footer_view);
//		Toast.makeText(getApplicationContext(), "" + types[itemPosition], Toast.LENGTH_SHORT).show();
		return true;
	}

	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandleMessage.QUERY_SUCCESS:
				// 查询成功
				progressBar.setVisibility(View.GONE);
				prompt.setVisibility(View.GONE);

				if (isFirstLoad) {
					totalSize = returnPara.getTotalSize();
					ab.setSubtitle(keyStr + "_共查询到" + totalSize + "项");
					CroutonUtil.showInfoCrouton(LibraryQueryListActivity.this, true, "共查询到" + totalSize + "项", 0);

					adapter = new LibraryQueryListAdapter(LibraryQueryListActivity.this, list);
					listView.setAdapter(adapter);
				}

				if (!isFirstLoad) {
					adapter.notifyDataSetChanged();
					ab.setSubtitle(keyStr + "_共查询到" + returnPara.getTotalSize() + "项");
					footer_text.setText("加载更多");
					footer_progressBar.setVisibility(View.GONE);
				}
				
				// 如果已查询数等于总数
				if ( list.size() >= Integer.parseInt(totalSize) ) {
					listView.removeFooterView(list_footer_view);
				}

				break;
			case HandleMessage.QUERY_ERROR:
				// 查询失败
				progressBar.setVisibility(View.GONE);
				if (isFirstLoad) {
					prompt.setVisibility(View.VISIBLE);
					prompt.setText("网络错误");
				} else {
					Toast.makeText(getApplicationContext(), "加载失败",
							Toast.LENGTH_SHORT).show();
					footer_text.setText("网络异常, 请检查网络");
					footer_progressBar.setVisibility(View.GONE);
				}
				break;
			case HandleMessage.NO_CONTENT:
				// 无信息
				progressBar.setVisibility(View.GONE);
				prompt.setVisibility(View.VISIBLE);
				prompt.setText("没有您想要查询的信息");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	// 图书查询
	public void performQuery(boolean isOnlylendable) {
		// 显示等待框
		progressBar.setVisibility(View.VISIBLE);
		
		// 构造查询UR
		String url = null;
		try {
			if (DEBUG) {
				url = "http://coin.lib.scuec.edu.cn/opac/openlink.php?location=ALL&title="
						+ new String("java".getBytes(), "ISO-8859-1")
						+ "&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&with_ebook=&with_ebook=";
			} else {
				if ( !isOnlylendable ) {
					url = "http://coin.lib.scuec.edu.cn/opac/openlink.php?location=ALL&title="
							+ new String(keyStr.trim().getBytes(), "ISO-8859-1")
							+ "&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&with_ebook=&with_ebook=";
				} else {
					url = "http://coin.lib.scuec.edu.cn/opac/openlink.php?location=ALL&title="
							+ new String(keyStr.trim().getBytes(), "ISO-8859-1")
							+ "&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=yes&count=1231&with_ebook=";
				}
			}
		} catch (Exception e) {
			e.toString();
		}

		isFirstLoad = true;
		// 开启查询新线程
		new Thread(new QueryThread(LibraryQueryListActivity.this, url)).start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	// 线程：图书查询
	private class QueryThread implements Runnable {
		private String url;
		private Context con;

		public QueryThread(Context con, String url) {
			this.con = con;
			this.url = url;
		}

		@Override
		public void run() {
			int result = -1;// 查询结果
			returnPara = new LibraryQueryListReturnPara();
			result = new LibraryQuery(con, url, list, returnPara).query();

			handler.sendEmptyMessage(result);
		}
	}

	// 加载更多
	public void getMoreItem(boolean isOnlylendable) {
		isFirstLoad = false;

		// 加载更多
		footer_progressBar.setVisibility(View.VISIBLE);
		footer_text.setText("正在加载...");

		// 下页URL
		int nextPageNum = list.size() / 20 + 1;
		String nextPageUrl = null;
		try {
			// 构造URL并转码
			if (DEBUG) {
				nextPageUrl = "http://coin.lib.scuec.edu.cn/opac/openlink.php?"
						+ "location=ALL&title="
						+ new String("java".getBytes(), "ISO-8859-1")
						+ "&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE"
						+ "&onlylendable=no&with_ebook=&page="
						+ nextPageNum;
			} else {
				if ( !isOnlylendable ) {
					// 全部
					nextPageUrl = "http://coin.lib.scuec.edu.cn/opac/openlink.php?"
							+ "location=ALL&title="
							+ new String(keyStr.getBytes(), "ISO-8859-1")
							+ "&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE"
							+ "&onlylendable=no&with_ebook=&page="
							+ nextPageNum;
				} else {
					// 可借
					nextPageUrl = "http://coin.lib.scuec.edu.cn/opac/openlink.php?" +
							"location=ALL&title="
							+ new String(keyStr.getBytes(), "ISO-8859-1") 
							+ "&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE"
							+ "&onlylendable=yes&with_ebook=&page="
							+ nextPageNum;
				}
				
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Logger.i("nextHref::", nextPageUrl);
		
		// 开启查询新线程
		new Thread(new QueryThread(LibraryQueryListActivity.this, nextPageUrl)).start();
	}

	// 对返回键进行监听
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	this.finish();
        	overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        	return true;
         }
         return super.onKeyDown(keyCode, event);
     }

}
