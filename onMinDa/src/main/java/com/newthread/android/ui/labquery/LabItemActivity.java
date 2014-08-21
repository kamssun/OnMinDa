package com.newthread.android.ui.labquery;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;

public class LabItemActivity extends SherlockActivity {

	private LabDetail Item;
	private TextView LabName, weenNum, Week, TeacherName, Time, Location,
			OperationScore, ReportScore, TotalScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lab_item);
		Item = (LabDetail) getIntent().getSerializableExtra("data");
		Log.v("LabItemActivity", Item.getLabName());
		initview();
		initdata();
	}

	private void initdata() {
		LabName.setText(Item.getLabName());
		Week.setText(Item.getWeek());
		weenNum.setText("第" + Item.getWeekNum() + "周");
		TeacherName.setText(Item.getTeacherName());
		Time.setText(Item.getTime());
		String location = Item.getLabLocation();
		int i = location.indexOf("(地");
		Location.setText(location.substring(0, i));
		String Score = Item.getScore();
		String Score1 = Score.substring(Score.indexOf("/") + 1);
		OperationScore.setText(Score.subSequence(0, Score.indexOf("/")));
		ReportScore.setText(Score1.subSequence(0, Score1.indexOf("/")));
		TotalScore.setText(Score1.substring(Score1.indexOf("/") + 1));
	}

	private void initview() {
		LabName = (TextView) findViewById(R.id.Lab_name);
		Week = (TextView) findViewById(R.id.week);
		weenNum = (TextView) findViewById(R.id.WEEKNUM);
		TeacherName = (TextView) findViewById(R.id.teacherName);
		Time = (TextView) findViewById(R.id.time);
		Location = (TextView) findViewById(R.id.LabLocation);
		OperationScore = (TextView) findViewById(R.id.OperationScore);
		ReportScore = (TextView) findViewById(R.id.ReportScore);
		TotalScore = (TextView) findViewById(R.id.TotalScore);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("实验明细");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
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
