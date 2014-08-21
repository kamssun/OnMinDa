package com.newthread.android.ui.coursechart;

import java.util.ArrayList;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.EverydayCourse;
import com.newthread.android.util.AndroidDB;
import com.newthread.android.util.TimeUtil;

import static com.newthread.android.ui.coursechart.BookStore_Top.OnRefreshViewListener;

public class CourseChartActivity extends SherlockFragmentActivity {
	private ArrayList<EverydayCourse> weekCourse;
    private int numOfWeek;

	private LinearLayout bookstore_container;
	private BookStore_Top viewTop;
    private TextView detailContent;
	private Context ctx;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_chart);

		initData();
		initView();
	}

	private void initData() {
		weekCourse = AndroidDB.getCourse(getApplicationContext());
        numOfWeek = TimeUtil.getDayOfWeek();
	}

	// 初始化界面
	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("课程表");

		ctx = CourseChartActivity.this;
        detailContent = (TextView) this.findViewById(R.id.detail_content);
		bookstore_container = (LinearLayout) findViewById(R.id.bookstore_container);
		bookstore_container.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);

		// 获取登录后数据
//		WeekCourseInfo week = (WeekCourseInfo) this.getIntent().getSerializableExtra("course_chart");
//		if (week != null) {
//			weekCourse = week.getWeekCourse();
//		}

        if (viewTop == null) {
            viewTop = new BookStore_Top(ctx, weekCourse, refreshViewListener);
        }
        bookstore_container.addView(viewTop, params);
		viewTop.DataBind(TimeUtil.getDayOfWeek());

        detailContent.setText("课程详细 (周" + numOfWeek + ")");

        View bottomView = this.findViewById(R.id.bottom_view);
        bottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewTop.isLeftExtended()) {
                    viewTop.showCenter();
                } else {
                    viewTop.showLeft();
                }
            }
        });
	}

    OnRefreshViewListener refreshViewListener = new OnRefreshViewListener() {

        @Override
        public void onRefresh(int index) {
            if (viewTop.isLeftExtended()) {
                detailContent.setText("收起");
            } else {
//                if (index == 6) {
//                }
                detailContent.setText("课程详细 (周" + (index + 1) + ")");
            }
        }
    };

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
}
