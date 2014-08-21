package com.newthread.android.ui.news;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.newthread.android.R;
import com.newthread.android.bean.NewsListItem;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.service.NewsListQuery;
import com.newthread.android.util.AndroidDB;
import com.newthread.android.util.Logger;
import com.newthread.android.util.MyAnimation;
import com.newthread.android.util.StringUtils;

public class NewsFragmentOne extends Fragment {
	private ArrayList<NewsListItem> list;
	private static Context con;
	private PullToRefreshListView listView;
	private ProgressBar progressBar;
	private NewsListAdapter adapter;
	private View mStatusPrompt;	// 状态提醒
	private TextView mStatuesPromptText; // 状态提醒文字信息
	private ProgressBar mStatusPromptPB;
	private PullToRefreshBase<ListView> mRefreshView;
	
	private boolean isLoading = false;
	private boolean isRefreshSuccess = false;  // 本次刷新是否成功
	
	private static String label = null;	// 加载时间
	private int visibleLastIndex = 0;	// 最后的可视项索引
	public static final int PER_PAGE_NUM = 12;	// 每页item数量
	
	public NewsFragmentOne() {
	}
	
	public NewsFragmentOne(Context con, ArrayList<NewsListItem> list) {
		this.con = con;
		this.list = list;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news_one, container, false);
		
		listView = (PullToRefreshListView)view.findViewById(R.id.news_listview_one);
		progressBar = (ProgressBar) view.findViewById(R.id.news_loading_one);
		progressBar.setVisibility(View.GONE);
		mStatusPrompt = (View) view.findViewById(R.id.news_one_status_prompt);
		mStatuesPromptText = (TextView) view.findViewById(R.id.news_one_status_prompt_text);
		mStatusPromptPB = (ProgressBar) view.findViewById(R.id.news_one_status_prompt_pb);
		
		// Item监听
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
				Intent _intent = new Intent(con, NewsContentActivity.class);
				_intent.putExtra("title", StringUtils.isEmpty(list.get(position - 1).getTitle()) ? "无" : list.get(position - 1).getTitle());
				_intent.putExtra("time", StringUtils.isEmpty(list.get(position - 1).getTime()) ? "无" : list.get(position - 1).getTime());
				_intent.putExtra("href", list.get(position - 1).getHref().trim());
				_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				con.startActivity(_intent);
			}
		});
		
		// 刷新监听
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// 刷新
				// 记录更新的时间并在此显示
//				if (MyPreferenceManager.getBoolean("news_one_isFirstLoad", true)) {
//					Logger.i("isFirstLoad","--1");
//					label = DateUtils.formatDateTime(con, System.currentTimeMillis(),
//							   DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//					// 记录记载时间
//					MyPreferenceManager.commitBoolean("news_one_isFirstLoad", false);
//				} else {
//					Logger.i("isFirstLoad","--2");
//					label = DateUtils.formatDateTime(con, MyPreferenceManager.getLong("news_one_last_load_time", System.currentTimeMillis()),
//							   DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//				}
//				MyPreferenceManager.commitLong("news_one_last_load_time", System.currentTimeMillis());	// 最后的加载时间(未格式化)
//				
				// Update the LastUpdatedLabel
//				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
				if (list.size() > PER_PAGE_NUM) {
					list.clear();
				}
				
				mRefreshView = refreshView;
				performQuery();
			}
			
		});
		
		// 从缓存中提取新闻列表信息
		list = AndroidDB.getNewsListByType(con, "1");
		
		Logger.i("list.size", list.size() + "");
		
		adapter = new NewsListAdapter(con, list);
		listView.setAdapter(adapter);
		
		//  设置列表滑动监听
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == adapter.getCount()) {
				
					if (!isLoading) {
						mStatusPrompt.setVisibility(View.VISIBLE);
						mStatuesPromptText.setText("正在加载...");
						mStatusPrompt.setVisibility(View.VISIBLE);
						mStatusPrompt.startAnimation(MyAnimation.B_bottomToUp());
						
						loadMore();
					}
					
				}
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				visibleLastIndex = firstVisibleItem + visibleItemCount - 2;
			}
		});
			
		return view;
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (isRefreshSuccess) {
			if (list.size() >= 24) {
				// 最多只存储24条最新记录
				AndroidDB.addNewsList(con, list.subList(0, 23), "1");	
			} else {
				AndroidDB.addNewsList(con, list, "1");
			}
		}
	}

	private Handler fragmentHandler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			if (mRefreshView != null) {
				mRefreshView.onRefreshComplete();
			}
			if (msg.what == HandleMessage.QUERY_SUCCESS) {
				mStatuesPromptText.setText("成功加载12条新闻");
				mStatusPromptPB.setVisibility(View.GONE);
			}
			// 关闭提示
			if (mStatusPrompt.isShown()) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mStatusPrompt.setVisibility(View.GONE);
						mStatusPrompt.startAnimation(MyAnimation.B_upToBottom());
					}
					
				}, 1000);
			}
			switch(msg.what) {
			case HandleMessage.QUERY_SUCCESS:
				// 成功
				isLoading = false;
				Logger.i("NewsFragmentOne__handleMessage", "********QUERY_SUCCESS " + adapter.getCount());
				adapter.notifyDataSetChanged();
				
				if (list.size() > 0) {
					isRefreshSuccess = true;
				}
				
				break;
			case HandleMessage.QUERY_ERROR:
				// 失败
				isLoading = false;
				Logger.i("NewsFragmentOne__handleMessage", "QUERY_ERROR");
				
				break;
			case HandleMessage.NO_CONTENT:
				isLoading = false;
				Logger.i("NewsFragmentOne__handleMessage", "NO_CONTENT");

				break;
			default:
				break;
			}
		}
		
	};
	
	// 加载更多
	public void loadMore() {
		// 下页URL
		final int nextPageNum = (list.size() / 12) + 1;
		Logger.i("loadMore", "page.size " + nextPageNum);
		new Thread(new Runnable() {

			@Override
			public void run() {
				isLoading = true;
				int result = -1;
				// 查询下一页新闻
				result = new NewsListQuery(list, 0, ("http://news.scuec.edu.cn/xww/?class-focusNews-page-" + nextPageNum)).queryMore();
				
				fragmentHandler.sendEmptyMessage(result);
			}
			
		}).start();
	}
	
	// 查询过程
	public void performQuery() {
		new Thread(new QueryThread()).start();
	}
	
	// 线程：查询
	private class QueryThread implements Runnable {

		@Override
		public void run() {
			int result = -1;
			result = new NewsListQuery(list, 0).query();
			
			fragmentHandler.sendEmptyMessage(result);
			Logger.i("NewsFragmentOne_QueryThread", "result: "  + result);
		}
		
	}
	
	// 将新闻列表数据写入SDCard
//	public static void writeDataToFile(ArrayList<NewsListItem> list, int pageNum) {
//		try {
//			JSONObject jo = new JSONObject();
//			JSONArray ja = new JSONArray();
//			
//			// 当前时间
//			label = DateUtils.formatDateTime(con, System.currentTimeMillis(),
//					DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//			jo.put("news_one_last_load_time_format", label); // 最后的加载时间(已格式化)
//			
//			int i = 0;
//			int length = 0;
//			// 只存12条
//			if (list.size() > PER_PAGE_NUM) {
//				length = PER_PAGE_NUM;
//			}
//			for (; i < length; i++) {
//				JSONObject obj = new JSONObject();
//				
//				obj.put("title", list.get(i).getTitle());
//				obj.put("digest", list.get(i).getDigest());
//				obj.put("time", list.get(i).getTime());
//				obj.put("href", list.get(i).getHref());
//				
//				ja.put(obj);
//			}
//			jo.put("list", ja);
//			
//			NewsListActivity.writeStrToFile(jo.toString(), "", pageNum);
//			
//			Logger.i("json:   ", jo.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void getNewsOneData() {
//		// 读取字符串
//		String str = NewsListActivity.getStrFromFile("mnt/sdcard/OnMinDa", 0);
//		Logger.i("str: ", str);
//		
//		list = new ArrayList<NewsListItem>();
//		if (str != null) {
//			// 解析
//			try {
//				JSONObject obj = new JSONObject(str);
//				JSONArray ja = new JSONArray();
//				
//				ja = obj.getJSONArray("list");
//				
//				int i = 0;
//				JSONObject object = new JSONObject();
//				for (; i < ja.length(); i++) {
//					object = ja.getJSONObject(i);
//					NewsListItem item = new NewsListItem();
//					
//					item.setTitle(object.getString("title"));
//					item.setDigest(object.getString("digest"));
//					item.setTime(object.getString("time"));
//					item.setHref(object.getString("href"));
//					
//					list.add(item);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
}
