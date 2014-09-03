package com.newthread.android.ui.exam;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.activity.main.MyApplication;
import com.newthread.android.bean.ExamArrangeInfo;

/**
 * Created by jindongping on 14-9-3.
 */
public class ExamArrangeDetailActivity extends SherlockFragmentActivity {
    private ExamArrangeInfo examArrangeInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        examArrangeInfo = MyApplication.getInstance().getThing("examArrangeInfo");
        setContentView(R.layout.activity_exam_detail);
        inItView();
    }

    private void inItView() {
        TextView course_name = (TextView) findViewById(R.id.course_name);
        course_name.setText(examArrangeInfo.getCourseName());
        TextView courseType = (TextView)findViewById(R.id.courseType);
        courseType.setText(examArrangeInfo.getCourseType());
        TextView teacher = (TextView)findViewById(R.id.teacher);
        teacher.setText(examArrangeInfo.getCourseTeacher());
        TextView  course_time = (TextView)findViewById(R.id.course_time);
        course_time.setText(examArrangeInfo.getExamTime());
        TextView  place = (TextView)findViewById(R.id.place);
        place.setText(examArrangeInfo.getExamAddress());
        TextView  seatNum = (TextView)findViewById(R.id.seatNum);
        seatNum.setText(examArrangeInfo.getSeatNum());
        TextView  courseGrade = (TextView)findViewById(R.id.courseGrade);
        courseGrade.setText(examArrangeInfo.getCourseGrade());
        TextView  examType = (TextView)findViewById(R.id.examType);
        examType.setText(examArrangeInfo.getExamType());
        TextView finishSate = (TextView)findViewById(R.id.finishSate);
        finishSate.setText(examArrangeInfo.getFinishSate());

    }

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

    // 对返回键进行监听
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.finish();
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
