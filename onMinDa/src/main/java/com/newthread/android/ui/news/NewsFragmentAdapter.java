package com.newthread.android.ui.news;

import java.util.ArrayList;
import com.newthread.android.bean.NewsListItem;
import com.newthread.android.util.Logger;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

public class NewsFragmentAdapter extends FragmentPagerAdapter  {
	private static final String[] CONTENT = new String[] { "民大要闻", "综合新闻", "院系风采",};
    private int mCount = CONTENT.length;
    private ArrayList<NewsListItem> listOne;
    private ArrayList<NewsListItem> listTwo;
    private ArrayList<NewsListItem> listThree;
    private NewsFragmentOne newsFragmentOne;
    private NewsFragmentTwo newsFragmentTwo;
    private NewsFragmentThree newsFragmentThree;
    private Context con;
    
	public NewsFragmentAdapter(FragmentManager fm, ArrayList<NewsListItem> listOne, ArrayList<NewsListItem> listTwo,
								ArrayList<NewsListItem> listThree, Context con) {
		super(fm);
		this.listOne = listOne;
		this.listTwo = listTwo;
		this.listThree = listThree;
		this.con = con;
	}

    public NewsFragmentTwo getNewsFragmentTwo() {
        return newsFragmentTwo;
    }

    public NewsFragmentOne getNewsFragmentOne() {
        return newsFragmentOne;
    }

    public NewsFragmentThree getNewsFragmentThree() {
        return newsFragmentThree;
    }

    public Fragment getItem(int position) {
		if (0 == position) {
            newsFragmentOne =new NewsFragmentOne(con, listOne);
			 return newsFragmentOne;
    	} else if (1 == position) {
            newsFragmentTwo=new NewsFragmentTwo(con, listTwo);
    		 return newsFragmentTwo;
    	} else if (2 == position) {
            newsFragmentThree =new NewsFragmentThree(con, listThree);
    		 return newsFragmentThree;
    	}
		return new NewsFragmentOne(con, listOne);
    }

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
    	if (0 == position) {
    		NewsFragmentOne fragmentOne = (NewsFragmentOne) super.instantiateItem(container, position);  
			return fragmentOne;
    	} else if (1 == position) {
    		NewsFragmentTwo fragmentTwo = (NewsFragmentTwo) super.instantiateItem(container, position);  
			return fragmentTwo;
    	} else if (2 == position) {
    		NewsFragmentThree fragmentThree = (NewsFragmentThree) super.instantiateItem(container, position);  
			return fragmentThree;
    	}
    	
		return super.instantiateItem(container, position);
	}

    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return NewsFragmentAdapter.CONTENT[position % CONTENT.length];
    }
    
    @Override
	public int getItemPosition(Object object) {
	    return PagerAdapter.POSITION_NONE;
	}
}
