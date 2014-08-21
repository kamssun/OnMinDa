package com.newthread.android.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;

public class MyLibraryActivity extends SherlockActivity {
	private View personalInfo, bookCollect, currentBorrow;
	private CheckBox library_notify;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_library);

		initView();
	}

	// 初始化界面
	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setTitle("我的图书馆");
		
        personalInfo = (View) this.findViewById(R.id.personal_info);
		currentBorrow = (View) this.findViewById(R.id.current_borrow);
		bookCollect = (View) this.findViewById(R.id.book_collect);
		
		personalInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent _intent = new Intent(MyLibraryActivity.this, PersonalInfoActivity.class);
				startActivity(_intent);
			}
		});

		currentBorrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent _intent = new Intent(MyLibraryActivity.this, LibraryCurrentBorrowActivity.class);
				startActivity(_intent);
			}
		});

		bookCollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent _intent = new Intent(MyLibraryActivity.this, LibraryCollectActivity.class);
				startActivity(_intent);
			}
		});
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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
