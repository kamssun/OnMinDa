package com.newthread.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newthread.android.R;
import com.newthread.android.bean.TuanGouListItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class DianPingListAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<TuanGouListItem> list;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private SimpleImageLoadingListener imageListener;
	
	public DianPingListAdapter(Context context, ArrayList<TuanGouListItem> list, SimpleImageLoadingListener imageListener) {
		this.mContext = context;
		this.list = list;
		this.imageListener = imageListener;
		
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_search)
			.showImageForEmptyUri(R.drawable.ic_search)
			.showImageOnFail(R.drawable.ic_search)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.displayer(new RoundedBitmapDisplayer(4))
			.build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_dianping_list_item, null);
			viewHolder = new ViewHolder();
			
			viewHolder.item = (View) convertView.findViewById(R.id.tuangou_item);
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.description = (TextView) convertView.findViewById(R.id.description);
			viewHolder.original_price = (TextView) convertView.findViewById(R.id.original_price);
			viewHolder.current_price = (TextView) convertView.findViewById(R.id.current_price);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		TuanGouListItem item = list.get(position);
		
		viewHolder.title.setText(item.getTitle());
		viewHolder.description.setText(item.getDescription());
		viewHolder.current_price.setText(item.getCurrentPrice() + "元");
		viewHolder.original_price.setText(item.getOriginalPrice() + "元");
		
		viewHolder.original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
		
		imageLoader.displayImage(item.getS_image_url(), viewHolder.mImageView, options, imageListener);
//		viewHolder.mImageView.setImageResource(R.drawable.ic_search);
		
		viewHolder.item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = list.get(position).getDealURL();
				Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});
		
		return convertView;
	}

	static class ViewHolder {
		View item;
		TextView title;
		TextView description;
		TextView original_price;
		TextView current_price;
		ImageView mImageView;
	}
}
