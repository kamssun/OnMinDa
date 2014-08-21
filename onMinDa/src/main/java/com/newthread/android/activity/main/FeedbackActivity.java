package com.newthread.android.activity.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;

public class FeedbackActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		initView();
	}

	// 初始化界面
	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setHomeButtonEnabled(true);
        ab.setTitle("问题反馈");
        
		// 邮箱
		this.findViewById(R.id.email).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendFeedbackEmail();
			}

		});

		// 微博
		this.findViewById(R.id.weibo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lookWeibo();
				finish();
			}

		});
	}

	// 发送问题反馈邮件
	private void sendFeedbackEmail() {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		String[] recipients = new String[] { "sushun001@gmail.com", "", };
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "人在民大问题反馈");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				"问题、机型等信息.Thank you.");
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
	
	// 查看微博
	private void lookWeibo() {
		Uri uri = Uri.parse("http://weibo.com/2203740884/profile?rightmod=1&wvr=5&mod=personinfo");
		Intent it  = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(it);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish(  );
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
