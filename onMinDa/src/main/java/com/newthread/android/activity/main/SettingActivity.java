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
import com.newthread.android.util.AppManager;
import com.newthread.android.util.UpdateManager;

public class SettingActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initView();
	}

	// 初始化界面
	private void initView() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setHomeButtonEnabled(true);
		ab.setTitle("设置");

		// 获取当前版本号
		try {
			((TextView) this.findViewById(R.id.current_ver)).setText("当前版本:V"
					+ AppManager.getVersionName(getApplicationContext()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 检查更新
		this.findViewById(R.id.check_update).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// checkAppUpdate();
						// 检查更新
						new UpdateManager(SettingActivity.this).checkUpdate();
					}
				});

		// 问题反馈
		this.findViewById(R.id.feedback).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent _intent = new Intent(SettingActivity.this,
								FeedbackActivity.class);
						startActivity(_intent);
					}
				});

		// 关于应用
		this.findViewById(R.id.about).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent _intent = new Intent(SettingActivity.this,
						AboutActivity.class);
				startActivity(_intent);
			}
		});
	}

	// 检查更新
//	public void checkAppUpdate() {
//		AdManager.getInstance(this).asyncCheckAppUpdate(
//				new CheckAppUpdateCallBack() {
//
//					@Override
//					public void onCheckAppUpdateFinish(
//							AppUpdateInfo appUpdateInfo) {
//						if (appUpdateInfo == null) {
//							Toast.makeText(SettingActivity.this, "当前版本已经是最新版",
//									Toast.LENGTH_SHORT).show();
//						} else {
//							// 获取版本号
//							int versionCode = appUpdateInfo.getVersionCode();
//							// 获取版本
//							String versionName = appUpdateInfo.getVersionName();
//							// 获取新版本的信息
//							String updateTips = appUpdateInfo.getUpdateTips();
//							// 获取apk下载地址
//							final String downloadUrl = appUpdateInfo.getUrl();
//
//							AlertDialog updateDialog = new AlertDialog.Builder(
//									SettingActivity.this)
//									.setIcon(android.R.drawable.ic_dialog_info)
//									.setTitle("发现新版本 " + versionName)
//									.setMessage(updateTips)
//									.setPositiveButton(
//											"更新",
//											new DialogInterface.OnClickListener() {
//												@Override
//												public void onClick(
//														DialogInterface dialog,
//														int which) {
//													try {
//														Intent intent = Intent
//																.parseUri(
//																		downloadUrl,
//																		Intent.FLAG_ACTIVITY_NEW_TASK);
//														startActivity(intent);
//													} catch (Exception e) {
//														// exception
//													}
//												}
//											}).setNegativeButton("下次再说", null)
//									.create();
//							updateDialog.show();
//						}
//					}
//				});
//	}

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
