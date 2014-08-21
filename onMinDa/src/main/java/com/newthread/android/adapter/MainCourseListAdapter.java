package com.newthread.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newthread.android.R;
import com.newthread.android.R.color;
import com.newthread.android.bean.SingleCourseInfo;
import com.newthread.android.util.Logger;

public class MainCourseListAdapter extends BaseAdapter {
	private ArrayList<SingleCourseInfo> list;
	private LayoutInflater inflater;

	public MainCourseListAdapter(Context con,
			ArrayList<SingleCourseInfo> courseList) {
		this.inflater = LayoutInflater.from(con);
		this.list = courseList;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Logger.i("****", "getView" + position
				+ list.get(position).getCourseName());
		ViewHolder holder = null;
		SingleCourseInfo info = list.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.view_main_course_list_item,
					null);
			holder = new ViewHolder();
			holder.courseName = (TextView) convertView
					.findViewById(R.id.course_name);
			holder.teacher = (TextView) convertView.findViewById(R.id.teacher);
			holder.place = (TextView) convertView.findViewById(R.id.place);
			holder.courseTime = (TextView) convertView
					.findViewById(R.id.course_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (info.isHaveCourse()) {
			holder.courseName.setText(info.getCourseName());
			holder.place.setText(info.getClassromNum());
			holder.courseTime.setText(info.getNumOfDay());
		} else {
			holder.courseName.setText("无课");
			holder.place.setText("");
			int temp_int=(position+1)*2-1;
			 String temp="第"+temp_int+"-"+(temp_int+1)+"节";
			holder.courseTime.setText(temp);
		}
		return convertView;
		// SingleCourseInfo info = list.get(position);
		// if (info.isHaveCourse()) {
		// convertView = inflater.inflate(R.layout.view_main_course_list_item,
		// null);
		//
		// TextView courseName = (TextView) convertView
		// .findViewById(R.id.course_name);
		// // TextView teacher = (TextView) convertView
		// // .findViewById(R.id.teacher);
		// TextView place = (TextView) convertView.findViewById(R.id.place);
		// TextView courseTime = (TextView) convertView
		// .findViewById(R.id.course_time);
		//
		// courseName.setText(info.getCourseName());
		// place.setText(info.getClassromNum());
		// courseTime.setText(info.getNumOfDay());
		// } else {
		// convertView = inflater.inflate(
		// R.layout.view_main_course_list_item1, null);
		// }
		//
		// return convertView;
	}

	public class ViewHolder {
		private TextView courseName;
		private TextView teacher;
		private TextView place;
		private TextView courseTime;
	}

}
