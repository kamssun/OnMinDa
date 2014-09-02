package com.newthread.android.ui.coursechart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.SingleCourseInfo;
import com.newthread.android.manager.CourseRemindManger;
import com.newthread.android.util.StringUtils;

public class CourseDetailActivity extends SherlockFragmentActivity {
    private TextView courseName, teacher, place, time, duration;
    private SingleCourseInfo info;
    private Button remindButton;
    private int position;
    private int day;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_course_detail);
        initData();
        initView();
    }

    private void initData() {
        info = (SingleCourseInfo) this.getIntent().getSerializableExtra(
                "course_info");
        position = this.getIntent().getExtras().getInt("position");
        day = this.getIntent().getExtras().getInt("day");
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

        courseName = (TextView) this.findViewById(R.id.course_name);
        teacher = (TextView) this.findViewById(R.id.teacher);
        place = (TextView) this.findViewById(R.id.place);
        time = (TextView) this.findViewById(R.id.course_time);
        duration = (TextView) this.findViewById(R.id.duration_weeks);
        remindButton = (Button) this.findViewById(R.id.remindbutton);
        remindButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseRemindManger.getInstance(getApplicationContext()).openRemind(info);
                Toast.makeText(getApplicationContext(),"增加提醒成功",Toast.LENGTH_SHORT).show();
            }
        });
        if (info.getCourseName()==null||info.getCourseName().length()==0) {
            remindButton.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(info.getCourseName())) {
            courseName.setText(info.getCourseName());
            teacher.setText(info.getTeacherName());
            place.setText(info.getClassromNum());
            time.setText(info.getNumOfDay());
            duration.setText(info.getSustainTime());
        } else {
            courseName.setText("无");
            teacher.setText("无");
            place.setText("无");
            time.setText("无");
            duration.setText("无");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add("编辑").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
        if (item.getTitle().equals("编辑")) {
            Intent intent = new Intent(getApplicationContext(),
                    CourseDetialEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("day", day);
            bundle.putInt("position", position);
            intent.putExtras(bundle);
            intent.putExtra("course_info", info);
            CourseDetailActivity.this.startActivity(intent);
            CourseDetailActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
