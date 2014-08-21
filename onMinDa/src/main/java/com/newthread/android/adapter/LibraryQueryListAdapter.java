package com.newthread.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newthread.android.R;
import com.newthread.android.bean.LibraryQueryItemInfo;
import com.newthread.android.util.Logger;

public class LibraryQueryListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<LibraryQueryItemInfo> list;

	public LibraryQueryListAdapter(Context con, List<LibraryQueryItemInfo> list) {
		this.inflater = LayoutInflater.from(con);
		this.list = list;
	}

	@Override
	public int getCount() {
		Logger.i("getCount", "" + list.size()); 
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
			convertView = this.inflater.inflate(R.layout.library_query_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.library_list_title);
			holder.auther = (TextView) convertView.findViewById(R.id.library_list_auther);
			holder.id = (TextView) convertView.findViewById(R.id.library_list_id);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(list.get(position).getTitle());
		holder.auther.setText(list.get(position).getAuther());
		holder.id.setText(list.get(position).getId());

		return convertView;
	}

	public void addItem(LibraryQueryItemInfo item) {
		list.add(item);
	}

	public class ViewHolder {
		private TextView title;
		private TextView auther;
		private TextView id;
	}

}
