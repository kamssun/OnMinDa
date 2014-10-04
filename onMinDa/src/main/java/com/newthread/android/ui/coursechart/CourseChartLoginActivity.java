package com.newthread.android.ui.coursechart;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.newthread.android.bean.MyTable;
import com.newthread.android.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.EverydayCourse;
import com.newthread.android.bean.SingleCourseInfo;
import com.newthread.android.global.HandleMessage;

/**
 * 登录后，从教务系统获取课程信息
 *
 * @author _foumnder
 */
public class CourseChartLoginActivity extends SherlockActivity {
    private static final String TAG = "CourseChartLoginActivity";
    private HttpClient httpClient;
    private int loginResult = -1; // 登录结果
    private ProgressDialog progressDialog;

    private String account;
    private String password;
    private CheckBox rememberPassword; // 记住密码

    private ArrayList<EverydayCourse> weekCourse;
    private long lastClickTime = 0;

    private ArrayList<EverydayCourse> list; // 临时数据,用于转化(假设每天有6节课)
    private static final String URL1 = "http://ids.scuec.edu.cn/amserver/UI/Login?goto=http://my.scuec.edu.cn/index.portal"; // 个人图书馆URL
    private static final String URL2 = "http://ssfw.scuec.edu.cn/ssfw/j_spring_ids_security_check"; // URL
    private static final String URL3 = "http://ssfw.scuec.edu.cn/ssfw/pkgl/kcbxx/4/" + TimeUtil.getChangeCouserUrl() + ".do"; // 课程表URL
    private static final boolean Debug = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_chart_login);

        if (this.getIntent().getBooleanExtra("is_direct_login", true)) {
            MyPreferenceManager.init(getApplicationContext());
            if (!MyPreferenceManager.getBoolean("admin_system_isFirstLogin",
                    true)) {
                // 已登录成功
                Intent _intent = new Intent(this, CourseChartActivity.class);
                startActivity(_intent);
                this.finish();
            }
        }
        initData();
        initView();

    }

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            switch (msg.what) {
                case HandleMessage.QUERY_SUCCESS:
                    // 查询成功
                    performSuccess();
                    Log.v("0000", URL3);
                    Toast.makeText(getApplicationContext(), "登录成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case HandleMessage.QUERY_ERROR:
                    // 查询失败
                    Toast.makeText(getApplicationContext(), R.string.net_error,
                            Toast.LENGTH_SHORT).show();
                    break;
                case HandleMessage.NO_CONTENT:
                    // 无信息
                    Toast.makeText(getApplicationContext(), "请输入正确的账号和密码",
                            Toast.LENGTH_SHORT).show();
                    break;
                case HandleMessage.PARSE_ERROR:
                    // 无信息
                    Toast.makeText(getApplicationContext(), "课程表数据解析失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case HandleMessage.DELAYED:
                    Toast.makeText(getApplicationContext(), "教务系统又开始坑爹了。。。。",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    // 登录成功
    private void performSuccess() {
        // MyPreferenceManager.init(getApplicationContext());
        // 判断是否记住登录
        if (rememberPassword.isChecked()) {
            // 保存信息
            MyPreferenceManager.commitBoolean("admin_system_isFirstLogin",
                    false);
            MyPreferenceManager.commitString("admin_system_account", account);
            MyPreferenceManager.commitString("admin_system_password", password);
        }
        // 保存课程信息
        if (weekCourse != null) {
            AndroidDB.addCourse(getApplicationContext(), weekCourse);// 这句话有问题
        }
        // 跳转
        Intent _intent = new Intent(CourseChartLoginActivity.this,
                CourseChartActivity.class);
        startActivity(_intent);
        this.finish();
    }

    // 初始化数据
    private void initData() {
        // 临时数据,用于转化(假设每天有6节课)
        list = new ArrayList<EverydayCourse>(6);
    }

    // 初始化界面
    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setTitle("教务系统登录");

        // 账号
        final EditText et_account = (EditText) this
                .findViewById(R.id.course_login_account);
        // 密码
        final EditText et_pass = (EditText) this
                .findViewById(R.id.course_login_password);
        rememberPassword = (CheckBox) this
                .findViewById(R.id.course_login_checkbox);

        //
        MyPreferenceManager.init(getApplicationContext());
        if (!MyPreferenceManager.getBoolean("admin_system_isFirstLogin", true)) {
            et_account.setText(MyPreferenceManager.getString(
                    "admin_system_account", "").trim());
            et_pass.setText(MyPreferenceManager.getString(
                    "admin_system_password", "").trim());
        }

        // 登录
        TextView login = (TextView) this.findViewById(R.id.course_login_btn);
        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - lastClickTime < 3000) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();

                if (!StringUtils
                        .isEmpty(et_account.getText().toString().trim())
                        && !StringUtils.isEmpty(et_pass.getText().toString()
                        .trim())) {

                    account = et_account.getText().toString().trim();
                    password = et_pass.getText().toString().trim();
                    Log.v("金东平", "输入的账号");
                    // Login...
                    performLogin();
                } else if (Debug) {
                    Log.v("金东平", "学长的账号");
                    account = "11061181";
                    password = "05001X";
                    // Login...
                    performLogin();
                } else {
                    Toast.makeText(getApplicationContext(), "请输入账号和密码",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    // 登录过程
    private void performLogin() {
        progressDialog = new ProgressDialog(CourseChartLoginActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在登录...");
        progressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {
                if (connectPost(URL1)) {// 登录民大认证平台
                    Log.v("conent(", "true");
                    connect(URL2); // 登录教务系统师生端
                    connect(URL3); // 查询课程表
                } else {
                    Log.v("conent(", "false");
                }

                handler.sendEmptyMessage(loginResult);
            }

        }).start();
    }

    // 数据解析
    private String parse(String result) {
        weekCourse = new ArrayList<EverydayCourse>();
        Document doc = Jsoup.parse(result);
        Element e0 = doc.select("table.CourseFormTable").first();

        MyTable myTable = TableParser.parser(e0);
        for (int week = 2; week < myTable.getColLength(); week++) {
//            Log.v(TAG, "星期" + (week - 1));
            //每天的课程
            EverydayCourse everydayCourse = new EverydayCourse();
            for (int courseIndex = 1; courseIndex < myTable.getRowLength(); courseIndex++) {
//                Log.v(TAG, courseIndex + ": " + myTable.getCell(courseIndex, week));
                // 每节课信息
                SingleCourseInfo singleCourse = new SingleCourseInfo();
                singleCourse = CourseParser.parser(myTable.getCell(courseIndex, week));
                // 每日第几节
                singleCourse.setNumOfDay(courseIndex + "");
                //周几的课
                singleCourse.setNumOfWeek(week - 1);
//                Log.v(TAG, singleCourse.toString());
                everydayCourse.getDayOfWeek().add(singleCourse);
            }
            weekCourse.add(everydayCourse);
        }
        loginResult = HandleMessage.QUERY_SUCCESS;

//        Elements es0 = e0.getElementsByTag("tr");
//        Vector<Integer> add_course_position = new Vector<Integer>();
//        int acp_i = 0;
//        for (int j = 0; j < 11; j++) {
//
//            if (j == 0 || j == 2 || j == 4 || j == 6 || j == 8) {
//                continue;
//            }
//
//            // 每天的课程
//            EverydayCourse everydayCourse = new EverydayCourse();
//            int i = 0;
//            int num = 0;
//            // mormal: 9
//            int tds_num = es0.get(j).getElementsByTag("td").size();
//            for (; i < tds_num; i++) {
//                if (i == 0 || i == 1) {
//                    continue;
//                }
//
//                // 每节课信息
//                SingleCourseInfo singleCourse = new SingleCourseInfo();
//
//                // /////////////////////////////////////
//                // 按照此方法，自改图书解析方式
//                // /////////////////////////////////////
//                Document doc1 = Jsoup.parse(es0.get(j).getElementsByTag("td")
//                        .get(i).toString());
//                System.out.println("第" + j + "节"
//                        + "------------------------------------");
//                String item = doc1.body().text();
//
//                System.out.println(i + " body: " + doc1.body().text());
//                System.out.println("    " + i + " length: "
//                        + item.split(" ").length);
//
//                System.out.println("------------------------------------");
//                if (j == 7 && add_course_position.size() != 0
//                        && i == add_course_position.get(acp_i)) {
//                    SingleCourseInfo singleCourse3 = new SingleCourseInfo();
//                    singleCourse3.setHaveCourse(false);
//                    System.out.println("第7节第" + i + " 增加的没有课 " + "td数量"
//                            + tds_num); // 无课
//                    everydayCourse.getDayOfWeek().add(singleCourse3);
//                    acp_i++;
//                    if (acp_i == add_course_position.size()) {
//                        acp_i--;
//                    }
//                }
//                // 确保课程有足够信息时
//                if (item.length() > 4 && item.split(" ").length >= 4) {
//                    String leftItem = item.substring(item.indexOf("[") + 1,
//                            item.length());
//                    String[] items = leftItem.split(" ");
//
//                    // 有课
//                    singleCourse.setHaveCourse(true);
//
//                    // 课程名
//                    if (item.contains("[")) {
//                        singleCourse.setCourseName(item.substring(0,
//                                item.indexOf("[")));
//
//                        System.out.println("    " + i + " courseName: "
//                                + item.substring(0, item.indexOf("[")));
//                    }
//
//                    // 课程持续周数
////					singleCourse.setSustainTime(items[1]);
//                    singleCourse.setSustainTime(items[1].substring(1, items[1].length()));
//                    // System.out
//                    // .println("    " + i + " sustainTime: " + items[1]);
//
//                    // 每日第几节
//                    singleCourse.setNumOfDay(items[2].substring(2,
//                            items[2].length() - 1));
//                    // System.out.println("    " + i + " numOfDay: "
//                    // + items[2].substring(2, items[2].length() - 1));
//                    String last_time_str = items[2].substring(3,
//                            items[2].length() - 2);
//                    int last_time = Integer
//                            .valueOf(last_time_str.split("-")[1])
//                            - Integer.valueOf(last_time_str.split("-")[0]);
//
//                    if (last_time == 2 && j == 5) {
//
//                        add_course_position.add(i - num);
//                        for (int k = 0; k < add_course_position.size(); k++) {
//                            Log.v("sssssssssssss", add_course_position.get(k)
//                                    + "");
//                        }
//                        num++;
//                    }
//
//                    // 教师名
//                    singleCourse.setTeacherName(items[3]);
//                    // System.out
//                    // .println("    " + i + " teacherName: " + items[3]);
//
//                    // 教室号
//                    // 如果有教室
//                    Log.v("000000", "item: " + item + ";" + item.length());
//                    if (item.split(" ").length >= 6) {
//                        singleCourse.setClassromNum(items[5]);
//                        System.out.println("    " + i + " classrom: "
//                                + items[5]);
//                    } else {
//                        singleCourse.setClassromNum("No Classroom");
//                    }
//                } else {
//                    // 无课程信息
//                    singleCourse.setHaveCourse(false);
//                    System.out.println("    " + i + " No Course! "); // 无课
//                }
//                everydayCourse.getDayOfWeek().add(singleCourse);
//            }
//
//
//        }
//        convertData();

        return null;
    }

    private String parse_login_result(String result) {
        Document doc = Jsoup.parse(result);
        Elements AlrtErrTxts = doc.getElementsByClass("AlrtErrTxt");
        String str = "";
        if (AlrtErrTxts.hasText()) {
            str = AlrtErrTxts.get(0).text();
        }
        return str;
    }

    // 数据转换
    private void convertData() {
        weekCourse = new ArrayList<EverydayCourse>();
        int i = 0; // 假设每周有7天课

        try {
            for (; i < 7; i++) {
                ArrayList<SingleCourseInfo> dayOfWeek = new ArrayList<SingleCourseInfo>(); // 每天的课程
                int j = 0; // 假设每天有5节课
                for (; j < 5; j++) {
                    SingleCourseInfo singleCourse = new SingleCourseInfo(); // 每节课

                    singleCourse = list.get(j).getDayOfWeek().get(i);

                    // 周几的课
                    singleCourse.setNumOfWeek(i + 1);

                    dayOfWeek.add(singleCourse);
                }

                EverydayCourse everydayCourse = new EverydayCourse(dayOfWeek);
                weekCourse.add(everydayCourse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginResult = HandleMessage.QUERY_ERROR;
        }

        loginResult = HandleMessage.QUERY_SUCCESS;
    }

    // Post
    private boolean connectPost(String url) {

        httpClient = new DefaultHttpClient(); // 新建HttpClient对象
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
                if (parse_login_result(result).contains("failed")) {
                    loginResult = HandleMessage.NO_CONTENT;
                    return false;
                } else {
                    return true;
                }

            } else {
                // 响应未通过
                System.out.println(httpResp.getStatusLine().getStatusCode()
                        + "");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            loginResult = HandleMessage.NO_CONTENT;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            loginResult = HandleMessage.NO_CONTENT;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            loginResult = HandleMessage.QUERY_ERROR;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            loginResult = HandleMessage.QUERY_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            loginResult = HandleMessage.DELAYED;

        } catch (Exception e) {
            loginResult = HandleMessage.QUERY_ERROR;
            e.printStackTrace();
        }
        return false;
    }

    // Get
    private void connect(String url) {
        HttpConnectionParams
                .setConnectionTimeout(httpClient.getParams(), 20000); // 设置连接超时
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 20000); // 设置数据读取时间超时
        ConnManagerParams.setTimeout(httpClient.getParams(), 20000); // 设置从连接池中取连接超时
        Log.v("金东平0_0", "url" + url);
        HttpGet httpget = new HttpGet(url); // 获取请求

        try {
            HttpResponse response = httpClient.execute(httpget); // 执行请求，获取响应结果
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
                String result = EntityUtils.toString(response.getEntity(),
                        "UTF-8");
                Log.v("金东平2", result);
                if (!StringUtils.isEmpty(result) && url.equals(URL3)) {
                    parse(result);
                }
            } else {
                // 响应未通过
                loginResult = HandleMessage.QUERY_ERROR;
                System.out.println("connect: "
                        + response.getStatusLine().getStatusCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            loginResult = HandleMessage.NO_CONTENT;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            loginResult = HandleMessage.NO_CONTENT;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            loginResult = HandleMessage.QUERY_ERROR;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            loginResult = HandleMessage.QUERY_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            loginResult = HandleMessage.QUERY_ERROR;
            // } catch (Exception e) {
            // loginResult = HandleMessage.QUERY_ERROR;
            // Logger.i("Exception——connect", "QUERY_ERROR: " + e.toString());
            // e.printStackTrace();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
