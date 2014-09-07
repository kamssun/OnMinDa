package com.newthread.android.ui.grade;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.GradeInfo;
import com.newthread.android.util.StringUtils;

public class GradeCalculate extends SherlockFragmentActivity {
	private ArrayList<GradeInfo> list;
	private String calculateType;
	private TextView result, courseNum;
	private String resultStr, courseNumStr;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_grade_calculate);

		initData();
		initView();
	}

	// 初始化数据
	private void initData() {
		calculateType = this.getIntent().getStringExtra("calculate_type");
		list = (ArrayList<GradeInfo>) getIntent().getBundleExtra("grades")
				.getSerializable("grade_list");
	}

	// 初始化界面
	private void initView() {
		ActionBar ab = this.getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("计算结果");

		result = (TextView) this.findViewById(R.id.result);
		courseNum = (TextView) this.findViewById(R.id.course_num);


		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isSelected()) {
			}
		}

		performCalculate();

		bingindData();
	}

	// 绑定数据
	private void bingindData() {
		result.setText(resultStr);
		courseNum.setText(courseNumStr);
	}

	// 计算过程
	public void performCalculate() {
		if (StringUtils.isEmpty(calculateType)) {
			return;
		}

		if (calculateType.equals("GPA")) {
			// 绩点
			CalculateGPA();
		} else if (calculateType.equals("weighting_average")) {
			// 加权平均分
			calculateWeightingAver();
		} else if (calculateType.equals("average")) {
			// 平均分
			calculateAver();
		} else {

		}
	}

	// 绩点计算
	// ∑学科所修课程学分×相应课程绩点／学年所得课程学分之和
	private void CalculateGPA() {
		float sum = 0;
		int count = 0;
		float CreditSum = 0;
		for (int i = 0; i < list.size(); i++) {
			try {
				sum = sum + list.get(i).getCourseCredit()
						* calculateGPA(list.get(i).getCourseGrade());
				CreditSum += list.get(i).getCourseCredit();
				count++;
			} catch (NumberFormatException e) {
				continue;
			}
		}

		Log.v("sum", sum + "");
		Log.v("creditsum", CreditSum + "");

		resultStr = ((float) sum / CreditSum) + "";
		courseNumStr = count + "";

		Log.v("绩点", resultStr);
	}

	// 计算单科绩点
	private float calculateGPA(String gradeStr) {
		float gpa = (float) 0.0;

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

	// 加权计算
	// 每门功课成绩乘以该门学分 相加的总和，然后除以总的学分多少
	private void calculateWeightingAver() {
		float sum = 0;
		int count = 0;
		float CreditSum = 0;
		for (int i = 0; i < list.size(); i++) {
			try {
				sum = sum + Float.parseFloat(list.get(i).getCourseGrade())
						* list.get(i).getCourseCredit();
				CreditSum += list.get(i).getCourseCredit();
				count++;

			} catch (NumberFormatException e) {
				continue;
			}
		}

		Log.v("sum", sum + "");
		Log.v("CreditSum", CreditSum + "");

		resultStr = ((float) sum / CreditSum) + "";
		courseNumStr = count + "";

		Log.v("加权", resultStr);
	}

	// 平均分计算
	private void calculateAver() {
		float sum = 0;
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			try {
				sum += Float.parseFloat(list.get(i).getCourseGrade());
				count++;
			} catch (NumberFormatException e) {
				continue;
			}
		}

		resultStr = ((float) sum / count) + "";
		courseNumStr = count + "";

		Log.v("平均分", resultStr);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
