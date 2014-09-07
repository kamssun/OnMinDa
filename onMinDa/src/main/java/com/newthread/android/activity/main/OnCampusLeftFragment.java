package com.newthread.android.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.newthread.android.R;
import com.newthread.android.ui.coursechart.CourseChartLoginActivity;
import com.newthread.android.ui.exam.ExamArrangeActivity;
import com.newthread.android.ui.grade.GradeSelect;
import com.newthread.android.ui.labquery.LabLogin_activity;
import com.newthread.android.ui.library.LibraryActivity;
import com.newthread.android.ui.news.NewsActivity;
import com.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class OnCampusLeftFragment extends ListFragment {
    private Context con;
    private View logoView;
    private SimpleAdapter adapter;

    public OnCampusLeftFragment() {
    }

    public OnCampusLeftFragment(Context con) {
        this.con = con;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_left_menu_list, null);

        initView(view);

        return view;
    }

    List<SlidingMenuConfig.ItemMenu> itemMenus;

    private void initView(View view) {
        itemMenus = SlidingMenuConfig.getInstance().getLeftList();
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (SlidingMenuConfig.ItemMenu itemMenu : itemMenus) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", itemMenu.getTitle());
            data.add(map);
        }
        adapter = new SimpleAdapter(con, data, R.layout.view_left_menu_item,
                new String[]{"title"},
                new int[]{R.id.left_menu_list_title});
        listView.setAdapter(adapter);
        logoView = view.findViewById(R.id.left_menu_top_view);
        logoView.setVisibility(View.INVISIBLE);
    }

    public void refreshLogo() {
        logoView.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.Shake)
                .duration(2000)
                .playOn(logoView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        Intent _intent = new Intent(con, itemMenus.get(position).getClazz());
        startActivity(_intent);
        ((Activity) con).overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);

    }
}