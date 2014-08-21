package com.newthread.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.newthread.android.R;
import com.newthread.android.bean.BookCollectItem;

public class LibraryCollectAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<BookCollectItem> list;

	public LibraryCollectAdapter(Context con, List<BookCollectItem> list) {
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
			convertView = this.inflater.inflate(R.layout.view_library_collect_list_item, null);
			holder = new ViewHolder();
			
			holder.title = (TextView) convertView.findViewById(R.id.collect_title);
			holder.auther = (TextView) convertView.findViewById(R.id.collect_auther);
			holder.publisher = (TextView) convertView.findViewById(R.id.collect_publisher);
			holder.place = (TextView) convertView.findViewById(R.id.collect_place);
			holder.id = (TextView) convertView.findViewById(R.id.collect_id);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(list.get(position).getTitle());
		holder.auther.setText(list.get(position).getAuther());
		holder.publisher.setText(list.get(position).getPublisher());
		holder.place.setText(list.get(position).getPlace());
		holder.id.setText(list.get(position).getId());

		return convertView;
	}

	public void addItem(BookCollectItem item) {
		list.add(item);
	}

	public class ViewHolder {
		private TextView title;
		private TextView auther;
		private TextView publisher;
		private TextView id;
		private TextView place;
		
		private Button btn;
	}

}
