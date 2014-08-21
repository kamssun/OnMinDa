package com.newthread.android.ui.labquery;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.util.AndroidDB;
import com.newthread.android.util.MyPreferenceManager;

public class LabDetail_activity extends SherlockActivity {

	private ListView mListView;
	private LabListAdapter mAdapter;
	private ArrayList<LabDetail> list;
	private ImageButton mImageButton;
	private ProgressBar progressBar;
	public static final int Refresh_Sccuess = 12345;
	public static final int Refresh_Fail = 54321;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labdetail_activity);
		intiview();
		list = AndroidDB.queryLab(getApplicationContext());
		mListView = (ListView) findViewById(R.id.LabList);
		mAdapter = new LabListAdapter(LabDetail_activity.this, list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LabDetail item = list.get(arg2);
				Intent mIntent = new Intent(LabDetail_activity.this,
						LabItemActivity.class);
				mIntent.putExtra("data", item);
				Log.v("LabDetail_activity", item.getLabName());
				startActivity(mIntent);
			}
		});
	}

	private void intiview() {
		MyPreferenceManager.init(getApplicationContext());
		progressBar = (ProgressBar) findViewById(R.id.refresh_loading);
		mImageButton = (ImageButton) findViewById(R.id.Refresh);
		mImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mImageButton.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				RequestManager.getInstance().startRequest(
						new OnRequestResponseListener() {

							@Override
							public void onRequestSuccess(long requestId,
									String result) {
								hander.sendEmptyMessage(Refresh_Sccuess);
							}

							@Override
							public void onRequestApiError(long requestId,
									String result) {
								hander.sendEmptyMessage(Refresh_Fail);
							}
						},
						new LoginRequestApi(MyPreferenceManager.getString(
								"LabQuery_account", ""), MyPreferenceManager
								.getString("LabQuery_password", "")));
			}

		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("实验列表");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("退出登陆").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;

		}
		if (item.getTitle().equals("退出登陆")) {
			Intent intent = new Intent(this, LabLogin_activity.class);
			intent.putExtra("is_direct_login", false);
			startActivity(intent);
			this.finish();
		}
		return super.onMenuItemSelected(featureId, item);

	}

	final Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Refresh_Sccuess:
				mImageButton.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				Toast.makeText(LabDetail_activity.this, "刷新成功", 2 * 1000)
						.show();
				break;
			case Refresh_Fail:
				mImageButton.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				Toast.makeText(LabDetail_activity.this, "刷新失败", 2 * 1000)
						.show();
			}
		}

	};
}
