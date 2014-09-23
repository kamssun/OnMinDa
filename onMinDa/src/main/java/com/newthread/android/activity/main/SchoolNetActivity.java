package com.newthread.android.activity.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.ui.coursechart.CourseChartLoginActivity;
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
    private Button connect;
    private String account, password;
    private static final String WIFINAME = "SCUEC";
    private static final String URL = "http://10.231.192.37/cgi-bin/srun_portal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_net);
//        account = "11061181";
//        password = "05001X";
        MyPreferenceManager.init(getApplicationContext());
        setLoginWifiData();
        initView();
        if (needLogin()) {
            showLoginDialog();
        } else {
            if (WifiHelper.getInstance(getApplicationContext()).getWifiState() == 1) {
                hintTv.setText("Wi-Fi未打开，请打开Wi-Fi后重试");
            } else {
                WifiInfo connectInfo=WifiHelper.getInstance(getApplicationContext()).getConnectionInfo();
                if (!connectInfo.getSSID().equals("\""+WIFINAME+"\"")) {
                    connectWifi();
                }else{
                    hintTv.setText("您已经连接上校园网了");
                    connectPost();
                }
            }
        }
    }

    private void connectWifi() {
        setConnectParam();
        WifiHelper.getInstance(getApplicationContext()).scanWifi();
    }

    private void setConnectParam() {
        WifiHelper.getInstance(getApplicationContext()).registWifiRecevier(new WifiHelper.CallBack() {
            @Override
            public void getScanResult(List<ScanResult> scanResults) {
                ScanResult scanResult = null;
                for (ScanResult tScanResult : scanResults) {
                    if (tScanResult.SSID.equals(WIFINAME)) {
                        scanResult = tScanResult;
                    }
                }
                if (scanResult != null) {
                    if (WifiHelper.getInstance(getApplicationContext()).connectWifi(scanResult)) {
                       while (!WifiHelper.getInstance(getApplicationContext()).isWifiConnect()) {
                           try {
                               Thread.currentThread();
                               Thread.sleep(100);
                           } catch (InterruptedException ie) {
                           }
                       }
                        connectPost();
                    } else {
                        hintTv.setText("自动连接校园失败，请手动登录校园网");
                    }
                } else {
                    hintTv.setText("未扫描到校园网");
                }
                WifiHelper.getInstance(getApplicationContext()).unRegistWifiRecevier();
            }
        });
    }

    private void setLoginWifiData() {
        account = MyPreferenceManager.getString("SchoolWifiName", "");
        password = MyPreferenceManager.getString("SchoolWifiPassWord", "");
        if (account .equals("")|| password.equals("")) {
            account = MyPreferenceManager.getString("admin_system_account", "");
            password = MyPreferenceManager.getString("admin_system_password", "");
        }
    }

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setTitle("校园网连接");
        hintTv = (TextView) this.findViewById(R.id.hint);
        connect = (Button)this.findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintTv.setText("正在登录");
                connectPost();
                connect.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 扫描的校园网后,才执行该方法.
     */
    private void connectPost() {
        setLoginWifiData();
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
                if (json.contains("/srun_portal.html?action=login_ok")) {
                    connectSucessful();
                } else {
                    connectFail();
                }
            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                hintTv.setText("连接校园网失败，请返回后重试");
            }
        });
    }

    private void connectSucessful() {
        hintTv.setText("登录成功");
        hintTv.setTextColor(Color.RED);
        hintTv.setTextSize(28);
        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
        MyPreferenceManager.commitString("SchoolWifiName", account);
        MyPreferenceManager.commitString("SchoolWifiPassWord", password);
    }

    private void connectFail() {
        hintTv.setText("登录失败,若您已经登录,请多次尝试重新登录.若密码错误,请更改默认登录");
        connect.setVisibility(View.VISIBLE);
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
            showLoginDialog2();
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
        builder.setMessage("请先尝试登录教务系统,如若您改了校园网密码，请点击右上角的“更改默认登录”按钮");
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

    protected void showLoginDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录");
        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_login_dialog, null);
        builder.setView(view);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                account = ((EditText) view.findViewById(R.id.username_edit)).getText().toString();
                password = ((EditText) view.findViewById(R.id.password_edit)).getText().toString();
                connectPost();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
