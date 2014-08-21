package com.newthread.android.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.newthread.android.R;
import com.newthread.android.bean.BookCollectItem;
import com.newthread.android.bean.CurrentBorrowItem;

public class CurrentBorrowAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CurrentBorrowItem> list;

	public CurrentBorrowAdapter(Context con, List<CurrentBorrowItem> list) {
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
			convertView = this.inflater.inflate(R.layout.view_library_current_borrow_item, null);
			holder = new ViewHolder();
			
			holder.title = (TextView) convertView.findViewById(R.id.current_borrow_title);
			holder.overTime = (TextView) convertView.findViewById(R.id.current_borrow_overtime);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(list.get(position).getBookName());
		holder.overTime.setText("超期时间:" + list.get(position).getOverTime());

		return convertView;
	}

	public class ViewHolder {
		private TextView title;
		private TextView overTime;
	}
}
