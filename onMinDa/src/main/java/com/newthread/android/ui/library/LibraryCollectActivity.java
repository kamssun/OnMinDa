package com.newthread.android.ui.library;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.adapter.LibraryCollectAdapter;
import com.newthread.android.bean.BookCollectItem;
import com.newthread.android.util.AndroidDB;

/**
 * 图书馆收藏
 */
public class LibraryCollectActivity extends SherlockActivity {
	private ListView listView;
	private ArrayList<BookCollectItem> list;
	private static final boolean DEBUG = false;
	private LibraryCollectAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_collect);

		initData();
		initView();
	}

	// 初始化数据
	private void initData() {
		list = new ArrayList<BookCollectItem>();
		// 从数据库中提取缓存数据
		getCollectData();
	}

	// 从数据库中读取数据
	private void getCollectData() {
		list = AndroidDB.query(getApplicationContext());
	}

	// 初始化界面
	private void initView() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("图书收藏");

		listView = (ListView) this.findViewById(R.id.collect_list);

		adapter = new LibraryCollectAdapter(getApplicationContext(), list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 查看详细
				Intent _intent = new Intent(getApplicationContext(),
						LibraryDetailActivity.class);
				_intent.putExtra("href", list.get(position).getUrl().trim());
				_intent.putExtra("title", list.get(position).getTitle().trim());
				startActivity(_intent);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}

		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 删除
				deleteCollect(position);
				return false;
			}
		
		});

	}

	// 删除收藏的图书
	private void deleteCollect(final int index) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确认删除该图书信息？");
		builder.setTitle("图书删除");

		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				AndroidDB.deleteBookCollect(getApplicationContext(), list.get(index));
				list.remove(index);
				adapter.notifyDataSetChanged();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}
