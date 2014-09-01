package com.newthread.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.newthread.android.bean.ExamArrangeInfo;

import java.util.List;

/**
 * Created by jindongping on 14-9-2.
 */
public class ExamListViewAdpeter extends BaseAdapter {
    private List<ExamArrangeInfo> examArrangeInfoList;

    public ExamListViewAdpeter(List<ExamArrangeInfo> examArrangeInfoList) {
        this.examArrangeInfoList = examArrangeInfoList;
    }

    @Override
    public int getCount() {
        return examArrangeInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return examArrangeInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return null;
    }
}
