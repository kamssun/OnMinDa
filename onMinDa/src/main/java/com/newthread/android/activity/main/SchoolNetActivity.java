package com.newthread.android.activity.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.bean.ExamArrangeInfo;
import com.newthread.android.ui.coursechart.CourseChartLoginActivity;
import com.newthread.android.util.Loger;
import com.newthread.android.util.MyPreferenceManager;
import com.newthread.android.util.StringUtils;
import com.newthread.android.util.WifiHelper;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;

import java.util.List;

/**
 * 校园网
 */
public class SchoolNetActivity extends SherlockActivity {
    private TextView hintTv;
    private Button reconnectBtn;
    private String account, password;
    private static final String WIFINAME = "SCUEC";
    private static final String URL = "http://10.231.192.37/srun_portal.html?&ac_id=8&sys=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_net);
        account = "11061181";
        password = "05001X";
        MyPreferenceManager.init(getApplicationContext());
        account = MyPreferenceManager.getString("admin_system_account", "");
        password = MyPreferenceManager.getString("admin_system_password", "");
        initView();
        if (needLogin()) {
            showLoginDialog();
        } else {
//            if (WifiHelper.getInstance(getApplicationContext()).getWifiState() == 1) {
//                hintTv.setText("Wi-Fi未打开，请打开Wi-Fi后重试");
//            } else {
//                WifiHelper.getInstance(getApplicationContext()).registWifiRecevier(new WifiHelper.CallBack() {
//                    @Override
//                    public void getScanResult(List<ScanResult> scanResults) {
//                        ScanResult scanResult = null;
//                        for (ScanResult tScanResult : scanResults) {
//                            if (tScanResult.SSID.equals(WIFINAME)) {
//                                scanResult = tScanResult;
//                            }
//                        }
//                        if (scanResult != null) {
//                            hintTv.setText("扫描到校园网" + scanResult.SSID + "尝试自动连接。。。");
//                            if (WifiHelper.getInstance(getApplicationContext()).connectWifi(scanResult)) {
//                                hintTv.setText("自动连接校园成功，尝试登录校园网");
//                                connect();
//                            } else {
//                                hintTv.setText("自动连接校园失败，请手动登录校园网");
//                            }
//                            reconnectBtn.setVisibility(View.VISIBLE);
//                        } else {
//                            hintTv.setText("未扫描到校园网");
//                        }
//                        WifiHelper.getInstance(getApplicationContext()).unRegistWifiRecevier();
//                    }
//                });
//                WifiHelper.getInstance(getApplicationContext()).scanWifi();
//            }
            connect();
        }
    }

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setTitle("校园网连接");
        hintTv = (TextView) this.findViewById(R.id.hint);
        reconnectBtn = (Button) this.findViewById(R.id.connect);
        reconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });
    }

    private void connect() {
        reconnectBtn.setVisibility(View.GONE);
        KJHttp kjh = new KJHttp();
        KJStringParams params = new KJStringParams();
        params.put("username", account);
        params.put("password", password);
        params.put("action", "login");
        params.put("ac_id", "8");
        params.put("type", "1");
        params.put("pop", "1");
        params.put("is_ldap", "1");
        params.put("nas_init_port", "0");
        kjh.post(URL, params, new StringCallBack() {
            @Override
            public void onSuccess(String json) {
                Loger.V(json);
                hintTv.setText("连接成功");
                hintTv.setTextColor(Color.RED);
                hintTv.setTextSize(28);
                Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_LONG).show();
                reconnectBtn.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                hintTv.setText("连接失败，请打开Wi-Fi并连接SCUEC热点后重新连接");
                reconnectBtn.setVisibility(View.VISIBLE);
                // hint text
            }
        });
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("更改默认登录").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
        if (item.getTitle().equals("更改默认登录")) {


        }
        return super.onMenuItemSelected(featureId, item);

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
        builder.setMessage("请先尝试登录教务系统,如若您改了校园网密码，请按右上角的“更改默认登录”按钮");
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

}
