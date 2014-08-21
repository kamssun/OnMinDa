package com.newthread.android.ui.labquery;

import java.util.ArrayList;

public class LoginResponseApi {
	private int code;
	private ArrayList<LabDetail> mArrayList;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ArrayList<LabDetail> getmArrayList() {
		return mArrayList;
	}

	public void setmArrayList(ArrayList<LabDetail> mArrayList) {
		this.mArrayList = mArrayList;
	}
}
