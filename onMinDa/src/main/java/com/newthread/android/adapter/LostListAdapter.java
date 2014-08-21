package com.newthread.android.adapter;

import java.util.ArrayList;
import com.newthread.android.R;
import com.newthread.android.bean.LostListItem;
import com.newthread.android.util.Logger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LostListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<LostListItem> list;
	
	public LostListAdapter(Context con, ArrayList<LostListItem> list) {
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
			convertView = this.inflater.inflate(R.layout.view_lost_list_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.lost_item_name);
			holder.place = (TextView) convertView.findViewById(R.id.lost_item_place);
			holder.time = (TextView) convertView.findViewById(R.id.lost_item_time);
			holder.description = (TextView) convertView.findViewById(R.id.lost_item_description );
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		LostListItem item = list.get(position);
		
		if (item == null) {
			Logger.i("null", "item == null");
		}
		
		Logger.i("item.getName()", " " + item.getName() + ", size: " + list.size());
		
		holder.name.setText(item.getName());
		holder.place.setText(item.getPlace());
		holder.time.setText(item.getTime());
		holder.description.setText(item.getDescription());

		return convertView;
	}
	
	public class ViewHolder {
		private TextView name;
		private TextView place;
		private TextView time;
		private TextView description;
	}

}
