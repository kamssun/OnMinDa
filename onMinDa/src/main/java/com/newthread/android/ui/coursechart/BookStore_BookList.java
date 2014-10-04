package com.newthread.android.ui.coursechart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newthread.android.R;
import com.newthread.android.bean.EverydayCourse;
import com.newthread.android.bean.SingleCourseInfo;

public class BookStore_BookList extends RelativeLayout {
    private static final String TAG = "BookStore_BookList";
	LinearLayout layout;
	LayoutInflater inflater;
	Context ctx;
	String adPos;
	ListView lstBooklist;
	BookAdapter adapter;
	private EverydayCourse everydayCourse;
	private int day;
	public BookStore_BookList(Context context, EverydayCourse everydayCourse,int index) {
		super(context);
		ctx = context;
		this.everydayCourse = everydayCourse;
        Log.v(TAG, everydayCourse.toString());
		this.day=index;
		Init();
	}

	private void Init() {
		inflater = LayoutInflater.from(ctx);
		layout = (LinearLayout) inflater.inflate(R.layout.bookstore_booklist,
				null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		this.addView(layout, params);

		lstBooklist = (ListView) layout.findViewById(R.id.lstBooklist);
		adapter = new BookAdapter();
		lstBooklist.setAdapter(adapter);
		
		lstBooklist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				SingleCourseInfo singleCourse = everydayCourse.getDayOfWeek().get(position);
				Intent _intent = new Intent(ctx, CourseDetailActivity.class);
				Bundle bundle =new Bundle();
				bundle.putInt("day", day);
				bundle.putInt("position", position);
				_intent.putExtras(bundle);
				_intent.putExtra("course_info", singleCourse);
				ctx.startActivity(_intent);
			}
		});
	}

	// //////////////////////////////////
	public void notifyDataSetChanged() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	private OnBookListScrollListener onScrollListener;

	public interface OnBookListScrollListener {
		void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount);

		void onTouch(View v, MotionEvent event);
	}

	public void SetOnScrollListener(OnBookListScrollListener listener) {
		onScrollListener = listener;
	}

	public void SetPos(String pos) {
		adPos = pos;
	}

	private class BookAdapter extends BaseAdapter {
		public BookAdapter() {
		}

		@Override
		public int getCount() {
			return everydayCourse.getDayOfWeek().size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.bookstore_booklist_item, null);
				holder = new ViewHolder();
				holder.bookstore_booklist_item_cover = (TextView) convertView
						.findViewById(R.id.bookstore_booklist_item_cover);
				holder.bookstore_booklist_item_name = (TextView) convertView
						.findViewById(R.id.bookstore_booklist_item_name);
				holder.bookstore_booklist_item_author = (TextView) convertView
						.findViewById(R.id.bookstore_booklist_item_author);
				holder.bookstore_booklist_item_chapter = (TextView) convertView
						.findViewById(R.id.bookstore_booklist_item_chapter);
				holder.bookstore_booklist_item_time = (TextView) convertView
						.findViewById(R.id.bookstore_booklist_item_time);
				holder.bookstore_booklist_item_wordcount = (TextView) convertView
						.findViewById(R.id.bookstore_booklist_item_wordcount);

				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			try {
				SingleCourseInfo singleCourse = everydayCourse.getDayOfWeek().get(position);
                holder.bookstore_booklist_item_cover.setText(1 + position + "");

				// 如果有课
				if (singleCourse.isHaveCourse()) {
                    holder.bookstore_booklist_item_name.setTextColor(ctx.getResources().getColor(R.color.black));
                    holder.bookstore_booklist_item_name.setText(singleCourse
							.getCourseName());
					holder.bookstore_booklist_item_author.setText("第" + singleCourse
							.getNumOfDay() + "节");
					holder.bookstore_booklist_item_chapter.setText(singleCourse
							.getClassromNum());
					holder.bookstore_booklist_item_time.setText(singleCourse
							.getTeacherName());
					holder.bookstore_booklist_item_wordcount
							.setText(singleCourse.getSustainTime());
				} else {
					holder.bookstore_booklist_item_name.setText("现在没课哦..");
					holder.bookstore_booklist_item_name.setTextSize(20);
					holder.bookstore_booklist_item_name.setTextColor(ctx.getResources().getColor(R.color.abs__holo_blue_light));
                    holder.bookstore_booklist_item_author.setText("");
                    holder.bookstore_booklist_item_chapter.setText("");
                    holder.bookstore_booklist_item_time.setText("");
                    holder.bookstore_booklist_item_wordcount.setText("");
                }

				convertView.setBackgroundResource(R.drawable.listitembg2);
				convertView.setPadding(dip2px(10), dip2px(5), 0, dip2px(5));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	private static class ViewHolder {
		public TextView bookstore_booklist_item_cover;
		public TextView bookstore_booklist_item_name;
		public TextView bookstore_booklist_item_author;
		public TextView bookstore_booklist_item_chapter;
		public TextView bookstore_booklist_item_time;
		public TextView bookstore_booklist_item_wordcount;
	}

	public int dip2px(float dipValue) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public int px2dip(float pxValue) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public String FixWords(int words) {
		if (words > 10000) {
			return (words / 10000) + "����";
		}
		return words + "��";
	}

	public String FixTime(long time) {
		long n = System.currentTimeMillis() - time;
		if (n < 1000l * 60) {
			return "1������";
		} else if (n < 1000l * 60 * 60) {
			long min = n / (1000l * 60);
			return min + "����ǰ";
		} else if (n < 1000l * 60 * 60 * 24) {
			long hour = n / (1000l * 60 * 60);
			return hour + "Сʱǰ";
		} else if (n < 1000l * 60 * 60 * 24 * 30) {
			long day = n / (1000l * 60 * 60 * 24);
			return day + "��ǰ";
		} else if (n < 1000l * 60 * 60 * 24 * 30 * 12) {
			long month = n / (1000l * 60 * 60 * 24 * 30);
			return month + "��ǰ";
		} else {
			return "�ܾ���ǰ";
		}
	}

}
