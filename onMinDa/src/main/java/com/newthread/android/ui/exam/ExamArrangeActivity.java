package com.newthread.android.ui.exam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.activity.main.MyApplication;
import com.newthread.android.adapter.ExamListViewAdpeter;
import com.newthread.android.bean.ExamArrangeInfo;
import com.newthread.android.ui.coursechart.CourseChartLoginActivity;
import com.newthread.android.util.*;
import org.kymjs.aframe.database.KJDB;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;

import java.util.List;

/**
 * Created by Administrator on 2014/8/26.
 */
public class ExamArrangeActivity extends SherlockFragmentActivity {
    private static final String URL1 = "http://ids.scuec.edu.cn/amserver/UI/Login?goto=http://my.scuec.edu.cn/index.portal"; // 个人图书馆URL
    private static final String URL2 = "http://ssfw.scuec.edu.cn/ssfw/j_spring_ids_security_check"; // URL
    private static final String URL3 = "http://ssfw.scuec.edu.cn/ssfw/xsks/kcxx.do"; // 考试安排查询网址
    private static final String current_semeser="2014-2015-1";

    private KJHttp kjh;
    private KJDB db;
    private ListView listView;
    private ProgressBar progressBar;
    private String account, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = KJDB.create(getApplicationContext(), "examArrange");
        MyPreferenceManager.init(getApplicationContext());
        account = MyPreferenceManager.getString("admin_system_account", "");
        password = MyPreferenceManager.getString("admin_system_password", "");
        setContentView(R.layout.activity_grade_list);
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) this.findViewById(R.id.library_list_loading);
        initView();
        initData();
    }

    private void initData() {
        List<ExamArrangeInfo> examArrangeInfos = db.findAll(ExamArrangeInfo.class);
        if (!needLogin()) {
            if (examArrangeInfos == null || examArrangeInfos.size() == 0) {
                getExamFromUrl();
            } else {
                setListView(examArrangeInfos);
            }
        }
    }

    private void getExamFromUrl() {
        progressBar.setVisibility(View.VISIBLE);
        kjh = new KJHttp();
        KJStringParams params = new KJStringParams();
        params.put("IDToken1", account);
        params.put("IDToken2", password);
        kjh.post(URL1, params, new StringCallBack() {
            @Override
            public void onSuccess(String json) {
                kjh.get(URL2, new StringCallBack() {
                    @Override
                    public void onSuccess(String json) {
                        KJStringParams params = new KJStringParams();
                        params.put("xnxqdm", current_semeser);
                        kjh.post(URL3, params, new StringCallBack() {
                            @Override
                            public void onSuccess(String html) {
                                progressBar.setVisibility(View.GONE);
                                final List<ExamArrangeInfo> examArrangeInfos = new ExamArrangeParser().parse(html);
                                setListView(examArrangeInfos);
                                //保存到数据库
                                for (ExamArrangeInfo examArrangeInfo : examArrangeInfos) {
                                    db.save(examArrangeInfo);
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListView(final List<ExamArrangeInfo> examArrangeInfos) {
        listView.setAdapter(new ExamListViewAdpeter(examArrangeInfos, getApplication()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.getInstance().putThing("examArrangeInfo",examArrangeInfos.get(position));
                Intent intent = new Intent(getApplicationContext(),ExamArrangeDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayUseLogoEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setTitle("考场安排");
        if (needLogin()) {
            showLoginDialog();
        }


    }

    // 判断是否需要登录
    private boolean needLogin() {
        if (StringUtils.isEmpty(account) | StringUtils.isEmpty(password) | MyPreferenceManager.getBoolean("admin_system_isFirstLogin", true)) {
            return true;
        }
        return false;
    }

    // 登录对话框
    protected void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("需要登录教务系统才能查询");
        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent _intent = new Intent(getApplicationContext(), CourseChartLoginActivity.class);
                startActivity(_intent);
                finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("刷新").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                break;

        }
        if (item.getTitle().equals("刷新")) {
            List<ExamArrangeInfo> examArrangeInfos = db.findAll(ExamArrangeInfo.class);
            for (ExamArrangeInfo examArrangeInfo : examArrangeInfos){
                db.delete(examArrangeInfo);
            }
            getExamFromUrl();
        }
        return super.onMenuItemSelected(featureId, item);

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
