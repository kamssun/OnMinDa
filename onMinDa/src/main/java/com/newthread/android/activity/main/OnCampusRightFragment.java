package com.newthread.android.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
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
    List<SlidingMenuConfig.ItemMenu> itemMenus;
	private void intiView(View view) {
        itemMenus = SlidingMenuConfig.getInstance().getRightList();
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (SlidingMenuConfig.ItemMenu itemMenu : itemMenus) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", itemMenu.getTitle());
            data.add(map);
        }
		SimpleAdapter adapter = new SimpleAdapter(
				getActivity().getApplicationContext(),
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
        Intent _intent = new Intent(con, itemMenus.get(position).getClazz());
        startActivity(_intent);
        ((Activity) con).overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);
	}
	
}
