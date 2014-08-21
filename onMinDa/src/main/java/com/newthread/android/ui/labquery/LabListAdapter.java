package com.newthread.android.ui.labquery;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newthread.android.R;
import com.newthread.android.util.TimeUtil;

public class LabListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<LabDetail> list;
	private Context context;

	public LabListAdapter(Context context, ArrayList<LabDetail> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void setList(ArrayList<LabDetail> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		// 如果缓存convertView为空，则需要创建View
		if (convertView == null) {
			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			convertView = mInflater.inflate(R.layout.lablist_item, null);
			holder.weekNum = (TextView) convertView.findViewById(R.id.WeekNum);
			holder.LabName = (TextView) convertView.findViewById(R.id.LabName);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.weekNum.setText(list.get(position).getWeekNum() + "周");
		holder.LabName.setText(list.get(position).getLabName() + "");
		Log.v("LabListAdapter", list.get(position).getLabLocation());
		Log.v("LabListAdapter", list.get(position).getWeekNum());
		Log.v("LabListAdapter", list.get(position).getLabName());

		if (Integer.valueOf(list.get(position).getWeekNum()) < TimeUtil
				.getWeekOfSemester()) {
			holder.weekNum.setTextColor(Color.RED);
		}
		return convertView;
	}

	class ViewHolder {
		public TextView weekNum;
		public TextView LabName;
		public TextView LabLocation;
	}

}
