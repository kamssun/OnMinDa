package com.newthread.android.ui.news;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 *  单独显示新闻图片
 * @author _foumnder
 *
 */
public class NewsContentImageActivity extends SherlockActivity {
	private Handler mHandler;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	
	private String imageUrl;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 获得数据
		imageUrl = this.getIntent().getCharSequenceExtra("image_url").toString().trim();
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		ColorDrawable color = new ColorDrawable(Color.BLACK);
		color.setAlpha(128);
		getSupportActionBar().setBackgroundDrawable(color);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		mHandler = new Handler();
		
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.CENTER_INSIDE);
		imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getSupportActionBar().show();
				hideActionBarDelayed(mHandler);
			}
		});
		
		setContentView(imageView);

		imageLoader = ImageLoader.getInstance();
		
		imageLoader.displayImage(imageUrl, imageView, NewsContentActivity.options, null);
		
		this.getWindow().setBackgroundDrawableResource(android.R.color.black);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSupportActionBar().show();
		hideActionBarDelayed(mHandler);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void hideActionBarDelayed(Handler handler) {
		handler.postDelayed(new Runnable() {
			public void run() {
				getSupportActionBar().hide();
			}
		}, 2000);
	}
	
	// 对返回键进行监听
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	this.finish();
        	overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        	return true;
         }
         return super.onKeyDown(keyCode, event);
     }
}
