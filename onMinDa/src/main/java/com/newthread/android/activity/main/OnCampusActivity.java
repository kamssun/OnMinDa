package com.newthread.android.activity.main;

import java.util.ArrayList;
import java.util.List;

import android.view.KeyEvent;
import com.newthread.android.util.Loger;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.newthread.android.R;
import com.newthread.android.util.UpdateManager;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class OnCampusActivity extends SlidingFragmentActivity {
	private SlidingMenu sm;
	private Fragment mContent;
    private boolean isFirstSlideMenu = true;
	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	protected static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";
	public static final String API_KEY = "pZ8I3BqGrj3HAB7xR9FBSIaZ";	// 百度云推送API
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(savedInstanceState);
		
		// 检查更新
		new UpdateManager(OnCampusActivity.this).checkUpdate();
		
		// 消息推送初始化
		PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, API_KEY);
		List<String> list = new ArrayList<String>();
		list.add("V1.0");
		PushManager.setTags(getApplicationContext(), list);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		PushManager.activityStarted(this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// 如果要统计Push引起的用户使用应用情况，请实现本方法，且加上这一个语句
		setIntent(intent);
		
		handleIntent(intent);
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d("", ">=====onStop=====<");
		PushManager.activityStoped(this);
	}

	public void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_oncampus);
		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		// customize the SlidingMenuf
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
//		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.55f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setSlidingActionBarEnabled(true);
		
		// Main
		if (mContent == null)
			mContent = new MainContentFragment(getApplicationContext(), sm);
		getSupportFragmentManager().beginTransaction()
									.replace(R.id.on_campus_main_frame, mContent)
									.commit();

		// Left Menu
        final OnCampusLeftFragment leftFragment = new OnCampusLeftFragment(OnCampusActivity.this);
		setBehindContentView(R.layout.fragment_on_campus_left);
		getSupportFragmentManager().beginTransaction()
									.replace(R.id.on_campus_left_frame, leftFragment)
									.commit();
		
		// Right Menu
		sm.setSecondaryMenu(R.layout.fragment_on_campus_right);
		getSupportFragmentManager().beginTransaction()
									.replace(R.id.on_campus_right_frame, new OnCampusRightFragment(OnCampusActivity.this))
									.commit();
		// 显示菜单
		sm.showMenu(true);

        sm.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                if (isFirstSlideMenu) {
                    leftFragment.refreshLogo();
                    isFirstSlideMenu = false;
                }
            }
        });
	}
	
	// 切换主界面Fragment
	public void switchContent(final Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.on_campus_main_frame, fragment)
					.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	/**
	 * 处理Intent
	 * @param intent
	 */
	private void handleIntent(Intent intent) {
		String action = intent.getAction();

		if (ACTION_RESPONSE.equals(action)) {

			String method = intent.getStringExtra(RESPONSE_METHOD);

			if (PushConstants.METHOD_BIND.equals(method)) {
				Log.d("", "Handle bind response");
				String toastStr = "";
				int errorCode = intent.getIntExtra(RESPONSE_ERRCODE, 0);
				if (errorCode == 0) {
					String content = intent.getStringExtra(RESPONSE_CONTENT);
					String appid = "";
					String channelid = "";
					String userid = "";

					try {
						JSONObject jsonContent = new JSONObject(content);
						JSONObject params = jsonContent
								.getJSONObject("response_params");
						appid = params.getString("appid");
						channelid = params.getString("channel_id");
						userid = params.getString("user_id");
					} catch (JSONException e) {
						Log.e("", "Parse bind json infos error: " + e);
					}

					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(this);
					Editor editor = sp.edit();
					editor.putString("appid", appid);
					editor.putString("channel_id", channelid);
					editor.putString("user_id", userid);
					editor.commit();

					toastStr = "Bind Success";
				} else {
					toastStr = "Bind Fail, Error Code: " + errorCode;
					if (errorCode == 30607) {
						Log.d("Bind Fail", "update channel token-----!");
					}
				}

				Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
			}
		} else if (ACTION_LOGIN.equals(action)) {
//			String accessToken = intent.getStringExtra(EXTRA_ACCESS_TOKEN);
//			PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_ACCESS_TOKEN, accessToken);
		} else if (ACTION_MESSAGE.equals(action)) {
			String message = intent.getStringExtra(EXTRA_MESSAGE);
			String summary = "Receive message from server:\n\t";
			JSONObject contentJson = null;
			String contentStr = message;
			try {
				contentJson = new JSONObject(message);
				contentStr = contentJson.toString(4);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			summary += contentStr;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(summary);
			builder.setCancelable(true);
			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			Log.i("", "Activity normally start!");
		}
	}

    private boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), getString(R.string.exit_hint), Toast.LENGTH_SHORT).show();
            exitHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

            System.exit(0);
        }
    }

    final Handler exitHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
}
