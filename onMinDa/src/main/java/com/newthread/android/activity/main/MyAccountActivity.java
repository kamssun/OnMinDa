package com.newthread.android.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.ui.coursechart.CourseChartLoginActivity;
import com.newthread.android.ui.library.LibraryLoginActivity;

public class MyAccountActivity extends SherlockActivity {
	private TextView libraryAccount, adminSystemAccount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		
		initView();
	}

	private void initView() {
		ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setHomeButtonEnabled(true);
        ab.setTitle("我的账号");
        
        libraryAccount = (TextView) this.findViewById(R.id.library_account);
        adminSystemAccount = (TextView) this.findViewById(R.id.admin_system_account);
        
//        libraryAccount.setText("11061181");
//        adminSystemAccount.setText("91108460");
		
		// 图书馆账号
		this.findViewById(R.id.library).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent _intent = new Intent(MyAccountActivity.this, LibraryLoginActivity.class);
				startActivity(_intent);
			}
		});
		
		// 教务系统账号 
		this.findViewById(R.id.admin_system).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent _intent = new Intent(MyAccountActivity.this, CourseChartLoginActivity.class);
				_intent.putExtra("is_direct_login", false);
				startActivity(_intent);
			}
		});
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
