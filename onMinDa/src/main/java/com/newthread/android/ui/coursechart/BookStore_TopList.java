package com.newthread.android.ui.coursechart;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.newthread.android.R;
import com.newthread.android.bean.EverydayCourse;

public class BookStore_TopList extends RelativeLayout {
	LayoutInflater inflater;
	LinearLayout bookstore_container;
	BookStore_Top top;
	Context ctx;
	
	private ArrayList<EverydayCourse> weekCourse;
	
	public BookStore_TopList(Context context, BookStore_Top view, ArrayList<EverydayCourse> weekCourse) {
		super(context);
		top = view;
		ctx = context;
		this.weekCourse = weekCourse;
		inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.bookstore_toplist, null);
		
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addView(layout,layoutParams);
		
		bookstore_container = (LinearLayout)layout.findViewById(R.id.bookstore_container);
	}
	
	public void DataBind(String topid,String name, int index) {
		bookstore_container.removeAllViews();
		BookStore_BookList booklist = new BookStore_BookList(ctx, weekCourse.get(index),index);
		bookstore_container.addView(booklist, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		top.bookstore_toplist_text.setText(name);
	}
}
