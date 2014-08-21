package com.newthread.android.activity.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.newthread.android.R;
import com.newthread.android.util.TimeUtil;

public class SplashActivity extends Activity {
	private static final int DALAYTIME = 2000;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        int week = TimeUtil.getWeekOfSemester();
        TextView weekTV = (TextView) this.findViewById(R.id.splash_text);
        weekTV.setText("本学期第" + week + "周");

        YoYo.with(Techniques.BounceInRight).duration(2000).playOn(weekTV);

        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent _intent = new Intent(getApplication(), OnCampusActivity.class);
				startActivity(_intent);
				SplashActivity.this.finish();
			}
		}, DALAYTIME);
    }
	
	// 得到今天是这学期的第几周
	public static int getWeekOfSemester(int sYear, int sMonth, int sDay,
			int cYear, int cMonth, int cDay) {
		int weeks = 0;
		if (sYear == cYear) {
			// 开学年份与今天在同一年
			int days = getDayOfYear(cYear, cMonth, cDay)
					- getDayOfYear(sYear, sMonth, sDay);
			int dayOfWeek = getDayOfWeek(sYear, sMonth, sDay);
			int days0 = days - dayOfWeek;

			if (days % 7 != 0)
				weeks++;

			weeks += days0 / 7 + 1;

		} else {

		}

		return weeks;
	}
	
	// 得到今天是一年中的第几天
	public static int getDayOfYear(int year, int month, int day) {
		int sum_days = 0;
		// 计算天数 巧用switch语句
		switch (month - 1) {
		case 11:
			sum_days += 30;
		case 10:
			sum_days += 31;
		case 9:
			sum_days += 30;
		case 8:
			sum_days += 31;
		case 7:
			sum_days += 31;
		case 6:
			sum_days += 30;
		case 5:
			sum_days += 31;
		case 4:
			sum_days += 30;
		case 3:
			sum_days += 31;
		case 2:
			sum_days += 28;
		case 1:
			sum_days += 31;
			break;
		default:
			break;
		}

		// 判断是否是闰年 闰年2月29天
		if ((year % 4 == 0) && (year % 100 != 0) || year % 400 == 0)
			if (month > 2)
				sum_days++;

		sum_days += day;
		return sum_days;
	}
	
	// 得到今天是周几
	public static int getDayOfWeek(int year, int month, int day) {
		if (month == 1) {
			month = 13;
			year--;
		}
		if (month == 2) {
			month = 14;
			year--;
		}
		return (day + 2 * month + 3 * (month + 1) / 5 + year + year / 4 - year
				/ 100 + year / 400) % 7;
	}
}
