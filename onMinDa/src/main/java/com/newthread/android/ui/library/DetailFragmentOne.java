package com.newthread.android.ui.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.newthread.android.R;
import com.newthread.android.bean.LibraryDetailBookInfo;

public class DetailFragmentOne extends Fragment {
	private TextView library_detail_title;
	private TextView library_detail_auther;
	private TextView library_detail_publisher;
	private ListView library_detail_listview;

	private LibraryDetailBookInfo bookInfo;
	
	private Context con;
	
	public DetailFragmentOne(Context con) {
		this.con = con;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_library_detail_one, container, false);
		
		library_detail_title = (TextView) view.findViewById(R.id.library_detail_title);
		library_detail_auther = (TextView) view.findViewById(R.id.library_detail_auther);
		library_detail_publisher = (TextView) view.findViewById(R.id.library_detail_publisher);
		library_detail_listview = (ListView) view.findViewById(R.id.library_detail_listview);
		
		if (bookInfo != null) {
			library_detail_title.setText(bookInfo.getTitle());
			library_detail_auther.setText(bookInfo.getAuther());
			library_detail_publisher.setText(bookInfo.getPublisher());
			
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			
			int size = bookInfo.getList().size();
			for (int i = 0; i < size; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", bookInfo.getList().get(i).getId());
				map.put("location", bookInfo.getList().get(i).getLocation());
				map.put("status", bookInfo.getList().get(i).getStatus());
				
				data.add(map);
			}
			
			SimpleAdapter adapter = new SimpleAdapter(con,  
										data,
										R.layout.library_detail_item,
										new String [] {"id","location","status"},
										new int []{R.id.detail_id, R.id.detail_location, R.id.detail_status});
			library_detail_listview.setAdapter(adapter);
		}

		return view;
	}
	
	public void updateData(LibraryDetailBookInfo bookInfo) {
		if (bookInfo != null) {
			this.bookInfo = bookInfo;
		}
	}
}
