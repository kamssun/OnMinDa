package com.newthread.android.ui.news;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.newthread.android.R;
import com.newthread.android.bean.NewsListItem;

public class NewsListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<NewsListItem> list;
	
	public NewsListAdapter() {
	}
	
	public NewsListAdapter(Context context, ArrayList<NewsListItem> list) {
		this.inflater = LayoutInflater.from(context);
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.view_news_item, parent, false);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.news_list_title);
			holder.digest = (TextView) convertView.findViewById(R.id.news_list_digest);
			holder.time = (TextView) convertView.findViewById(R.id.news_list_time);
			convertView.setTag(holder);
		} else {
            holder = (ViewHolder) convertView.getTag();
		}
		
		NewsListItem item = list.get(position);
		holder.title.setText(item.getTitle());
		holder.digest.setText(item.getDigest());
		holder.time.setText(item.getTime());
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView title;
		TextView digest;
		TextView time;
	}
}
