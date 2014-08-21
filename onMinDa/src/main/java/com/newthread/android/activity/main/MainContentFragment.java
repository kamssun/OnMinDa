package com.newthread.android.activity.main;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.newthread.android.R;
import com.newthread.android.adapter.MainCourseListAdapter;
import com.newthread.android.bean.EverydayCourse;
import com.newthread.android.bean.SingleCourseInfo;
import com.newthread.android.ui.coursechart.CourseDetailActivity;
import com.newthread.android.util.AndroidDB;
import com.newthread.android.util.AndroidUtil;
import com.newthread.android.util.TimeUtil;
import com.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class MainContentFragment extends Fragment {
	private RelativeLayout relayout;
	private View leftButton, rightButton;
	private SlidingMenu sm;
	private Context con;
	private TextView week_of_semester, todayTitle, tomorrowTitle,
			todayNoCoursePrompt, tomorrowNoCoursePrompt;
	private ArrayList<EverydayCourse> weekCourse;
	private MainCourseListAdapter todayAdapter, tomorrowAdapter;
	private ArrayList<SingleCourseInfo> todayShowList = new ArrayList<SingleCourseInfo>(),
			tomorrowShowList = new ArrayList<SingleCourseInfo>();
	private ListView todayCourseList;
	private ListView tomorrowCourseList;
	private LinearLayout todayView, tomorrowView;
	int src[] = new int[] { R.drawable.main_titlebar_bg, R.drawable.main_titlebar_bg_2,
			R.drawable.main_titlebar_bg_3, R.drawable.main_titlebar_bg_4,
			R.drawable.main_titlebar_bg_5 };

	public MainContentFragment() {
		super();
	}

	public MainContentFragment(Context con, SlidingMenu sm) {
		super();
		this.con = con;
		this.sm = sm;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_content, container, false);

		initData();
		initView(view);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		todayShowList.clear();
		tomorrowShowList.clear();
		weekCourse = AndroidDB.getCourse(con);
		refreshData();
		setListViewHight();
		if (todayShowList.size() != 0) {
			todayCourseList.setVisibility(View.VISIBLE);
			todayNoCoursePrompt.setVisibility(View.GONE);
		}
		if (tomorrowShowList.size() != 0) {
			tomorrowCourseList.setVisibility(View.VISIBLE);
			tomorrowNoCoursePrompt.setVisibility(View.GONE);
		}
		todayAdapter = new MainCourseListAdapter(con, todayShowList);
		tomorrowAdapter = new MainCourseListAdapter(con, tomorrowShowList);
		todayAdapter.notifyDataSetChanged();
		tomorrowAdapter.notifyDataSetChanged();
		todayCourseList.setAdapter(todayAdapter);
		tomorrowCourseList.setAdapter(tomorrowAdapter);
	}

	private void initData() {
		weekCourse = AndroidDB.getCourse(con);
	}

	private int changephoto = 1;

	// 初始化界面
	private void initView(View view) {
		relayout = (RelativeLayout) view
				.findViewById(R.id.view_title_bar_relativelayout);
//		relayout.setBackgroundResource(src[(int) Math.random() * src.length]);

//		relayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				relayout.setBackgroundResource(src[changephoto % src.length]);
//				changephoto++;
//			}
//		});
		leftButton = view.findViewById(R.id.logo_and_title);
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sm.showMenu();
			}
		});

		rightButton = view.findViewById(R.id.main_person);
		rightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sm.showSecondaryMenu();
			}
		});

		int dayOfWeek = TimeUtil.getDayOfWeek();
		refreshData();
		todayNoCoursePrompt = (TextView) view
				.findViewById(R.id.today_no_course);
		tomorrowNoCoursePrompt = (TextView) view
				.findViewById(R.id.tomorrow_no_course);

		todayCourseList = (ListView) view.findViewById(R.id.today_course_list);
		tomorrowCourseList = (ListView) view
				.findViewById(R.id.tomorrow_course_list);

		todayAdapter = new MainCourseListAdapter(con, todayShowList);
		tomorrowAdapter = new MainCourseListAdapter(con, tomorrowShowList);

		todayCourseList.setAdapter(todayAdapter);
		todayCourseList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent _intent = new Intent(con, CourseDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("day", TimeUtil.getDayOfWeek());
				bundle.putInt("position", arg2);
				_intent.putExtras(bundle);
				_intent.putExtra("course_info", todayShowList.get(arg2));
				MainContentFragment.this.startActivity(_intent);
			}
		});
		tomorrowCourseList.setAdapter(tomorrowAdapter);
		tomorrowCourseList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent _intent = new Intent(con, CourseDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("day", TimeUtil.getDayOfWeek() + 1);
				bundle.putInt("position", arg2);
				_intent.putExtras(bundle);
				_intent.putExtra("course_info", tomorrowShowList.get(arg2));
				MainContentFragment.this.startActivity(_intent);
			}
		});
		week_of_semester = (TextView) view.findViewById(R.id.week_of_semester);

		int week = TimeUtil.getWeekOfSemester();

		week_of_semester.setText("第" + week + "周");
        YoYo.with(Techniques.BounceInRight)
                .duration(1600)
                .playOn(week_of_semester);

		todayTitle = (TextView) view.findViewById(R.id.today_title);
		tomorrowTitle = (TextView) view.findViewById(R.id.tomoroow_title);
		todayTitle.setText("今天(周" + getStrDayOfWeek(dayOfWeek) + ")");
		tomorrowTitle.setText("明天(周" + getStrDayOfWeek((dayOfWeek + 1) % 7)
				+ ")");
		todayView = (LinearLayout) view.findViewById(R.id.today_view);
		tomorrowView = (LinearLayout) view.findViewById(R.id.tomorrow_view);
		setListViewHight();

		// 如果今天没有课
		if (todayShowList.size() == 0) {
			todayCourseList.setVisibility(View.GONE);
			todayNoCoursePrompt.setVisibility(View.VISIBLE);
			todayNoCoursePrompt.setText("今天没有课..");
		}

		// 如果明天没有课
		if (tomorrowShowList.size() == 0) {
			tomorrowCourseList.setVisibility(View.GONE);
			tomorrowNoCoursePrompt.setVisibility(View.VISIBLE);
			tomorrowNoCoursePrompt.setText("明天没有课..");
		}
	}

	private void setListViewHight() {

		// 今天课程列表高度

		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) todayView
				.getLayoutParams(); // 取控件mGrid当前的布局参数
		int height = AndroidUtil.dp2px(con, 75 * todayShowList.size());
		if (todayShowList.size() == 0) {
			height = 140;
		}
		linearParams.height = height;
		todayView.setLayoutParams(linearParams);

		// 明天课程列表高度

		LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) tomorrowView
				.getLayoutParams(); // 取控件mGrid当前的布局参数
		int height2 = AndroidUtil.dp2px(con, 75 * tomorrowShowList.size());
		if (tomorrowShowList.size() == 0) {
			height2 = 140;
		}
		linearParams2.height = height2;
		tomorrowView.setLayoutParams(linearParams2);
	}

//	private int sizeOfHaveCourse(ArrayList<SingleCourseInfo> todayShowList2) {
//		int num = 0;
//		for (int i = 0; i < todayShowList2.size(); i++) {
//			if (todayShowList2.get(i).isHaveCourse()) {
//				num++;
//			}
//		}
//		return num;
//	}

	// 刷新数据
	private void refreshData() {
		int dayOfWeek = TimeUtil.getDayOfWeek();
		// 今天的课程
		ArrayList<SingleCourseInfo> todayList = weekCourse.get(dayOfWeek)
				.getDayOfWeek();

		for (int i = 0; i < todayList.size(); i++) {
			// Log.v("000000", i + "   " + todayList.get(i).getCourseName()
			// + isProceed(todayList.get(i)));
			if (todayList.get(i).isHaveCourse()) {
				if (!isProceed(todayList.get(i))) {
					SingleCourseInfo sci = new SingleCourseInfo();
					sci.setClassromNum("");
					sci.setCourseName("");
					sci.setHaveCourse(false);
					sci.setTeacherName("");
					todayShowList.add(sci);
				} else {
					todayShowList.add(todayList.get(i));
				}
			} else {
				todayShowList.add(todayList.get(i));
			}
		}
		// 明天的课程
		ArrayList<SingleCourseInfo> tomorrowList = weekCourse.get(
				(dayOfWeek + 1) % 7).getDayOfWeek();

		for (int i = 0; i < tomorrowList.size(); i++) {
			// Log.v("000000", i + "   " + tomorrowList.get(i).getCourseName()
			// + isProceed(tomorrowList.get(i)));

			if (tomorrowList.get(i).isHaveCourse()) {
				if (!isProceed(tomorrowList.get(i))) {
					SingleCourseInfo sci = new SingleCourseInfo();
					sci.setClassromNum("");
					sci.setCourseName("");
					sci.setHaveCourse(false);
					sci.setTeacherName("");
					sci.setSustainTime("");
					tomorrowShowList.add(sci);
				} else {
					tomorrowShowList.add(tomorrowList.get(i));
				}
			} else {
				tomorrowShowList.add(tomorrowList.get(i));
			}
		}
	}

	// 判断是否在上课周期内
	private boolean isProceed(SingleCourseInfo info) {
		if (info.getCourseName() == "无" || info.getCourseName() == ""
				|| info.getCourseName() == null) {
			return false;
		}

		int startTime = 0;
		int endTime = 0;
		String duration = info.getSustainTime().trim();
		duration = duration.substring(0, duration.indexOf("周"));
		if (duration.split("-").length == 2) {
			startTime = Integer.parseInt(duration.split("-")[0]);
			endTime = Integer.parseInt(duration.split("-")[1]);

		} else {
			return true;
		}
		if (startTime <= TimeUtil.getWeekOfSemester()
				&& TimeUtil.getWeekOfSemester() <= endTime) {
			return true;
		}

		return false;
	}

	private String getStrDayOfWeek(int dayOfWeek2) {
		int dayOfWeek = dayOfWeek2;

		if (dayOfWeek == 0) {
			return "一";
		} else if (dayOfWeek == 1) {
			return "二";
		} else if (dayOfWeek == 2) {
			return "三";
		} else if (dayOfWeek == 3) {
			return "四";
		} else if (dayOfWeek == 4) {
			return "五";
		} else if (dayOfWeek == 5) {
			return "六";
		} else {
			return "日";
		}
	}
}
