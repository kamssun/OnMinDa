package com.newthread.android.ui.exam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.global.HandleMessage;
import com.newthread.android.ui.coursechart.CourseChartLoginActivity;
import com.newthread.android.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/8/26.
 */
public class ExamArrangeActivity extends SherlockFragmentActivity {
    private static final String URL1 = "http://ids.scuec.edu.cn/amserver/UI/Login?goto=http://my.scuec.edu.cn/index.portal"; // 个人图书馆URL
    private static final String URL2 = "http://ssfw.scuec.edu.cn/ssfw/j_spring_ids_security_check"; // URL
    private static final String URL3 = "http://ssfw.scuec.edu.cn/ssfw/xsks/kcxx.do"; // 考试安排查询网址


    private KJHttp kjh;
    private String account, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_list);
        initData();
        initView();
    }

    private void initData() {
        MyPreferenceManager.init(getApplicationContext());
        account = MyPreferenceManager.getString("admin_system_account", "");
        password = MyPreferenceManager.getString("admin_system_password", "");
        connectPost(URL1);
//        if (!needLogin()) {
//            kjh = new KJHttp();
//            KJStringParams params = new KJStringParams();
//            params.put("IDToken1", account);
//            params.put("IDToken2", password);
//            kjh.post(URL1, params, new StringCallBack() {
//                @Override
//                public void onSuccess(String json) {
//                    Loger.V(json);
//                    kjh.urlGet(URL2, new StringCallBack() {
//                        @Override
//                        public void onSuccess(String json) {
//
//                            KJStringParams params = new KJStringParams();
//                            params.put("xnxqdm", "2013-2014-2");
//                            kjh.urlPost(URL3, params, new StringCallBack() {
//                                @Override
//                                public void onSuccess(String html) {
//                                    new ExamArrangeParser().parse(html);
//                                }
//                            });
//                        }
//                    });
//                }
//
//                @Override
//                public void onFailure(Throwable t, int errorNo, String strMsg) {
//                    super.onFailure(t, errorNo, strMsg);
//
//                }
//            });
//        }
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
    private boolean connectPost(String url) {

        HttpClient httpClient = new DefaultHttpClient(); // 新建HttpClient对象
        HttpPost httpPost = new HttpPost(url); // 新建HttpPost对象

        List<NameValuePair> params = new ArrayList<NameValuePair>(); // 使用NameValuePair来保存要传递的Post参数
        params.add(new BasicNameValuePair("IDToken1", account)); // 添加要传递的参数
        params.add(new BasicNameValuePair("IDToken2", password));

        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 20 * 1000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                20 * 1000);

        try {
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8); // 设置字符集
            httpPost.setEntity(entity); // 设置参数实体
            HttpResponse httpResp = httpClient.execute(httpPost); // 获取HttpResponse实例
            if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过

                String result = EntityUtils.toString(httpResp.getEntity(),
                        "UTF-8");
                   Loger.V(result);

            } else {
                // 响应未通过
                System.out.println(httpResp.getStatusLine().getStatusCode()
                        + "");
            }
        } catch (MalformedURLException e) {
            Logger.i("MalformedURLException", "MalformedURLException");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Logger.i("IllegalArgumentException", "IllegalArgumentException");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Logger.i("ClientProtocolException", "QUERY_ERROR");
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            Logger.i("SocketTimeoutException", "QUERY_ERROR");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.i("IOException", "QUERY_ERROR");

        } catch (Exception e) {
            Logger.i("Exception-connectPost", "QUERY_ERROR: " + e.toString());
            e.printStackTrace();
        }
        return false;
    }
}
