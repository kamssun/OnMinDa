package com.newthread.android.ui.news;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.NewsContentVo;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.global.URL;
import com.newthread.android.service.NewsContentLoader;
import com.newthread.android.util.Logger;
import com.newthread.android.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class NewsContentActivity extends SherlockActivity {
	private ImageView image;	// 图片
	private TextView title;	// 标题
	private TextView detail;	// 内容
	private TextView source;	// 来源
	private TextView prompt;	// 提示
	private TextView time;
	
	private ProgressBar progressBar;
	private NewsContentVo vo;
	
	private String titleStr;
	private String timeStr;
	private String url;		// 文章地址
	
	static DisplayImageOptions options;
	ImageLoader imageLoader;
	
	private static final String TAG = "NewsContentActivity";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		//////////////////////
		// 更换主题
		//////////////////////
//        setTheme(SampleList.THEME); //Used for theme switching in samples
//        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        
        initData();
        initView();
	        	
    	loadContent();
       }
	
	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			progressBar.setVisibility(View.GONE);
			
			switch(msg.what) {
			case HandleMessage.QUERY_SUCCESS:
				// 成功
				querySuccess();
				
				break;
			case HandleMessage.QUERY_ERROR:
				// 错误
				prompt.setText("加载失败");
				break;
			case HandleMessage.NO_CONTENT:
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};
	
	// 查询成功
	private void querySuccess() {
		// 绑定数据
		bindingData();
		// 下载图片
		if (!StringUtils.isEmpty(vo.getImageUrl())) {
			downloadImage(vo.getImageUrl());
		}
	}

	
	//  BUG:
	//  Try to initialize ImageLoader which had already been initialized before. 
	//  To re-init ImageLoader with new configuration call ImageLoader.destroy() at first.
	// 下载图片
	private void downloadImage(String imageUrl) {
		imageLoader.displayImage(imageUrl, image, options, new AnimateFirstDisplayListener());
	}

	// 加载内容
	public void loadContent() {
		progressBar.setVisibility(View.VISIBLE);
		vo = new NewsContentVo();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				int result = new NewsContentLoader(getApplicationContext(), vo, url).loadContent();
				Logger.i(TAG, "result: " + result);
				
				handler.sendEmptyMessage(result);
			}
			
		}).start();

	}
	
	// 初始化数据
	private void initData() {
		// 获得该新闻的超链接
		url = this.getIntent().getCharSequenceExtra("href").toString();
		timeStr = this.getIntent().getCharSequenceExtra("time").toString() != null ? this.getIntent().getCharSequenceExtra("time").toString() : "";
		titleStr = this.getIntent().getCharSequenceExtra("title").toString() != null ? this.getIntent().getCharSequenceExtra("title").toString() : "";
		Logger.i("time", timeStr);
	}
	
	// 初始化界面 
	private void initView() {
		ActionBar ab = this.getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("新闻详情");
		
		///////////////////  setBackgroundDrawableResource  ////////////////////
		this.getWindow().setBackgroundDrawableResource(android.R.color.black);
		
		imageLoader = ImageLoader.getInstance();
		
		options = new DisplayImageOptions.Builder()
						.showStubImage(R.drawable.ic_launcher)
						.showImageForEmptyUri(R.drawable.ic_launcher)
						.showImageOnFail(R.drawable.ic_launcher)
						.cacheInMemory(true)
						.cacheOnDisc(true)
						.displayer(new RoundedBitmapDisplayer(0))
						.build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		
		time = (TextView)this.findViewById(R.id.news_content_time);
		image = (ImageView)this.findViewById(R.id.news_content_image);
		title = (TextView)this.findViewById(R.id.news_content_title);
		detail = (TextView)this.findViewById(R.id.news_content_detail);
		source = (TextView)this.findViewById(R.id.news_content_source);
		progressBar = (ProgressBar)this.findViewById(R.id.news_content_loading);
		prompt = (TextView)this.findViewById(R.id.news_content_prompt);
		
		// 点击图片跳转到另一个Activity中显示
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 跳转
				Intent _intent = new Intent(getApplicationContext(), NewsContentImageActivity.class);
				_intent.putExtra("image_url", vo.getImageUrl());
				startActivity(_intent);
			}
		});
	}
	
	// 绑定数据
	private void bindingData() {
		time.setText("时间: " + timeStr);
		source.setText(vo.getSource());
		title.setText(vo.getTitle());
		detail.setText(vo.getContent());
	}
	
	// 图片下载监听
	public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				Logger.i("onLoadingFailed", "图片下载成功");
				
				ImageView imageView = (ImageView) view;
				
				imageView.setVisibility(View.VISIBLE);
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			super.onLoadingStarted(imageUri, view);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			Logger.i("onLoadingFailed", "图片下载失败");
			super.onLoadingFailed(imageUri, view, failReason);
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			super.onLoadingCancelled(imageUri, view);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("分享").setIcon(R.drawable.ic_share).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}
		
		if (item.getTitle().equals("分享")) {
			startActivity(createShareIntent());
		}
		return super.onOptionsItemSelected(item);
	}
	
	 /**
	 * Creates a sharing {@link Intent}.		
	 */
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("image/*");/_STREAM, uri);
        shareIntent.setType("text/plain");   
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");   
        shareIntent.putExtra(Intent.EXTRA_TEXT,  titleStr + ": " + url + " from人在民大:" + URL.APK_DOWNLOAD);    
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        return shareIntent;
    }
}
