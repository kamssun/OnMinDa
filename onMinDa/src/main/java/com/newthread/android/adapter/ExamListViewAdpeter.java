package com.newthread.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.newthread.android.R;
import com.newthread.android.bean.ExamArrangeInfo;

import java.util.List;

/**
 * Created by jindongping on 14-9-2.
 */
public class ExamListViewAdpeter extends BaseAdapter {
    private List<ExamArrangeInfo> examArrangeInfoList;
    private LayoutInflater inflater;

    public ExamListViewAdpeter(List<ExamArrangeInfo> examArrangeInfoList, Context con) {
        this.inflater = LayoutInflater.from(con);
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
        ViewHolder holder ;

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.library_query_list_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.library_list_title);
            holder.auther = (TextView) convertView.findViewById(R.id.library_list_auther);
            holder.id = (TextView) convertView.findViewById(R.id.library_list_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(examArrangeInfoList.get(position).getCourseName());
        holder.auther.setText(examArrangeInfoList.get(position).getExamTime());
        holder.id.setText(examArrangeInfoList.get(position).getExamAddress());

        return convertView;
    }

    public class ViewHolder {
        private TextView title;
        private TextView auther;
        private TextView id;
    }
}
