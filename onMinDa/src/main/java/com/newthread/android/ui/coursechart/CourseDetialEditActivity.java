package com.newthread.android.ui.coursechart;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.EverydayCourse;
import com.newthread.android.bean.SingleCourseInfo;
import com.newthread.android.util.AndroidDB;
import com.newthread.android.util.StringUtils;

public class CourseDetialEditActivity extends SherlockFragmentActivity {
	private EditText courseName, teacher, place, time1, time2, duration1,
			duration2;
	private SingleCourseInfo info;
	private int position, day;
	private ArrayList<EverydayCourse> weekCourse;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_course_detail_edit);
		initData();
		initView();
	}

	private void initData() {
		info = (SingleCourseInfo) this.getIntent().getSerializableExtra(
				"course_info");
		position = this.getIntent().getExtras().getInt("position");
		day = this.getIntent().getExtras().getInt("day");
		weekCourse = AndroidDB.getCourse(getApplicationContext());
	}

	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);

		if (StringUtils.isEmpty(info.getCourseName())) {
			ab.setTitle("课程详细");
		} else {
			ab.setTitle(info.getCourseName());
		}

		courseName = (EditText) this.findViewById(R.id.course_edit_name);
		teacher = (EditText) this.findViewById(R.id.edit_teacher);
		place = (EditText) this.findViewById(R.id.edit_place);
		time1 = (EditText) this
				.findViewById(R.id.edit_course_detial_time_start);
		time2 = (EditText) this.findViewById(R.id.edit_course_detial_time_end);
		duration1 = (EditText) this
				.findViewById(R.id.edit_duration_weeks_start);
		duration2 = (EditText) this
				.findViewById(R.id.edit_duration_weeks_start_end);
		if (!StringUtils.isEmpty(info.getCourseName())) {
			courseName.setText(info.getCourseName());
			teacher.setText(info.getTeacherName());
			place.setText(info.getClassromNum());
			// time.setText(info.getNumOfDay());
			// duration.setText(info.getSustainTime());
		} else {
			courseName.setHint("必填");
			teacher.setText("无");
			place.setText("无");
			// time.setText("无");
			// duration.setHint("必填（如1-18周）");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("确定").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}
		if (item.getTitle().equals("确定")) {
			SingleCourseInfo sci = new SingleCourseInfo();
			String str = "无";
			if (courseName.getText().toString().equals(str)
					|| StringUtils.isEmpty(courseName.getText().toString())) {
				sci.setHaveCourse(false);
				Toast.makeText(getApplicationContext(),
						"该课程信息被清空,若有课请重新填写完整信息或登录教务系统", Toast.LENGTH_LONG)
						.show();
				sci.setClassromNum("");
				sci.setCourseName("");
				sci.setNumOfDay("");
				sci.setSustainTime("");
				sci.setTeacherName("");
				weekCourse.get(day).getDayOfWeek().set(position, sci);
				AndroidDB.addCourse(getApplicationContext(), weekCourse);
				CourseDetialEditActivity.this.finish();
			} else {
				sci.setHaveCourse(true);
				sci.setClassromNum(place.getText().toString());
				sci.setCourseName(courseName.getText().toString());
				sci.setNumOfDay("第"+time1.getText().toString()+"-"+time2.getText().toString()+"节");
				sci.setSustainTime(duration1.getText().toString()+"-"+duration2.getText().toString()+"周");
				sci.setTeacherName(teacher.getText().toString());
				weekCourse.get(day).getDayOfWeek().set(position, sci);
				AndroidDB.addCourse(getApplicationContext(), weekCourse);
				CourseDetialEditActivity.this.finish();
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
