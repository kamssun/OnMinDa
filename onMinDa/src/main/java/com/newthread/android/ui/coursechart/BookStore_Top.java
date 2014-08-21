package com.newthread.android.ui.coursechart;

import java.util.ArrayList;
import org.json.JSONArray;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.newthread.android.R;
import com.newthread.android.bean.EverydayCourse;

public class BookStore_Top extends LinearLayout {

	LayoutInflater inflater;

	Context ctx;
	LinearLayout bookstore_top;
	JSONArray jsonList;

	public int CurrIndex;
	public String[] mMenuNames;
	public String[] mMenuValues;

	LeftRightExtendView layout;
	BookStore_TopLeft topLeft;
	BookStore_TopList toplist;
	LinearLayout top;

	TextView bookstore_toplist_text;
	TextView bookstore_toplist_btn;

    private OnRefreshViewListener listener;

    public interface OnRefreshViewListener {
        public void onRefresh(int index);
    }

	// BookStore_TopList list1;
	// BookStore_TopList list2;

	private ArrayList<EverydayCourse> weekCourse;

	public BookStore_Top(Context context, ArrayList<EverydayCourse> weekCourse, OnRefreshViewListener listener) {
		super(context);
		ctx = context;
		this.weekCourse = weekCourse;
        this.listener = listener;
		Init();
	}

	private void Init() {
		mMenuNames = getResources().getStringArray(R.array.bookstore_top);
		mMenuValues = getResources().getStringArray(R.array.bookstore_top_value);

		setOrientation(VERTICAL);

		inflater = LayoutInflater.from(ctx);
		top = (LinearLayout) inflater.inflate(R.layout.bookstore_top, null);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, dip2px(35));
		this.addView(top, params);

		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 0);
		params2.weight = 1;
		RelativeLayout container = new RelativeLayout(ctx);
		addView(container, params2);

		layout = new LeftRightExtendView(ctx);
		layout.setLeftRightWidth(120);

		container.addView(layout, RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);

		topLeft = new BookStore_TopLeft(ctx, this);
		toplist = new BookStore_TopList(ctx, this, weekCourse);

		layout.addViewLeft(topLeft);
		layout.addViewCenter(toplist);

		layout.setLeftRightWidth(110);

		toplist.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (layout.isLeftExtended()) {
					layout.showCenter();
					return true;
				}
				return false;
			}
		});

		bookstore_toplist_text = (TextView) top.findViewById(R.id.bookstore_toplist_text);
		bookstore_toplist_btn = (TextView) top.findViewById(R.id.bookstore_toplist_btn);

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isLeftExtended()) {
					showCenter();
				} else {
					showLeft();
				}
			}
		};

		bookstore_toplist_btn.setOnClickListener(onClickListener);
		bookstore_toplist_text.setOnClickListener(onClickListener);

		this.postDelayed(new Runnable() {

			@Override
			public void run() {
				showLeft();
			}
		}, 500);

	}

	public void showLeft() {
		bookstore_toplist_btn.setText("课程详细");
		layout.showLeft();
	}

	public void showCenter() {
		bookstore_toplist_btn.setText("收起");
		layout.showCenter();
	}

	public boolean isLeftExtended() {
		return layout.isLeftExtended();
	}

	public int dip2px(float dipValue) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public void DataBind(int index) {
		// 展开或收起
		if (isLeftExtended()) {
//			showCenter();
		}

		CurrIndex = index;
		toplist.DataBind(mMenuValues[index], mMenuNames[index], index);
		topLeft.notifyDataSetChanged();
	}
	
}
