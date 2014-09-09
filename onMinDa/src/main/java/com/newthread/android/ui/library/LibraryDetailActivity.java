package com.newthread.android.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.BookCollectItem;
import com.newthread.android.bean.LibraryDetailBookInfo;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.service.LibraryDetailQuery;
import com.newthread.android.util.AndroidDB;
import com.newthread.android.util.CroutonUtil;
import com.newthread.android.util.TimeUtil;
import com.viewpagerindicator.UnderlinePageIndicator;

public class LibraryDetailActivity extends SherlockFragmentActivity {

	private boolean onPause = false;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		onPause = true;
	}

	private static final boolean DEBUG = false;
	private ProgressBar mProgressBar;
	private DetailFragmentAdapter mAdapter;
	private ViewPager mPager;
	private String href;
	private LibraryDetailBookInfo bookInfo;
	private String title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_detail_main);
		initView();
		queryPerform();
	}

	public void initView() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("查询详细");

		mProgressBar = (ProgressBar) this
				.findViewById(R.id.library_detail_loading_one);
		mProgressBar.setVisibility(View.VISIBLE);

		setSupportProgressBarIndeterminateVisibility(true);

		bookInfo = new LibraryDetailBookInfo();

		mAdapter = new DetailFragmentAdapter(getSupportFragmentManager(),
				bookInfo, getApplicationContext());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setBackgroundColor(this.getResources().getColor(R.color.red));
		indicator.setFades(false);

		// 获得该Item链接
		href = getIntent().getCharSequenceExtra("href").toString();
		title = getIntent().getCharSequenceExtra("title").toString();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mProgressBar.setVisibility(View.GONE);
			if (!onPause) {
				switch (msg.what) {
				case HandleMessage.QUERY_SUCCESS:
					mAdapter.notifyDataSetChanged();
					break;
				case HandleMessage.QUERY_ERROR:
					Toast.makeText(getApplicationContext(), R.string.net_error,
							Toast.LENGTH_SHORT).show();
					break;
				case HandleMessage.NO_CONTENT:
					Toast.makeText(getApplicationContext(), R.string.net_error,
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
			super.handleMessage(msg);
		}

	};
	private Thread queryThread;

	public void queryPerform() {
		// new Thread(new QueryThread()).start();
		queryThread = new Thread(new QueryThread());
		queryThread.start();
	}

	private class QueryThread implements Runnable {

		@Override
		public void run() {
			int result = -1;
			result = new LibraryDetailQuery(href, bookInfo).query();

			System.out.println("result: " + result);
			handler.sendEmptyMessage(result);
		}

	}

	// 图书收藏
	private int addBookCollect() {
		// 判断是否已经收藏
		if (bookInfo.getList().size() > 0
				&& AndroidDB.haveCollected(getApplicationContext(), bookInfo
						.getList().get(0).getId())) {
			return 0;
		}

		// 存储
		BookCollectItem item = new BookCollectItem();

		item.setTitle(bookInfo.getTitle());
		item.setAuther(bookInfo.getAuther() != null ? bookInfo.getAuther()
				: "无信息");
		item.setPublisher(bookInfo.getPublisher() != null ? bookInfo
				.getPublisher() : "无信息");
		if (bookInfo.getList().size() > 1) {
			item.setPlace((bookInfo.getList().get(0).getLocation() != null && bookInfo
					.getList().get(1).getLocation() != null) ? bookInfo
					.getList().get(0).getLocation()
					+ "  |  " + bookInfo.getList().get(1).getLocation() : "无信息");
		} else {
			item.setPlace((bookInfo.getList().get(0).getLocation() != null) ? bookInfo
					.getList().get(0).getLocation()
					: "无信息");
		}
		item.setId(bookInfo.getList().get(0).getId() != null ? bookInfo
				.getList().get(0).getId() : "无信息");
		item.setAddTime(TimeUtil.getCurrentTime());
		item.setUrl(href);

		AndroidDB.addBookCollect(getApplicationContext(), item);

		return 1;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Logger.i("bookInfo.getList().size()  ", "" +
		// bookInfo.getList().size());
		// Logger.i("是否存在", "    " +
		// AndroidDB.haveCollected(getApplicationContext(),
		// bookInfo.getList().get(0).getId()));
		// 判断是否已经收藏
		if (bookInfo.getList().size() > 0
				&& AndroidDB.haveCollected(getApplicationContext(), bookInfo
						.getList().get(0).getId())) {
			menu.add("收藏").setIcon(R.drawable.ic_collect_on)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

			// Logger.i("ic_collect_on", "ic_collect_on");
		} else {
			menu.add("收藏").setIcon(R.drawable.ic_collect_off)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

			// Logger.i("ic_collect_off", "ic_collect_off");
		}

		menu.add("分享").setIcon(R.drawable.ic_share)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("收藏")) {
			if (bookInfo.getList().size() > 0) {
				int result = addBookCollect();

				if (result == 1) {
					item.setIcon(R.drawable.ic_collect_on);
					CroutonUtil.showInfoCrouton(LibraryDetailActivity.this,
							true, "收藏成功", 0);
				} else {
					CroutonUtil.showAlertCrouton(LibraryDetailActivity.this,
							true, "该书已收藏", 0);
				}

			}
		} else if (item.getTitle().equals("分享")) {
			if (bookInfo.getList().size() > 0) {
				startActivity(createShareIntent());
			}
		}

		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Creates a sharing {@link Intent}.
	 * 
	 * @return The sharing intent.
	 */
	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		// shareIntent.setType("image/*");
		// Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
		// shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		shareIntent.putExtra(Intent.EXTRA_TEXT, "<<" + title
				+ ">> 这本书很不错呦。同学们赶快去借吧。猛击这里:" + href);
		shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return shareIntent;
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
