package com.newthread.android.activity.main;

import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;

public class AboutActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		initView();
	}

	private void initView() {
		ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setHomeButtonEnabled(true);
        ab.setTitle("关于");
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
