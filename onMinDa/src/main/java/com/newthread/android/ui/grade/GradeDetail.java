package com.newthread.android.ui.grade;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.GradeInfo;
import com.newthread.android.global.URL;
import com.newthread.android.util.CroutonUtil;

public class GradeDetail extends SherlockFragmentActivity {
	private TextView courseName, courseID, courseGrade, courseCredit, 
					 courseGPA, courseDate, courseType, courseProperty, courseStudyType;
	public GradeInfo info;
	private String absTitle;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_grade_detail);
		
		initData();
		initView();
		bindingData();
	}

	//  绑定数据
	private void bindingData() {
		if (info != null) {
			courseName.setText(info.getCourseName());
			courseID.setText(info.getCourseID());        
			courseGrade.setText(info.getCourseGrade());
			courseCredit.setText(info.getCourseCredit() + "");
			courseGPA.setText(calculateGPA(info.getCourseGrade()) + "");
			courseDate.setText(info.getYear() + "第" + info.getSemesterNum() + "学期");
			courseType.setText(info.getCourseType());
			courseProperty.setText(info.getCourseProperty());
			courseStudyType.setText(info.getStudyType());
		}
	}

	// 初始化数据
	private void initData() {
		info = (GradeInfo) this.getIntent().getSerializableExtra("grade_info");
		absTitle = info.getCourseName();
	}

	// 初始化界面
	private void initView() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(absTitle);
		
		courseName = (TextView) this.findViewById(R.id.course_name);
		courseID = (TextView) this.findViewById(R.id.course_ID);
		courseGrade = (TextView) this.findViewById(R.id.course_grade);
		courseCredit = (TextView) this.findViewById(R.id.course_credit);
		courseGPA = (TextView) this.findViewById(R.id.course_GPA);
		courseDate = (TextView) this.findViewById(R.id.course_date);
		courseType = (TextView) this.findViewById(R.id.course_type);
		courseProperty = (TextView) this.findViewById(R.id.course_property);
		courseStudyType = (TextView) this.findViewById(R.id.course_study_type);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("分享").setIcon(R.drawable.ic_share).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("分享")) {
			startActivity(createShareIntent());
		}
		
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	// 计算绩点
	private float calculateGPA(String gradeStr) {
		float gpa = (float)0.0;
		
		try {
			Float grade = Float.parseFloat(gradeStr);
			
			grade -= 50;
			if (grade < 0) {
				gpa = 0;
			} else {
				gpa = (float) ((grade / 10) + ((grade - (grade / 10) * 10) * 0.1));
			}
		} catch (NumberFormatException e) {
		}
		
		return gpa;
	}
	
	// 分享
	public Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");   
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"分享");   
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		if (info != null) {
	        shareIntent.putExtra(Intent.EXTRA_TEXT, getShareText());    
		} else {
			shareIntent.putExtra(Intent.EXTRA_TEXT, "" + " from人在民大:" + URL.APK_DOWNLOAD);
		}
		return shareIntent;
    }
	
	// 分享文字
	private String getShareText() {
		String text = "";
		try {
			float grade = Float.parseFloat(info.getCourseGrade());
			if (grade > 80) {
				text = "我的『" + info.getCourseName() + "』居然考了" + grade + "分!羡慕嫉妒恨吧，哈哈哈哈哈~~~" + " from人在民大:" + URL.APK_DOWNLOAD; 
			} else {
				text = "我的『" + info.getCourseName() + "』居然只考了" + grade + "分!求安慰、求陪同、求人品.5555~~~" + " from人在民大:" + URL.APK_DOWNLOAD;
			}
		} catch (NumberFormatException e) {
			text = info.getCourseName() + ":" + info.getCourseGrade() + " from人在民大:" + URL.APK_DOWNLOAD;
		}
		return text;
	}
}
