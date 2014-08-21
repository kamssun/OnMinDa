package com.newthread.android.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.newthread.android.R;
import com.newthread.android.bean.NewsListItem;

public class NewsListviewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<NewsListItem> list;
	
	public NewsListviewAdapter(Context con, ArrayList<NewsListItem> list) {
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
			convertView = this.inflater.inflate(R.layout.view_news_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.news_list_title);
			holder.digest = (TextView) convertView.findViewById(R.id.news_list_digest);
			holder.time = (TextView) convertView.findViewById(R.id.news_list_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(list.get(position).getTitle());
		holder.digest.setText(list.get(position).getDigest());
		holder.time.setText(list.get(position).getTime());

		return convertView;
	}
	
	public void addItem(NewsListItem item) {
		list.add(item);
	}

	public class ViewHolder {
		private TextView title;
		private TextView digest;
		private TextView time;
	}

}
