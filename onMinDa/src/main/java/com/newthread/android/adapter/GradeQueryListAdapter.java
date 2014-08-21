package com.newthread.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.newthread.android.R;
import com.newthread.android.bean.GradeInfo;
import com.newthread.android.bean.GradeListAdapterVo;

public class GradeQueryListAdapter extends BaseAdapter {
	private ArrayList<GradeInfo> list;
	// 用来控制CheckBox的选中状况
	public static HashMap<Integer, Boolean> isSelected;
	private Context context;
	private LayoutInflater inflater = null;
	private GradeListAdapterVo vo;

	// 构造器
	@SuppressLint("UseSparseArrays")
	public GradeQueryListAdapter(ArrayList<GradeInfo> list, Context context) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		// 初始化数据
		initDate();
	}
	
	public GradeQueryListAdapter(ArrayList<GradeInfo> list, Context context,  GradeListAdapterVo vo) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.vo = vo;
		// 初始化数据
		initDate();
	}

	// 初始化isSelected的数据
	private void initDate() {
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, true);
		}
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
			holder = new ViewHolder(); 
			convertView = inflater.inflate(R.layout.view_grade_list_item, null);
			
			holder.serial_num = (TextView) convertView.findViewById(R.id.serial_num);
			holder.course_name = (TextView) convertView.findViewById(R.id.course_name);
			holder.course_grade = (TextView) convertView.findViewById(R.id.course_grade);
			holder.subMenu = (ImageView) convertView.findViewById(R.id.sub_menu);
			
			holder.CKMultipleChoice = (CheckBox) convertView.findViewById(R.id.checkbox);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		GradeInfo item = list.get(position);
		
		holder.serial_num.setText((position + 1) + ". ");
		holder.course_name.setText(item.getCourseName());
		holder.course_grade.setText("(" + item.getCourseGrade() + "分)");
		
//		try {
//			if (Float.parseFloat(item.getCourseGrade()) < 60.0) {
//				holder.course_grade.setTextColor(context.getResources().getColor(R.color.red));
//			}
//		} catch (NumberFormatException e) {
//			holder.course_grade.setText("(" + item.getCourseGrade() + ")");
//		}
		
		if (vo.isMultipleSelect()) {
			holder.CKMultipleChoice.setVisibility(View.VISIBLE);
			holder.CKMultipleChoice.setChecked(getIsSelected().get(position));
			
			holder.subMenu.setVisibility(View.GONE);
		} else {
			holder.CKMultipleChoice.setVisibility(View.GONE);
			
			holder.subMenu.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		GradeQueryListAdapter.isSelected = isSelected;
	}

	public static class ViewHolder {
		TextView serial_num;
		TextView course_name;
		TextView course_grade;
		public CheckBox CKMultipleChoice;
		ImageView subMenu;
	}

}
