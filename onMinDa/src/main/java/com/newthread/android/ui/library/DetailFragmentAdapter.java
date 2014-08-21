package com.newthread.android.ui.library;


import com.newthread.android.bean.LibraryDetailBookInfo;
import com.newthread.android.util.Logger;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

public class DetailFragmentAdapter extends FragmentPagerAdapter {
	private LibraryDetailBookInfo bookInfo;
	private Context con;
	
	public DetailFragmentAdapter(FragmentManager fm, LibraryDetailBookInfo bookInfo, Context con) {
		super(fm);
		this.bookInfo = bookInfo;
		this.con = con;
	}

	@Override
	public Fragment getItem(int position) {
		Logger.i("getItem: ", "" + position);
		if (position == 0) {
			return new DetailFragmentOne(con);
		} else {
			return new DetailFragmentTwo();
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		DetailFragmentOne fragmentOne = null;
		
		if (0 == position) {
			Logger.i("instantiateItem-0", "update-0");
			
			fragmentOne = (DetailFragmentOne) super.instantiateItem(container, position);  
			if (bookInfo != null) {
				fragmentOne.updateData(bookInfo);
			}
			
			return fragmentOne;
		} else if (1 == position) {
			Logger.i("instantiateItem-1", "update-1");
			
			DetailFragmentTwo fragmentTwo = (DetailFragmentTwo) super.instantiateItem(container, position);
			if (bookInfo != null) {
				fragmentTwo.updateData(bookInfo);
			}
			
			return fragmentTwo;
		}
		
		return fragmentOne;
	}
	
	@Override
	public int getItemPosition(Object object) {
	    return PagerAdapter.POSITION_NONE;
	}
}
