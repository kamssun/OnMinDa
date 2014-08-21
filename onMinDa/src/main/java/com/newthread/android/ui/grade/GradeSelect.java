package com.newthread.android.ui.grade;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.GradeInfo;
import com.newthread.android.bean.GradeQueryVo;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.service.GradeQuery;
import com.newthread.android.ui.coursechart.CourseChartLoginActivity;
import com.newthread.android.ui.library.LibraryLoginActivity;
import com.newthread.android.util.CroutonUtil;
import com.newthread.android.util.Logger;
import com.newthread.android.util.MyPreferenceManager;
import com.newthread.android.util.StringUtils;

public class GradeSelect extends SherlockFragmentActivity implements OnClickListener {
	private View yearView, semesterView;
	private TextView yearText, semesterText;
	private ProgressDialog progressDialog;
	
	private ArrayList<GradeInfo> list;
	private GradeQueryVo vo;
	private String account, password;
	
	private int mYear;
	private int mMonth;
	private int mDay;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_grade_select);
		
		initData();
		initView();
	}
	
	@Override
	protected void onResume() {
		if (list != null && list.size() > 0) {
			list.clear();
		}		
		
		super.onResume();
	}
	
	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			progressDialog.cancel();
			
			switch (msg.what) {
			case HandleMessage.QUERY_SUCCESS:
				// 查询成功
				querySuccess();
				break;
			case HandleMessage.QUERY_ERROR:
				CroutonUtil.showAlertCrouton(GradeSelect.this, true, "网络错误", 0);
				break;
			default:
				break;
			}
			
			super.handleMessage(msg);
		}
	};
	
	// 初始化数据
	private void initData() {
		MyPreferenceManager.init(getApplicationContext());		
		account = MyPreferenceManager.getString("admin_system_account", "");
		password = MyPreferenceManager.getString("admin_system_password", "");
		
		list = new ArrayList<GradeInfo>();
		vo = new GradeQueryVo();
		
		// 初始化日期
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
	}
	
	// 查询成功
	private void querySuccess() {
		Intent _intent = new Intent(getApplicationContext(), GradeList.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("grade_list", list);
		_intent.putExtra("grades", bundle);
		_intent.putExtra("title", getABSTitle());
		startActivity(_intent);
	}
	
	// 查询过程
	private void performQuery() {
		// 判断是否选择
		if ( !isSelected() ) {
			CroutonUtil.showAlertCrouton(GradeSelect.this, true, "请选择要查询的学年", 0);
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在查询。。。");
		progressDialog.show();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int result = -1;
				
				vo.setAccount(account);
				vo.setPassword(password);
				result = new GradeQuery(list, vo).query();
				
				handler.sendEmptyMessage(result);
			}
		}).start();
	}

	// 初始化界面
	private void initView() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("成绩查询");
		
		if (needLogin()) {
			showLoginDialog();
		}
		
		yearView = (View) this.findViewById(R.id.year_view);
		semesterView = (View) this.findViewById(R.id.semester_view);

		yearText = (TextView) this.findViewById(R.id.year);
		semesterText = (TextView) this.findViewById(R.id.semester);
		
		yearText.setText("2012-2013");
		vo.setYear("2012-2013");
		
		yearView.setOnClickListener(this);
		semesterView.setOnClickListener(this);
	}
	
	// 登录对话框
	protected void showLoginDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("需要登录教务系统才能查询");
		builder.setTitle("提示");
		
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent _intent = new Intent(GradeSelect.this, CourseChartLoginActivity.class);
				startActivity(_intent);
				finish();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		builder.create().show();
	}
	
	// 判断是否完成必选项的选择
	private boolean isSelected() {
		if (StringUtils.isEmpty(yearText.getText().toString().trim())) {
			return false;
		}
		return true;
	}
	
	// 判断学期是否选择
	private boolean isSemesterSelected() {
		if (StringUtils.isEmpty(semesterText.getText().toString().trim()) 
				|| semesterText.getText().toString().trim().equals("可不选择")) {
			return false;
		}
		
		return true;
	}
	
	// 标题
	private  String getABSTitle() {
		String title = "";
		String semester = semesterText.getText().toString().trim();
		if (isSemesterSelected()) {
			title = yearText.getText().toString().trim() + "(" + semester + ")";
		} else {
			title += yearText.getText().toString().trim();
		}
		
		return title;
	}

	// 控件监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.year_view:
				// 选择年份
				selectYear();
				break;
			case R.id.semester_view:
				// 选择学期
				selectSemester();
				break;
			default:
				break;
		}
	}

	// 学期选择
	private void selectSemester() {
		new AlertDialog.Builder(GradeSelect.this)
		.setTitle(R.string.semester_select)
		.setSingleChoiceItems(R.array.grade_semesters, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						dialog.cancel();
						
						String [] semesters = getResources().getStringArray(R.array.grade_semesters);
						semesterText.setText(semesters[whichButton]);
						
						Logger.i("*************", "whichButton " + whichButton);
						if (whichButton == 1) {
							vo.setSemester("1");
						} else if (whichButton == 2) {
							vo.setSemester("2");
						} else {
							vo.setSemester("");
						}
					}
				}).create().show();
	}

	// 年份选择
	private void selectYear() {
		new AlertDialog.Builder(GradeSelect.this)
		.setTitle(R.string.year_select)
		.setSingleChoiceItems(R.array.grade_years, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						dialog.cancel();
						
						String [] years = getResources().getStringArray(R.array.grade_years);
						yearText.setText(years[whichButton]);
						
						vo.setYear(years[whichButton].replace("~", "-"));
					}
				}).create().show();
	}
	
	// 判断是否需要登录
	private boolean needLogin() {
		if (StringUtils.isEmpty(account) 
			| StringUtils.isEmpty(password)
			| MyPreferenceManager.getBoolean("admin_system_isFirstLogin", true)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("完成").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("完成")) {
			performQuery();
		}

		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
