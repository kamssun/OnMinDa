package com.newthread.android.ui.library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.newthread.android.R;
import com.newthread.android.bean.LibraryDetailBookInfo;
import com.newthread.android.util.StringUtils;

public class DetailFragmentTwo extends Fragment {
	private LibraryDetailBookInfo bookInfo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_library_detail_two, container, false);
		TextView detail_digest = (TextView) view.findViewById(R.id.library_detail_page_two_digest);
		TextView detail_intro = (TextView) view.findViewById(R.id.library_detail_page_two_intro);
		Button douBanHref = (Button) view.findViewById(R.id.library_detail_page_two_douban_href);
		
		if (bookInfo != null) {
			detail_digest.setText(StringUtils.isEmpty(bookInfo.getDigest()) ? "无" : bookInfo.getDigest());
			detail_intro.setText(StringUtils.isEmpty(bookInfo.getIntro()) ? "无" : bookInfo.getIntro());
			douBanHref.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Uri uri = Uri.parse(bookInfo.getDouBanHref());   
			        Intent  _intent = new Intent(Intent.ACTION_VIEW,uri);    
			        startActivity(_intent);    
				}
				
			});
		}
		
		return view;
	}
	
	// 更新数据 
	public void updateData(LibraryDetailBookInfo bookInfo) {
		if (bookInfo != null) {
			this.bookInfo = bookInfo;
		}
	}
}
