package com.newthread.android.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.newthread.android.R;
import com.newthread.android.ui.exam.ExamArrangeActivity;
import com.newthread.android.ui.grade.GradeSelect;
import com.newthread.android.ui.labquery.LabLogin_activity;
import com.newthread.android.ui.library.LibraryCollectActivity;
import com.newthread.android.ui.library.LibraryCurrentBorrowActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

@SuppressLint("ValidFragment")
public class OnCampusRightFragment extends ListFragment {
	private Context con;

	public OnCampusRightFragment() {
	}
	
	public OnCampusRightFragment(Context con) {
		this.con = con;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_right_menu, container, false);
		
		intiView(view);
		return view;
	}

	private void intiView(View view) {
		ListView listView = (ListView) view.findViewById(android.R.id.list);

		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("title", "我的账号");
//		map0.put("icon", R.drawable.ic_left_menu_library);
		data.add(map0);

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("title", "图书收藏");
//		map1.put("icon", R.drawable.ic_left_menu_account);
		data.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("title", "当前借阅");
//		map2.put("icon", R.drawable.ic_on_campus_right_setting);
		data.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("title", "成绩查询");
        // map5.put("icon", R.drawable.ic_on_campus_right_setting);
        data.add(map3);

        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("title", "实验查询");
        // map5.put("icon", R.drawable.ic_on_campus_right_setting);
        data.add(map4);
        HashMap<String, Object> map5 = new HashMap<String, Object>();
        map5.put("title", "考试安排");
        // map5.put("icon", R.drawable.ic_on_campus_right_setting);
        data.add(map5);

		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("title", "设置");
//		map6.put("icon", R.drawable.ic_on_campus_right_setting);
		data.add(map6);
		
		SimpleAdapter adapter = new SimpleAdapter(
				con,
				data,
				R.layout.view_right_menu_item,
				new String[] { "title" },
				new int[] { R.id.right_menu_list_title});

		listView.setAdapter(adapter);		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		if (0 == position) {
			// 账号管理
			Intent _intent = new Intent(con, MyAccountActivity.class);
			startActivity(_intent);
		} else if (1 == position) {
			// 图书收藏
			Intent _intent = new Intent(con, LibraryCollectActivity.class);
			startActivity(_intent);
		} else if (2 == position) {
			// 当前借阅
			Intent _intent = new Intent(con, LibraryCurrentBorrowActivity.class);
			startActivity(_intent);
		}else if (3 == position) {
            // 成绩查询
            Intent _intent = new Intent(con, GradeSelect.class);
            startActivity(_intent);
        } else if (4 == position) {
            // 实验查询
            Intent _intent = new Intent(con, LabLogin_activity.class);
            startActivity(_intent);
        }else if (5 == position) {
            //
            Intent _intent = new Intent(con, ExamArrangeActivity.class);
            startActivity(_intent);
        }else if (6 == position) {
			// 设置
			Intent _intent = new Intent(con, SettingActivity.class);
			startActivity(_intent);
		}
	}
	
}
