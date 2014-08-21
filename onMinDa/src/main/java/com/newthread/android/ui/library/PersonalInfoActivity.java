package com.newthread.android.ui.library;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.PersonalInfo;
import com.newthread.android.util.AndroidDB;
import com.newthread.android.util.Logger;

public class PersonalInfoActivity extends SherlockActivity {
	private TextView name, sId, barId, borrowNum;
	private PersonalInfo info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		
		initData();
		initView();
	}

	private void initData() {
		info = AndroidDB.queryPersonalInfo(getApplicationContext());
		
		Logger.i("~~~~~~~~~~~info", info.toString());
	}

	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setTitle("个人信息");
		
		name = (TextView) this.findViewById(R.id.name);
		sId = (TextView) this.findViewById(R.id.student_id);
		barId = (TextView) this.findViewById(R.id.bar_code_id);
		borrowNum = (TextView) this.findViewById(R.id.borrow_num);
		
		if (info != null) {
			name.setText(info.getName());
			sId.setText(info.getSid());
			barId.setText(info.getBar());
			borrowNum.setText(info.getBorrowNum());
		}
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
}
