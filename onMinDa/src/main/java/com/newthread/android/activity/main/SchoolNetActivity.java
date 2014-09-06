package com.newthread.android.activity.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.newthread.android.R;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;

/**
 * 校园网
 */
public class SchoolNetActivity extends SherlockActivity {
    private TextView hintTv;
    private Button reconnectBtn;
    private String account, password;
    private static final String URL = "http://10.231.192.37/cgi-bin/srun_portal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_net);

        account = "11061181";
        password = "05001X";

//        account = MyPreferenceManager.getString("", "");
//        password = MyPreferenceManager.getString("", "");

        if (false) {

        } else {

        }

        initView();

        connect();
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
        hintTv.setText("正在连接");
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
                Log.d("", "提示信息：" + json);
                hintTv.setText("连接成功");
                hintTv.setTextColor(Color.RED);
                hintTv.setTextSize(28);
                Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_LONG).show();
                reconnectBtn.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                hintTv.setText("连接失败，请打开Wi-Fi并连接SCUEC热点后重新连接");
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                reconnectBtn.setVisibility(View.VISIBLE);
                // hint text
            }
        });
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
