package com.newthread.android.bean;

public class GradeListAdapterVo {
	private boolean isMultipleSelect;

	public GradeListAdapterVo(boolean isMultipleSelect) {
		this.isMultipleSelect = isMultipleSelect;
	}

	public boolean isMultipleSelect() {
		return isMultipleSelect;
	}

	public void setMultipleSelect(boolean isMultipleSelect) {
		this.isMultipleSelect = isMultipleSelect;
	}
}
