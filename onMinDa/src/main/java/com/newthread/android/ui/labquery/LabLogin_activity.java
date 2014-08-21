package com.newthread.android.ui.labquery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.util.MyPreferenceManager;

public class LabLogin_activity extends SherlockActivity {

	private EditText mAccount, mPassword;
	private ProgressDialog dialog;
	public static Context mcontext;
	public static SharedPreferences sp;// SharedPreferences用来保存账号密码和登陆的状态
	private CheckBox checkbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lablogin);
		if (!isConnectNET()) {
			Toast.makeText(this, "无网络连接，请检查...", 20 * 1000).show();
		}
		if (this.getIntent().getBooleanExtra("is_direct_login", true)) {
			MyPreferenceManager.init(getApplicationContext());
			if (!MyPreferenceManager.getBoolean("LabQuery_isFirstLogin", true)) {
				// 已登录成功
				Intent _intent = new Intent(this, LabDetail_activity.class);
				startActivity(_intent);
				this.finish();
			}
		}
		mcontext = getApplicationContext();
		initview();
	}

	//检查是否连接了网络
	private Boolean isConnectNET() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;

	}

	private void initview() {
		mAccount = (EditText) findViewById(R.id.login_account);
		mPassword = (EditText) findViewById(R.id.login_password);

		MyPreferenceManager.init(getApplicationContext());
		if (!MyPreferenceManager.getBoolean("LabQuery_isFirstLogin", true)) {
			mAccount.setText(MyPreferenceManager.getString("LabQuery_account",
					"").trim());
			mPassword.setText(MyPreferenceManager.getString(
					"LabQuery_password", "").trim());
		}

		this.findViewById(R.id.login_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						performLogin();
					}
				});
		checkbox = (CheckBox) findViewById(R.id.login_checkbox);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("实验查询登录");

	}

	// 登陆过程
	private void performLogin() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("Wait...");
		dialog.show();
		RequestManager.getInstance().startRequest(
				new OnRequestResponseListener() {

					@Override
					public void onRequestSuccess(long requestId, String result) {
						dialog.cancel();
						RequestSuccess();
					}

					@Override
					public void onRequestNetError(long requestId, String result) {
						super.onRequestNetError(requestId, result);
						dialog.cancel();
					}

					@Override
					public void onRequestApiError(long requestId, String result) {
						dialog.cancel();
						handler.sendEmptyMessage(123);
					}

				},
				new LoginRequestApi(mAccount.getText().toString().trim(),
						mPassword.getText().toString().trim()));
	}

	//登陆成功
	private void RequestSuccess() {

		if (checkbox.isChecked()) {
			// 保存信息
			MyPreferenceManager.commitBoolean("LabQuery_isFirstLogin", false);
			MyPreferenceManager.commitString("LabQuery_account", mAccount
					.getText().toString());
			MyPreferenceManager.commitString("LabQuery_password", mPassword
					.getText().toString());
		}
		Intent intent = new Intent(this, LabDetail_activity.class);
		startActivity(intent);
		this.finish();
	}

	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 123:
				Toast.makeText(LabLogin_activity.this, "账号或密码错误，请重新输入",
						3 * 1000).show();
				break;

			default:
				break;
			}
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		return super.onMenuItemSelected(featureId, item);
	}

}
