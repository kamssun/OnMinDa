package com.newthread.android.bean;

public class LibraryQueryListReturnPara {
	private String totalSize;  		
	private String nextPageHref;	
	
	public String getNextPageHref() {
		return nextPageHref;
	}

	public void setNextPageHref(String nextPageHref) {
		this.nextPageHref = nextPageHref;
	}

	public String getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}
	
	
}
