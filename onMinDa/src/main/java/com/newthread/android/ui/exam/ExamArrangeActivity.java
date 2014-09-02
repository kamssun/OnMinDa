package com.newthread.android.ui.exam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.adapter.ExamListViewAdpeter;
import com.newthread.android.bean.ExamArrangeInfo;
import com.newthread.android.ui.coursechart.CourseChartLoginActivity;
import com.newthread.android.util.*;
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


    private KJHttp kjh;
    private ListView listView;
    private ProgressBar progressBar;
    private String account, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_list);
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) this.findViewById(R.id.library_list_loading);
        initData();
        initView();
    }

    private void initData() {
        MyPreferenceManager.init(getApplicationContext());
        account = MyPreferenceManager.getString("admin_system_account", "");
        password = MyPreferenceManager.getString("admin_system_password", "");
        if (!needLogin()) {
            progressBar.setVisibility(View.VISIBLE);
            kjh = new KJHttp();
            KJStringParams params = new KJStringParams();
            params.put("IDToken1", account);
            params.put("IDToken2", password);
            kjh.post(URL1, params, new StringCallBack() {
                @Override
                public void onSuccess(String json) {
                    FileUtil.write(getApplicationContext(), "1.txt", json);
                    kjh.get(URL2, new StringCallBack() {
                        @Override
                        public void onSuccess(String json) {
                            FileUtil.write(getApplicationContext(), "2.txt", json);
                            KJStringParams params = new KJStringParams();
                            params.put("xnxqdm", "2013-2014-2");
                            kjh.post(URL3, params, new StringCallBack() {
                                @Override
                                public void onSuccess(String html) {
                                    FileUtil.write(getApplicationContext(), "3.txt", html);
//                                    List<ExamArrangeInfo> examArrangeInfos = new ExamArrangeParser().parse(html);
//                                    listView.setAdapter(new ExamListViewAdpeter(examArrangeInfos));
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {super.onFailure(t, errorNo, strMsg);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        if (StringUtils.isEmpty(account)
                | StringUtils.isEmpty(password)
                | MyPreferenceManager.getBoolean("admin_system_isFirstLogin", true)) {
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
//        menu.add("完成").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
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
