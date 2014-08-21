package com.newthread.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newthread.android.R;
import com.newthread.android.bean.CourseItem;

public class CardCourseAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CourseItem> list;

	public CardCourseAdapter(Context con, List<CourseItem> list) {
		this.inflater = LayoutInflater.from(con);
		this.list = list;
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
		ViewHolder holder = null;
		
		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.view_card_course_item, null);
			holder = new ViewHolder();
			
			holder.numOfDay = (TextView) convertView.findViewById(R.id.num_of_day);
			holder.courseName = (TextView) convertView.findViewById(R.id.course_name);
			holder.place = (TextView) convertView.findViewById(R.id.place);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.numOfDay.setText(list.get(position).getNumOfDay());
		holder.courseName.setText(list.get(position).getCourseName());
		holder.place.setText(list.get(position).getPlace());

		System.out.println("----------------------------" + position);
		
		return convertView;
	}

	public class ViewHolder {
		private TextView numOfDay;
		private TextView courseName;
		private TextView place;
	}
}
