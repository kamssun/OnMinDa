package com.newthread.android.ui.news;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.newthread.android.util.Loger;
import org.apache.http.util.EncodingUtils;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.NewsListItem;
import com.viewpagerindicator.TitlePageIndicator;

public class NewsActivity extends SherlockFragmentActivity {
    private NewsFragmentAdapter mAdapter;
    private ViewPager mViewPager;
    private TitlePageIndicator mIndicator;
    private Boolean temp =true,temp1=true,temp2=true;
    private ArrayList<NewsListItem> listOne;
    private ArrayList<NewsListItem> listTwo;
    private ArrayList<NewsListItem> listThree;
    private static String FILENAME = "news_one.txt"; // 当前保存文件名

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        initView();
    }

    public void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setTitle("校园新闻");

        listOne = new ArrayList<>();
        listTwo = new ArrayList<>();
        listThree = new ArrayList<>();
        mAdapter = new NewsFragmentAdapter(getSupportFragmentManager(),
                listOne, listTwo, listThree, getApplicationContext());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
        mIndicator.setBackgroundColor(this.getResources().getColor(R.color.tab_background));
        mIndicator.setViewPager(mViewPager);
        // 对mInidicator监听，正在滑向另外一页时刷新
        mIndicator
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // 记录最后所在的pager位置
                        if(position==1&&temp1){
                            temp1=false;
                            mAdapter.getNewsFragmentTwo().getListView().setRefreshing();
                        }else if(position==2&&temp2){
                            temp2=false;
                            mAdapter.getNewsFragmentThree().getListView().setRefreshing();
                        }
                    }

                    @Override
                    public void onPageScrolled(int position,float positionOffset, int positionOffsetPixels) {
                        // 滑动刷新前进行判断是否刚刚刷下

                        if(temp){
                            temp=false;
                            Loger.V(temp);
                            mAdapter.getNewsFragmentOne().getListView().setRefreshing();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });

    }

    // 从指定文件中读取字符串
    public static String getStrFromFile(String filePathDir, int pageNum) {
        File destDir = new File(filePathDir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        String res = "";
        try {
            if (0 == pageNum) {
                FILENAME = "news_one.txt";
            } else if (1 == pageNum) {
                FILENAME = "news_two.txt";
            } else if (2 == pageNum) {
                FILENAME = "news_three.txt";
            }
            FileInputStream fin = new FileInputStream("mnt/sdcard/OnMinDa/"
                    + FILENAME);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
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

        return super.onOptionsItemSelected(item);
    }

    // 构造假数据
    public void testData() {
        for (int i = 0; i < 10; i++) {
            NewsListItem item = new NewsListItem();
            item.setTitle("title" + i);
            item.setDigest("digest " + i);
            item.setTime("time " + i);
            item.setHref("href " + i);
            listOne.add(item);
        }
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
